/*
** Project: USPS-AMS-WebService
** Author: Ken Zalewski
** Organization: New York State Senate
*/

#include <stdio.h>	/* printf() */
#include <string.h>
#include <stdlib.h>	/* exit() */
#include <zip4.h>	/* z4opencfg() */
#include "config.h"	/* set_config_from_file() */
#include "tcp.h"	/* do_main_loop() */

#define DEFAULT_CONFIG_FNAME "ams.cfg"

static void usage(char* prog, char* arg);


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

  for (i = 1; i < argc; i++) {
    if (*argv[i] != '-' || strlen(argv[i]) < 2) {
      usage(argv[0], argv[i]);
    }
    switch (argv[i][1]) {
      case 's':
        if (i + 1 < argc) in_street = argv[++i];
        break;
      case 'c':
        if (i + 1 < argc) in_csz = argv[++i];
        break;
      case 'f':
        if (i + 1 < argc) cfg_fname = argv[++i];
        break;
      default:
        usage(argv[0], argv[i]);
    }
  }

  if (!in_street || !in_csz) {
    usage(argv[0], NULL);
  }

  // Get the library version. The AMS system does not have to be open for this.
  z4ver(ams_version);
  printf("Using AMS API code version %s\n", ams_version);

  rc = set_config_from_file(cfg_fname, op);
  if (rc) {
    fprintf(stderr, "Error: set_config_from_file() failed\n");
    return 1;
  }
  display_config(op);

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

  if (rc) {
    fprintf(stderr, "Failed to properly open AMS API using %s\n", msg);
    printf("Retrieving error [rc=%d]\n", rc);
    Z4_ERROR z4error;
    z4geterror(&z4error);
    printf("Error details:\n\tiErrorCode=%d, msg=[%s]\n\tiFileCode=%d, filename=[%s]\n\tdiagnostics=[%s]\n", z4error.iErrorCode, z4error.strErrorMessage, z4error.iFileCode, z4error.strFileName, z4error.strDiagnostics);
    z4close();
    return 1;
  }
  else {
    printf("Successfully opened AMS API using %s\n", msg);
  }

  z4date(release_date);
  printf("Release date: %s\n", release_date);
  printf("Days to expiration of data license: %d\n", z4GetDataExpireDays());
  printf("Days to expiration of code license: %d\n", z4GetCodeExpireDays());
  printf("Code version: %s\n", ams_version);

  ZIP4_PARM parm;
  CITY_REC city;

  bzero(&parm, sizeof(ZIP4_PARM));
  strcpy(parm.iadl1, in_street);
  strcpy(parm.ictyi, in_csz);
  z4adrinq(&parm);
  if (parm.retcc == Z4_SINGLE || parm.retcc == Z4_DEFAULT) {
    printf("ZIP4 RESPONSE:\n"
           "Addr1: %s\n"
           "Addr2: %s\n"
           "Addr3: %s\n"
           "C/S/Z: %s\n"
           "City: %s\n"
           "City (abbr): %s\n"
           "State: %s\n"
           "ZIP5: %s\n"
           "ZIP4: %s\n",
           parm.dadl1, parm.dadl2, parm.dadl3, parm.dlast,
           parm.dctya, parm.abcty, parm.dstaa, parm.zipc, parm.addon);
  }

  z4ctyget(&city, parm.zipc);

  printf("CITY RESPONSE:\n"
         "Zip: %s\n"
         "City: %s\n"
         "City (abbr): %s\n"
         "State: %s\n"
         "County#: %s\n"
         "County: %s\n",
         city.zip_code, city.city_name, city.city_abbrev, city.state_abbrev,
         city.county_no, city.county_name);
  z4close();
  return 0;
}


static void
usage(char* prog, char* arg)
{
  if (arg) {
    fprintf(stderr, "%s: %s: Invalid option\n", prog, arg);
  }
  fprintf(stderr, "Usage: %s [-f config-file] [-s street] [-c city/state/zip]\n", prog);
  exit(1);
} /* usage() */
