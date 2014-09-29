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
 * For quiet output, hide anything less then a warning (WARN)
 * epf.php -q
 * Also works with force
 * epf.php --force -q
 *
 */

// required config parsing
require(realpath(dirname(__FILE__).'/lib/utils.php'));

# CLI options
$longopts = array("force", "quiet");
$cli = getopt("fq", $longopts);

$action = (isset($cli['f']) || isset($cli['force'])) ? "Clear" : "Update" ;

# Set log vars
$g_log_level = null;
$g_log_file = null;

if (isset($cli['q']) || isset($cli['quiet'])) {
  $g_log_level = WARN;
}

$config = load_config();
if ($config === false) {
  echo "Exiting\n";
  exit(1);
}

if ($g_log_level === null && isset($config['debug']['level'])) {
  $g_log_level = get_log_level($config['debug']['level']);
}
if ($g_log_file === null && isset($config['debug']['file'])) {
  $g_log_file = get_log_file($config['debug']['file']);
}

# reformat the files listed in the config
foreach ($config['file']['code'] as $key => $value) {
  $productFiles[$key]['code'] = $value;
}
foreach ($config['file']['id'] as $key => $value) {
  $productFiles[$key]['id'] = $value;
}

# Test to see if the Server is online before we start
$test = send_request('epf/version');
if ($test['body']['response'] === 'success' &&  $test['meta']['http_code'] === 200) {
  log_(INFO, "Server is online, Version ".$test['body']['version']);
}
else {
  log_(ERROR, "Server is offline (ERROR ".$test['meta']['http_code']."); exiting script");
  exit(1);
}

$login = login($config);
if (!$login) {
  log_(ERROR, "Unable to authenticate to the server");
  exit(1);
}

$logonkey = $login['logonkey'];
$tokenkey = $login['tokenkey'];

// loop through the config, allowing several productFiles
foreach ($productFiles as $key => $value) {
  log_(INFO, "Getting List for FILE Code:".$value['code'].", ID:".$value['id']);
  $listpost = array(
    "logonkey"=>$logonkey, // epf logon key required
    "tokenkey"=>$tokenkey, // epf security token required
    "productcode"=>$value['code'], // epf product code required
    "productid"=>$value['id'], // epf product id required
    "status"=> "NSXC", // filter by download status - optional
    // "fulfilled"=>$abc, // filter by fulfilled date - optional
  );

  $list[$key] = send_request('download/list', $listpost);
  if ($list[$key]['body']['response'] === 'success' && $list[$key]['meta']['http_code'] === 200 && !empty($list[$key]['body']['fileList'])) {
    $logonkey = $list[$key]['body']['logonkey'];
    $tokenkey = $list[$key]['body']['tokenkey'];
    $filelist = $list[$key]['body']['fileList'];
    $filecount =count($list[$key]['body']['fileList']);
    log_(INFO, "List was Successful, Found ".$filecount." Items");
    // var_dump($list[$key]['body']);
  }
  elseif ($list[$key]['meta']['http_code'] === 403) {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }
  elseif (empty($list[$key]['body']['fileList'])) {
    log_(ERROR, "List was Empty, No new files for :".$value['code'].", ID :".$value['id']." With Filter ".$Filter);
    exit(1);
  }
  else{
    log_(ERROR, "List Request Failed, Product Code :".$value['code'].", ID :".$value['id']);
    exit(1);
  }

  // we only care about the most recent file
  $download = $filelist[$filecount-1];

  // generate a local filename for download
  $path = explode('/', $download['filepath']);
  $filename = $path[6].'_'.$path[7];

  // set some headers to save & access the file
  $headers = array(
    "filepath"  => $download['filepath'], // fileid from file list required
    "fileoutput"  => $config['destination']['download_path'].'/'.$filename // fileid from file list required
  );
  if ($action === "Update" && file_exists($headers['fileoutput'])) {
    log_(WARN, "Latest File exits in system at: ".$headers['fileoutput']);
    exit();
  }else{
    log_(INFO, "Downloading most recent file: ".$headers['filepath']);
    log_(INFO, "File will be placed in: ".$headers['fileoutput']);
  }

  # Mark the file as started
  $statuspost = array(
    "logonkey" => $logonkey, // epf logon key required
    "tokenkey" => $tokenkey, // epf security token required
    "newstatus"=> 'S', // download started
    "fileid"   => $download['fileid'] // fileid from file list required
  );

  $status[$filename] = send_request('download/status', $statuspost);
  if ($status[$filename]['meta']['http_code'] === 200) {
    log_(INFO, "Updated File status to 'Started'");
    $logonkey = $status[$filename]['body']['logonkey'];
    $tokenkey = $status[$filename]['body']['tokenkey'];
  }
  else {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }

  # Download the file
  $filepost = array(
    "logonkey"=>$logonkey, // epf logon key required
    "tokenkey"=>$tokenkey, // epf security token required
    "fileid"  =>$download['fileid'] // fileid from file list required
  );

  $files[$filename] = send_request('download/file', $filepost, $headers);
  if ($files[$filename]['meta']['http_code'] === 200) {
    $logonkey = $files[$filename]['meta']['User-Logonkey'];
    $tokenkey = $files[$filename]['meta']['User-Tokenkey'];
  }
  else {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }

  # Mark the file as downloaded
  $statuspost = array(
    "logonkey"=> $logonkey, // epf logon key required
    "tokenkey"=> $tokenkey, // epf security token required
    "newstatus"=>'C', // download completed
    "fileid"  => $download['fileid'] // fileid from file list required
  );
  $status[$filename] = send_request('download/status', $statuspost);
  if ($status[$filename]['meta']['http_code'] === 200) {
    log_(INFO, "Updated File status to 'Completed'");

    $logonkey = $status[$filename]['body']['logonkey'];
    $tokenkey = $status[$filename]['body']['tokenkey'];
  }
  else {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }
  log_(INFO, "Completed FILE Code:".$value['code'].", ID:".$value['id']);
}


/**
 * Login to the site and retrieve logonkey & tokenkey
 * @return [array]    login credentials
 */
function login($cfg)
{
  $loginpost = array(
    'login'=>$cfg['credentials']['login'],
    'pword'=>$cfg['credentials']['pword']
  );

  $login = send_request('epf/login', $loginpost);

  if ($login['body']['response'] === 'success' && $login['meta']['http_code'] == 200) {
    log_(INFO, 'Login was Successful');
    return $login['body'];
  }
  else {
    log_(ERROR, "Login Request Failed");
    var_dump($login);
    return null;
  }
} // login()


?>
