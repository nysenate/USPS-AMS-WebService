<?php
/**
 * The script downloads USPS EPF product files
 *
 * Default behavior: download newest file that hasn't been downloaded yet
 * epf.php
 *
 * Ignore file checks and download most recent file, overwriting existing
 * epf.php --force
 *
 * For quiet output, hide anything less than a warning (WARN)
 * epf.php --quiet
 *
 * Also works with force
 * epf.php --force --quiet
 *
 */

// required config parsing
require(realpath(dirname(__FILE__).'/lib/utils.php'));

# CLI options
$opts = getopt("fq", array("force", "quiet"));
$force_update = false;

# Set global vars
$g_log_level = null;
$g_log_file = null;

if (isset($opts['q']) || isset($opts['quiet'])) {
  $g_log_level = WARN;
}

if (isset($opts['f']) || isset($opts['force'])) {
  $force_update = true;
}

$config = load_config();
if ($config === false) {
  echo "Unable to load configuration file; exiting\n";
  exit(1);
}

if ($g_log_level === null && isset($config['debug']['level'])) {
  $lvl = $config['debug']['level'];
  if (is_numeric($lvl)) {
    $g_log_level = $lvl;
  }
  else {
    echo "debug.level setting [$lvl] is not valid; defaulting to WARN\n";
    $g_log_level = WARN;
  }
}

if ($g_log_file === null && isset($config['debug']['file'])) {
  $g_log_file = get_log_file($config['debug']['file']);
}

$output_dir = $config['output']['dir'];
if (!file_exists($output_dir)) {
  echo "$output_dir: Output directory not found\n";
  exit(1);
}

$products = $config['products'];
if (!isset($products['product'])) {
  log_(ERROR, "At least one USPS product must be specified in the config file\n");
  exit(1);
}
else if (!is_array($products['product'])) {
  $products['product'] = array($products['product']);
}


# Test to see if the server is online before we start
$version = epf_version();
log_(INFO, "USPS EPF server version = $version");


$login_keys = epf_login($config);
if (!$login_keys) {
  log_(ERROR, "Unable to authenticate to the server");
  exit(1);
}

// loop through the config, allowing several productFiles
foreach ($products['product'] as $val) {
  $val_parts = explode(':', $val);
  $product = array('code' => $val_parts[0], 'id' => $val_parts[1]);
  $filelist = epf_list($login_keys, $product);
  $numfiles = count($filelist);

  if ($numfiles < 1) {
    log_(INFO, "No files to download for this product");
    continue;
  }

  // we only care about the most recent file
  $lastfile = $filelist[$numfiles - 1];
  $lastfileid = $lastfile['fileid'];
  $lastfilepath = $lastfile['filepath'];
  $lastfilestatus = $lastfile['status'];
  $lastfilefulfill = $lastfile['fulfilled'];

  log_(INFO, "About to download file '$lastfilepath' (id=$lastfileid, status='$lastfilestatus', fulfilled=$lastfilefulfill)");

  // generate a local filename for download
  $lastfilename = basename($lastfilepath);
  $pathsegs = explode('/', $lastfilepath);
  $numpathsegs = count($pathsegs);
  $filename = $pathsegs[$numpathsegs-2].'_'.$pathsegs[$numpathsegs-1];
  $outfile = $output_dir.DIRECTORY_SEPARATOR.$filename;

  if (!$force_update && file_exists($outfile)) {
    log_(WARN, "Download file '$lastfilepath' already exists as '$outfile'");
    continue;
  }
  else {
    log_(INFO, "Downloaded file will be saved as '$outfile'");
  }

  if (!epf_status($login_keys, $lastfileid, 'S')) {
    continue;
  }
  $rc = epf_download($login_keys, $lastfileid, $lastfilepath, $outfile);
  if ($rc) {
    epf_status($login_keys, $lastfileid, 'C');
    log_(INFO, "Completed file download for product code=".$product['code'].", id=".$product['id']);
  }
  else {
    epf_status($login_keys, $lastfileid, 'X');
    log_(ERROR, "Download failed");
  }
}


function epf_resp_ok($r)
{
  if (isset($r['response']) && $r['response'] === 'success'
      && isset($r['http_code']) && $r['http_code'] === 200) {
    return true;
  }
  else {
    return false;
  }
} // epf_resp_ok()


function epf_version()
{
  $resp = send_request('epf/version');
  if (epf_resp_ok($resp)) {
    log_(INFO, "Server is online");
    return $resp['version'];
  }
  else {
    log_(ERROR, "Server is offline (ERROR ".$resp['meta']['http_code'].")");
    return null;
  }
} // epf_version()


/**
 * Login to the site and retrieve logonkey & tokenkey
 * @return [array] login credentials
 */
function epf_login($cfg)
{
  if (!isset($cfg['credentials'])) {
    log_(ERROR, "Credentials must be specified");
    return null;
  }

  $creds = $cfg['credentials'];

  if (!isset($creds['login']) || !isset($creds['pword'])) {
    log_(ERROR, "Both login and password must be specified");
    return null;
  }

  $params = array(
    'login' => $creds['login'],
    'pword' => $creds['pword']
  );

  $resp = send_request('epf/login', $params);

  if (epf_resp_ok($resp)) {
    log_(INFO, 'Login was successful');
    return $resp;
  }
  else {
    log_(ERROR, "Login request failed");
    var_dump($resp);
    return null;
  }
} // epf_login()


function epf_list(&$keys, $product)
{
  $pcode = $product['code'];
  $pid = $product['id'];
  log_(INFO, "Getting file list for product code=$pcode, id=$pid");
  $params = array(
    'logonkey' => $keys['logonkey'],
    'tokenkey' => $keys['tokenkey'],
    'productcode' => $pcode,
    'productid' => $pid,
    'status' => "NSXC",    // filter by download status - optional
    // "fulfilled"=>$abc,  // filter by fulfilled date - optional
  );

  $resp = send_request('download/list', $params);

  if (epf_resp_ok($resp)) {
    $keys['logonkey'] = $resp['logonkey'];
    $keys['tokenkey'] = $resp['tokenkey'];
    if (!empty($resp['fileList'])) {
      log_(INFO, "Retrieved file list for product code=$pcode, id=$pid");
      return $resp['fileList'];
    }
    else {
      log_(WARN, "File list was empty; no new files for product");
      return null;
    }
  }
  else {
    log_(ERROR, "Unable to retrieve file list for product code=$pcode, id=$pid");
    return null;
  }
} // epf_list()


function epf_status(&$keys, $fileid, $status)
{
  log_(DEBUG, "Setting status for file $fileid to '$status'");

  $params = array(
    'logonkey' => $keys['logonkey'],
    'tokenkey' => $keys['tokenkey'],
    'newstatus' => $status,
    'fileid' => $fileid
  );

  $resp = send_request('download/status', $params);

  if (epf_resp_ok($resp)) {
    $keys['logonkey'] = $resp['logonkey'];
    $keys['tokenkey'] = $resp['tokenkey'];
    log_(INFO, "Updated file status for file $fileid to '$status'");
    return true;
  }
  else {
    log_(WARN, "Unable to update status to '$status' for file $fileid");
    return false;
  }
} // epf_status()


function epf_download(&$keys, $fileid, $filepath, $outfile)
{
  log_(INFO, "About to download file '$filepath' (id=$fileid)");

  // set some headers to save & access the file
  $headers = array(
    'filepath'  => $filepath,
    'fileoutput' => $outfile
  );

  # Download the file
  $params = array(
    'logonkey' => $keys['logonkey'],
    'tokenkey' => $keys['tokenkey'],
    'fileid' => $fileid
  );

  $resp = send_request('download/file', $params, $headers);

  // For downloaded files, there is no response body, since the response
  // is the actual file being saved to disk.
  if ($resp['http_code'] == 200) {
    $keys['logonkey'] = $resp['logonkey'];
    $keys['tokenkey'] = $resp['tokenkey'];
    log_(INFO, "Downloaded file '$filepath' (id=$fileid)");
    return true;
  }
  else {
    log_(WARN, "Unable to download file '$filepath' (id=$fileid)");
    return false;
  }
} // epf_download()

?>
