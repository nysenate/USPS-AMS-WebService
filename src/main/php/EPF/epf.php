<?php
/**
 * epf.php - Download EPF files from USPS
 *
 * Organization: New York State Senate
 * Author: Ken Zalewski
 * Last revised: 2017-09-20
 *
 * Examples of usage:
 *
 * Default behavior, which downloads all of the latest files:
 *   $ php epf.php
 *
 * Ignore file checks, and overwrite any existing files of the same name:
 *   $ php epf.php --force
 *
 * Quiet mode, which only logs WARN or ERROR messages:
 *   $ php epf.php --quiet
 *
 * Inhibit the download of any current files that have already been downloaded:
 *   $ php epf.php --skip-completed
 *
 * Simply print the EPF server version and exit:
 *   $ php epf.php --version
 *
 * Perform a "dry run" of the full process without actually downloading files:
 *   $ php epf.php --no-download
 */

require realpath(dirname(__FILE__).'/utils.php');

// Set global vars
$g_log_level = null;
$g_log_file = null;
$g_api_url = null;

// CLI options
$opts = getopt('fhnqsv', array('force', 'help', 'no-download', 'quiet',
                               'skip-completed', 'version'));

if (isset($opts['h']) || isset($opts['help'])) {
  $prog = $argv[0];
  echo "Usage: {$argv[0]} [--force] [--help] [--no-download] [--quiet] [--skip-completed] [--version]\n";
  exit(0);
}

$force_update = false;
$no_download = false;
$skip_completed = false;
$version_only = false;

if (isset($opts['f']) || isset($opts['force'])) {
  $force_update = true;
}

if (isset($opts['n']) || isset($opts['no-download'])) {
  $no_download = true;
}

if (isset($opts['q']) || isset($opts['quiet'])) {
  $g_log_level = WARN;
}

if (isset($opts['s']) || isset($opts['skip-completed'])) {
  $skip_completed = true;
}

if (isset($opts['v']) || isset($opts['version'])) {
  $version_only = true;
}

$config = load_config();
if ($config === false) {
  echo "Unable to load configuration file; exiting\n";
  exit(1);
}

if ($g_log_level === null && isset($config['log_level'])) {
  $lvl = $config['log_level'];
  if (is_numeric($lvl)) {
    $g_log_level = $lvl;
  }
  else {
    echo "log_level setting [$lvl] is not valid; defaulting to WARN\n";
    $g_log_level = WARN;
  }
}

if ($g_log_file === null && isset($config['log_file'])) {
  $g_log_file = get_log_file($config['log_file']);
}

$g_api_url = $config['api_url'];

$output_dir = $config['output_dir'];
if (!file_exists($output_dir)) {
  echo "$output_dir: Output directory not found\n";
  exit(1);
}

// Test to see if the server is online before we start
$vinfo = epf_version();
if ($vinfo === null) {
  log_(ERROR, "Unable to obtain server version info");
  exit(1);
}
log_(INFO, "USPS EPF server version: ".$vinfo['version']);
log_(INFO, "USPS EPF build date: ".$vinfo['build']);

if ($version_only === true) {
  echo $vinfo['version'].' @ '.$vinfo['build']."\n";
  exit(0);
}


$login_keys = epf_login($config['username'], $config['password']);
if (!$login_keys) {
  log_(ERROR, "Unable to authenticate to the server");
  exit(1);
}
log_(DEBUG, "USPS keys: logon={$login_keys['logonkey']}, token={$login_keys['tokenkey']}");

$allfiles = epf_listall($login_keys);
$last_timestamp = get_last_fulfilled($allfiles);

log_(DEBUG, "Full file list: ".print_r($allfiles, true));
log_(INFO, "File count: ".count($allfiles)."; last fulfilled timestamp: $last_timestamp");

// Only download the subset of files that match the last fulfilled timestamp.
foreach ($allfiles as $f) {
  $fname = $f['filepath'].$f['filename'];
  $fid = $f['fileid'];
  $fstat = $f['status'];
  $fprod = $f['productcode'].':'.$f['productid'];

  if ($f['fulfilled'] != $last_timestamp) {
    log_(DEBUG, "Skipping file '$fname' (id=$fid, status='$fstat', product=$fprod), since timestamp does not match '$last_timestamp'");
    continue;
  }
  else if ($fstat == 'C' && $skip_completed == true) {
    log_(INFO, "Skipping file '$fname' (id=$fid, status='$fstat', product=$fprod), since it was already downloaded");
    continue;
  }

  log_(INFO, "About to download file '$fname' (id=$fid, status='$fstat', product=$fprod)");

  // Generate a local filename for download.
  $ts = str_replace('-', '', $last_timestamp);
  $outfile = $output_dir.'/'.$ts.'_'.$f['filename'];

  if (!$force_update && file_exists($outfile)) {
    log_(WARN, "Download file '$fname' (id=$fid, status='$fstat', product=$fprod) already exists as '$outfile'; skipping");
    continue;
  }
  else {
    log_(INFO, "Downloaded file '$fname' will be saved as '$outfile'");
  }

  if ($no_download === true) {
    log_(WARN, "Skipping the actual download of file '$fname' since --no-download was specified");
  }
  else {
    if (epf_status($login_keys, $fid, 'S') === false) {
      continue;
    }

    $rc = epf_download($login_keys, $fid, $outfile);
    if ($rc === true) {
      epf_status($login_keys, $fid, 'C');
      log_(INFO, "Downloaded file '$fname' (id=$fid, product=$fprod)");
    }
    else {
      epf_status($login_keys, $fid, 'X');
      log_(ERROR, "Download failed for file '$fname' (id=$fid, product=$fprod)");
    }
  }
}


function epf_resp_ok($r)
{
  if (isset($r['logonkey']) && isset($r['tokenkey'])) {
    log_(DEBUG, "New keys: logon={$r['logonkey']}, token={$r['tokenkey']}");
  }

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
    log_(DEBUG, "Server is online");
    return $resp;
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
function epf_login($user, $pass)
{
  $params = array(
    'login' => $user,
    'pword' => $pass
  );

  $resp = send_request('epf/login', $params);

  if (epf_resp_ok($resp)) {
    log_(DEBUG, 'Login was successful');
    return $resp;
  }
  else {
    log_(ERROR, "Login request failed");
    var_dump($resp);
    return null;
  }
} // epf_login()


function epf_list(&$keys, $pcode, $pid)
{
  log_(DEBUG, "Getting file list for product code=$pcode, id=$pid");
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
    // Refresh the keys, since they change on each API call
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


function epf_listall(&$keys)
{
  log_(DEBUG, "Getting full file list for all products");

  $resp = send_request('download/dnldlist', $keys);

  if (epf_resp_ok($resp)) {
    // Refresh the keys, since they change on each API call
    $keys['logonkey'] = $resp['logonkey'];
    $keys['tokenkey'] = $resp['tokenkey'];
    if (!empty($resp['dnldfileList'])) {
      log_(INFO, "Retrieved full file list; count={$resp['reccount']}");
      return $resp['dnldfileList'];
    }
    else {
      log_(WARN, "File list was empty; no files were found for this account");
      return null;
    }
  }
  else {
    log_(ERROR, "Unable to retrieve full file list");
    return null;
  }
} // epf_listall()


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
    log_(INFO, "Updated file status for file id=$fileid to '$status'");
    return true;
  }
  else {
    log_(WARN, "Unable to update status to '$status' for file id=$fileid");
    return false;
  }
} // epf_status()


function epf_download(&$keys, $fileid, $outfile)
{
  log_(DEBUG, "About to download file id=$fileid => $outfile");

  $params = array(
    'logonkey' => $keys['logonkey'],
    'tokenkey' => $keys['tokenkey'],
    'fileid' => $fileid
  );

  $resp = send_request('download/epf', $params, $outfile);

  if (epf_resp_ok($resp)) {
    $keys['logonkey'] = $resp['logonkey'];
    $keys['tokenkey'] = $resp['tokenkey'];
    log_(DEBUG, "Downloaded file id=$fileid");
    return true;
  }
  else {
    log_(WARN, "Unable to download file id=$fileid");
    return false;
  }
} // epf_download()


function get_last_fulfilled($files)
{
  $ts = null;
  foreach ($files as $f) {
    if ($ts === null || $f['fulfilled'] > $ts) {
      $ts = $f['fulfilled'];
    }
  }
  return $ts;
} // get_last_fulfilled()

?>
