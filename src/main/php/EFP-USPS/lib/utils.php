<?php
date_default_timezone_set('America/New_York');
const FATAL = 0;
const ERROR = 1;
const WARN  = 2;
const INFO  = 3;
const DEBUG = 4;

function load_config()
{
  $config_file =  realpath(dirname(__FILE__).'/../efp_config.ini');
  if (in_array('BBSTATS_CONFIG', $_ENV)) {
    $config_file = $_ENV['config_file'];
  }

  $config = parse_ini_file($config_file, true);
  if (!$config) {
    log_(ERROR, "Configuration file not found at '$config_file'.");
    return FALSE;
  }
  // var_dump($config);

  // foreach(array('database','input') as $section) {
  //   if (!array_key_exists($section, $config)) {
  //     log_(500,"Invalid config file. '$section' section required");
  //     return FALSE;
  //   }
  // }

  return $config;
}

function convert($size)
{
  $unit=array('b','kb','mb','gb','tb','pb');
  return @round($size/pow(1024,($i=floor(log($size,1024)))),2).' '.$unit[$i];
}

function curl($url,$post=null,$headers=null){

  $ch = curl_init($url);
  curl_setopt($ch, CURLOPT_FILETIME, true);
  curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
  if (isset($post)){
    $json = 'obj='.json_encode($post);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-type: application/x-www-form-urlencoded"));
  }
  if (isset($headers)){
    ob_start();
    curl_setopt($ch, CURLOPT_HTTPHEADER,
      array(
        "Content-type: application/x-www-form-urlencoded",
        "Akamai-File-Request: ".$headers['filepath'],
        "logonkey: ".$post['logonkey'],
        "tokenkey: ".$post['tokenkey'],
        "fileid:".$post['fileid']
      )
    );
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_PROGRESSFUNCTION, 'progress');
    curl_setopt($ch, CURLOPT_NOPROGRESS, false);
    $fp = fopen ($headers['fileoutput'], 'w+'); //temp file
    curl_setopt($ch, CURLOPT_FILE, $fp); // write curl response to file
    curl_exec($ch);
  }
  $body = json_decode(curl_exec($ch), true);
  $meta = curl_getinfo($ch);
  return array('body'=>$body,'meta'=>$meta);
}
$n = 0;
function progress($resource,$download_size, $downloaded, $upload_size, $uploaded)
{
    global $n;
    if($download_size > 0 && $downloaded > 0){
      $perc = round(($downloaded / $download_size  * 100));
      if($n !== $perc && $perc <= 100  && $perc >= 0 && $downloaded >= 0 && $download_size >= 0 ){
        log_(DEBUG, "Downloaded ".$perc."%\t(".convert($downloaded)." / ".convert($download_size).")");
        $n = $perc;
      }
    }
    ob_flush();
    flush();
}


function log_($log_level, $message)
{
  global $g_log_level, $g_log_file;

  //Get the integer level for each and ignore out of scope log messages
  if ($g_log_level < $log_level) {
    return;
  }else{
    echo "$message\n";
    switch ($log_level) {
      case FATAL: $debug_level = 'FATAL'; break;
      case ERROR: $debug_level = 'ERROR'; break;
      case WARN: $debug_level = 'WARN'; break;
      case INFO: $debug_level = 'INFO'; break;
      case DEBUG: $debug_level = 'DEBUG'; break;
      default: $debug_level = $log_level; break;
    }
    $date = date('Y-m-d H:i:s');

    //Log to a debug file, or to Apache if debug file was not opened.
    if ($g_log_file) {
      fwrite($g_log_file, "$date\t[$debug_level]\t$message\n");
    }
    else {
      error_log("[statserver]\t$date\t[$debug_level]\t$message\n");
    }
  }
}

function get_log_level($config)
{
  $debug_level = DEBUG;  // default debug level is DEBUG
  if (isset($config['debug_level'])) {
    $debug_level_val = $config['debug_level'];
    if (is_numeric($debug_level_val)) {
      $debug_level = $debug_level_val;
    }
    else {
      error_log("[statserver] $debug_level_val: Invalid debug level");
    }
  }
  return $debug_level;
}

function get_log_file($config)
{
  $log_file = false;

  if (isset($config['log_file'])) {
    $filepath = $config['log_file'];
    $log_file = fopen($filepath, 'a');
    if (!$log_file) {
      error_log("[statserver] $filepath: Unable to open for writing");
    }
  }
  return $log_file;
}
