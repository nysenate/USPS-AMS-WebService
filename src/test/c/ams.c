/*
** Project: USPS-AMS-WebService
** Author: Ken Zalewski
** Organization: New York State Senate
*/

#include <stdio.h>	/* printf() */
#include <string.h>
#include <stdlib.h>	/* exit() */
#include <zip4.h>	/* z4opencfg() */
#include <time.h> /* timing */
#include <sys/time.h>
#include "config.h"	/* set_config_from_file() */
#include "tcp.h"	/* do_main_loop() */

#define DEFAULT_CONFIG_FNAME "ams.cfg"

static void usage(char* prog, char* arg);
static void print_elapsed_time(struct timeval start, struct timeval end);

int
main(int argc, char** argv)
{
  Z4OPEN_PARM openparm;
  Z4OPEN_PARM* op = &openparm;
  int rc, i;
  char release_date[9];
  char ams_version[32];
  char* in_street = NULL;
  char* in_csz = NULL;
  char* cfg_fname = DEFAULT_CONFIG_FNAME;

  struct timeval start, end;
   
  gettimeofday(&start, NULL);

  rc = set_config_from_file(cfg_fname, op);
  if (rc) {
    fprintf(stderr, "Error: set_config_from_file() failed\n");
    return 1;
  }

  //display_config(op);

  rc = z4opencfg(op);
  char* msg;

  switch (op->status) {
    case Z4_FNAME: msg = "specified filename"; break;
    case Z4_CONFIG: msg = "provided config parameters"; break;
    case Z4_SEARCH: msg = "default config file z4config.dat"; break;
    default:
      fprintf(stderr, "Error: Invalid z4opencfg status: %d\n", op->status);
      z4close();
      return 1;
  }

  printf("Opened AMS API using %s\n", msg);

  if (rc) {
    printf("Retrieving error [rc=%d]\n", rc);
    Z4_ERROR z4error;
    z4geterror(&z4error);
    printf("iErrorCode=%d, msg=[%s]\n", z4error.iErrorCode, z4error.strErrorMessage);
    z4close();
    return 1;
  }

  gettimeofday(&end, NULL);
  print_elapsed_time(start, end);
  //z4date(release_date);
  //z4ver(ams_version);
  //printf("Release date: %s\n", release_date);
  //printf("Days to expiration of data license: %d\n", z4GetDataExpireDays());
  //printf("Days to expiration of code license: %d\n", z4GetCodeExpireDays());
  //printf("Code version: %s\n", ams_version);

  ZIP4_PARM parm;
  

  while (1) {
    char street[51];
    char cty[51];
    int i;
    printf("Enter Street: ");
    fgets(street, 51, stdin);
    printf("Enter City: ");
    fgets(cty, 51, stdin);
    
    gettimeofday(&start, NULL);

	bzero(&parm, sizeof(ZIP4_PARM));
  	strcpy(parm.iadl1, street);
  	strcpy(parm.ictyi, cty);

  	z4adrinq(&parm);
  	z4adrstd(&parm, 1);

    printf("Performed USPS Lookup.\n");

  	if (parm.retcc == Z4_SINGLE || parm.retcc == Z4_DEFAULT) {
	    printf("ZIP4 RESPONSE:\n"
	           "County: %s\n"
	           "Addr1: %s\n"
	           "Addr2: %s\n"
	           "Addr3: %s\n"
	           "C/S/Z: %s\n"
	           "City: %s\n"
	           "City (abbr): %s\n"
	           "State: %s\n"
	           "ZIP5: %s\n"
	           "ZIP4: %s\n"
	           "Address Record\n\n"
	           "Street Name: %s\n"  
	           "Suffix: %s\n"
	           "Prim Low: %s\n"
	           "Prim High: %s\n"
	           "Prim Code: %d\n"
	           "Sec Name: %s\n"
	           "Footnotes: %s\n",
	           parm.county, parm.dadl1, parm.dadl2, parm.dadl3, parm.dlast,
	           parm.dctya, parm.abcty, parm.dstaa, parm.zipc, parm.addon, 
	           parm.stack[0].str_name, 
	           parm.stack[0].suffix,
	           parm.stack[0].prim_low,
	           parm.stack[0].prim_high,
	           parm.stack[0].prim_code,
	           parm.stack[0].sec_name,
	           parm.footnotes);
	     
    }
      
      gettimeofday(&end, NULL);
      print_elapsed_time(start, end);      
  }  

  
  z4close();
  return 0;
}

static void 
print_elapsed_time(struct timeval start, struct timeval end) 
{
	long mtime, secs, usecs;
	secs  = end.tv_sec  - start.tv_sec;
	usecs = end.tv_usec - start.tv_usec;
    mtime = ((secs) * 1000 + usecs/1000.0) + 0.5;
    printf("Elapsed time: %ld millisecs\n", mtime);
}


static void
usage(char* prog, char* arg)
{
} /* usage() */
