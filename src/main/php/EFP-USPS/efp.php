<?php
/**
 * The script downloads USPS EFP product files
 *
 * Default behavior: download newest file that hasn't been downloaded yet
 * efp.php
 *
 * Ignore file checks and download most recent file, overwriting existing
 * efp.php --force
 *
 * For quite output
 * efp.php -q
 * efp.php --force -q
 *
 */

// required config parsing
require(realpath(dirname(__FILE__).'/lib/utils.php'));
$config = load_config();
if ($config === false) {
  exit(1);
}


# CLI options
$longopts  = array("force");
$cli = getopt("fq",$longopts);
$action = (isset($cli['f'])|| isset($cli['force'])) ? "Clear" : "Update" ;
# Set log vars
$g_log_level = (isset($cli['q'])) ? 2 : get_log_level($config['debug']);
$g_log_file = get_log_file($config['debug']);

# reformat the config products
foreach ($config['file']['code'] as $key => $value) {
  $productFiles[$key]['code'] = $value;
}
foreach ($config['file']['id'] as $key => $value) {
  $productFiles[$key]['id'] = $value;
}

# Test to see if the Server is online before we start
$test = curl('https://epfws.usps.gov/ws/resources/epf/version');
if ($test['body']['response'] === "success" &&  $test['meta']['http_code'] === 200) {
  log_(INFO, "Server is online, Version ".$test['body']['version']);
}else{
  log_(ERROR, "Server is offline (ERROR ".$test['meta']['http_code']."), Exiting Script");
  exit(1);
}

/**
 * Login to the site and retrieve logonkey & tokenkey
 * @return [array]    login credentials
 */
function login()
{
  global $config;
  $loginpost = array(
    'login'=>$config['credentials']['login'],
    'pword'=>$config['credentials']['pword']
  );
  $login = curl('https://epfws.usps.gov/ws/resources/epf/login',$loginpost);
  if ($login['body']['response'] === "success" && $login['meta']['http_code'] === 200) {
    log_(INFO, "Login was Successful");
    return array('logonkey'=>$login['body']['logonkey'],'tokenkey'=>$login['body']['tokenkey']);
  }else{
    log_(ERROR, "Login Request Failed");
    var_dump($login);
    exit(1);
  }
}

$login = login();
$logonkey = $login['logonkey'];
$tokenkey = $login['tokenkey'];

// loop through the config, allowing several productFiles
foreach ($productFiles as $key => $value) {
  log_(INFO, "Getting List for FILE Code:".$value['code'].", ID:".$value['id']);
  $listpost =   array(
    "logonkey"=>$logonkey, // epf logon key required
    "tokenkey"=>$tokenkey, // epf security token required
    "productcode"=>$value['code'], // epf product code required
    "productid"=>$value['id'], // epf product id required
    "status"=> "NSXC", // filter by download status - optional
    // "fulfilled"=>$abc, // filter by fulfilled date - optional
  );
  $list[$key] = curl('https://epfws.usps.gov/ws/resources/download/list',$listpost);
  if ($list[$key]['body']['response'] === "success" && $list[$key]['meta']['http_code'] === 200 && (!empty($list[$key]['body']['fileList']))) {
    $logonkey = $list[$key]['body']['logonkey'];
    $tokenkey = $list[$key]['body']['tokenkey'];
    $filelist = $list[$key]['body']['fileList'];
    $filecount =count($list[$key]['body']['fileList']);
    log_(INFO, "List was Successful, Found ".$filecount." Items");
    // var_dump($list[$key]['body']);
  } elseif ($list[$key]['meta']['http_code'] === 403) {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }elseif (empty($list[$key]['body']['fileList'])) {
    log_(ERROR, "List was Empty, No new files for :".$value['code'].", ID :".$value['id']." With Filter ".$Filter);
    exit(1);
  }else{
    log_(ERROR, "List Request Failed, Product Code :".$value['code'].", ID :".$value['id']);
    exit(1);
  }

  // we only care about the last file
  $download = $filelist[$filecount-1];
  // set some headers to save & access the file
  $path = explode('/', $download['filepath']);
  $filename = $path[6].'_'.$path[7];


  $headers = array(
    "filepath"  => $download['filepath'], // fileid from file list required
    "fileoutput"  => $config['destination']['download_path'].'/'.$filename // fileid from file list required
  );
  if ($action === "Update" && file_exists($headers['fileoutput'])) {
    log_(WARN, "Latest File exits in system at: ".$headers['fileoutput']);
    exit();
  }else{
    log_(INFO, "Downloading Newest file: ".$headers['filepath']);
    log_(INFO, "File will be placed in: ".$headers['fileoutput']);
  }

  # Mark the file as started
  $statuspost = array(
    "logonkey" => $logonkey, // epf logon key required
    "tokenkey" => $tokenkey, // epf security token required
    "newstatus"=> 'S', // download started
    "fileid"   => $download['fileid'] // fileid from file list required
  );
  $status[$filename] = curl('https://epfws.usps.gov/ws/resources/download/status',$statuspost);
  if ($status[$filename]['meta']['http_code'] === 200) {
    log_(INFO, "Updated File status to 'Started'");
    $logonkey = $status[$filename]['body']['logonkey'];
    $tokenkey = $status[$filename]['body']['tokenkey'];
  } else {
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
  $files[$filename] = curl('https://epfws.usps.gov/ws/resources/download/file',$filepost,$headers);
  if ($files[$filename]['meta']['http_code'] === 200) {
    $logonkey = $files[$filename]['meta']['User-Logonkey'];
    $tokenkey = $files[$filename]['meta']['User-Tokenkey'];
  } else {
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
  $status[$filename] = curl('https://epfws.usps.gov/ws/resources/download/status',$statuspost);
  if ($status[$filename]['meta']['http_code'] === 200) {
    log_(INFO, "Updated File status to 'Completed'");

    $logonkey = $status[$filename]['body']['logonkey'];
    $tokenkey = $status[$filename]['body']['tokenkey'];
  } else {
    log_(WARN, "Key Expired, Logging in again");
    $login = login();
    $logonkey = $login['logonkey'];
    $tokenkey = $login['tokenkey'];
  }
  log_(INFO, "Completed FILE Code:".$value['code'].", ID:".$value['id']);

}

?>
