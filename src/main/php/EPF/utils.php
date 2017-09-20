<?php

const FATAL = 0;
const ERROR = 1;
const WARN  = 2;
const INFO  = 3;
const DEBUG = 4;


function load_config()
{
  $config_file =  realpath(dirname(__FILE__).'/epf_config.ini');
  if (in_array('BBSTATS_CONFIG', $_ENV)) {
    $config_file = $_ENV['config_file'];
  }

  $config = parse_ini_file($config_file, true);
  if (!$config) {
    echo "$config_file: Configuration file not found\n";
    return false;
  }

  // Check for required parameters.
  foreach (array('api_url', 'username', 'password', 'output_dir') as $param) {
    if (!array_key_exists($param, $config)) {
      echo "$config_file: Invalid config file; [$param] param is required\n";
      return false;
    }
  }
  return $config;
} // load_config()


function convert($size)
{
  $unit = array('b','kb','mb','gb','tb','pb');
  return @round($size/pow(1024,($i=floor(log($size,1024)))),2).' '.$unit[$i];
} // convert()


function send_request($url_suffix, $params = null, $outfile = null)
{
  global $g_api_url;
  global $cb_logonkey, $cb_tokenkey;

  $url = "$g_api_url/$url_suffix";
  $ch = curl_init($url);
  curl_setopt($ch, CURLOPT_FILETIME, true);
  curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

  if (isset($params)) {
    $json = 'obj='.json_encode($params);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));

    if (isset($outfile)) {
      $cb_logonkey = $cb_tokenkey = null;
      curl_setopt($ch, CURLOPT_RETURNTRANSFER, false);
      curl_setopt($ch, CURLOPT_HEADERFUNCTION, 'cb_curl_header');
      curl_setopt($ch, CURLOPT_PROGRESSFUNCTION, 'cb_curl_progress');
      curl_setopt($ch, CURLOPT_NOPROGRESS, false);
      // Open a file and write the Curl response to that file.
      $fp = fopen($outfile, 'w+');
      curl_setopt($ch, CURLOPT_FILE, $fp);
      curl_exec($ch);
      fclose($fp);
      $result = array('response' => 'success',
                      'logonkey' => $cb_logonkey,
                      'tokenkey' => $cb_tokenkey);
    }
    else {
      $result = json_decode(curl_exec($ch), true);
    }
  }
  else {
    $result = json_decode(curl_exec($ch), true);
  }

  $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  $result['http_code'] = $http_code;
  curl_close($ch);
  return $result;
} // send_request()


function cb_curl_header($resource, $data)
{
  global $cb_logonkey, $cb_tokenkey;

  if (strncmp($data, 'User-Logonkey:', 14) === 0) {
    $cb_logonkey = trim(substr($data, 14));
  }
  else if (strncmp($data, 'User-Tokenkey:', 14) === 0) {
    $cb_tokenkey = trim(substr($data, 14));
  }
  return strlen($data);
} // cb_curl_header()


function cb_curl_progress($resource, $dl_size, $dl, $ul_size, $ul)
{
  static $n = 0;

  if ($dl_size > 0 && $dl > 0) {
    $perc = round($dl / $dl_size * 100);
    if ($n !== $perc) {
      log_(INFO, "Downloaded $perc%\t(".convert($dl)." / ".convert($dl_size).")");
      $n = $perc;
    }
  }

  flush();
  return 0;
} // cb_curl_progress()


function log_($log_level, $message)
{
  global $g_log_level, $g_log_file;

  static $debug_levels = array(
    FATAL => 'FATAL',
    ERROR => 'ERROR',
    WARN => 'WARN',
    INFO => 'INFO',
    DEBUG => 'DEBUG'
  );

  //Get the integer level for each and ignore out of scope log messages
  if ($g_log_level >= $log_level) {
    $lvlstr = 'UNKNOWN';
    if (isset($debug_levels[$log_level])) {
      $lvlstr = $debug_levels[$log_level];
    }
    $datestr = date('Y-m-d:H:i:s');
    if ($g_log_file) {
      fprintf($g_log_file, "%s [%s] %s\n", $datestr, $lvlstr, $message);
    }
    else {
      echo "$datestr [$lvlstr] $message\n";
    }
  }
} // log_()


function get_log_file($filepath)
{
  $log_file = fopen($filepath, 'a');
  if (!$log_file) {
    echo "[usps-epf] $filepath: Unable to open for writing\n";
  }
  return $log_file;
} // get_log_file()
