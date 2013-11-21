/*
** Project: USPS-AMS-WebService
** Author: Ken Zalewski
** Organization: New York State Senate
*/

#include <stdio.h>	/* fopen() */
#include <strings.h>	/* bzero() */
#include <string.h>	/* strchr(), strcmp() */
#include <ctype.h>	/* isspace(), toupper() */
#include <zip4.h>	/* Z4OPEN_PARM */
#include "config.h"	/* DATA_PATH, etc */

#define MAX_LINE_SZ	256


void
set_default_config(Z4OPEN_PARM* p_openparm)
{
  Z4OPEN_PARM* op = p_openparm;
  CONFIG_PARM* cp = &(op->config);

  bzero(op, sizeof(Z4OPEN_PARM));
  cp->address1   = AMS_PATH;
  cp->addrindex  = AMS_PATH;
  cp->citystate  = AMS_PATH;
  cp->crossref   = AMS_PATH;
  cp->fnsnpath   = AMS_PATH;
  cp->elot       = ELOT_PATH;
  cp->elotindex  = ELOT_PATH;
  cp->llkpath    = LACS_PATH;
  cp->dpvpath    = DPV_PATH;
  cp->stelnkpath = SUITE_PATH;
  cp->abrstpath  = AMS_PATH;
  cp->system     = SYSTEM_PATH;
  op->elotflag = 'Y';
  op->llkflag = 'Y';
  op->dpvflag = 'Y';
  op->ewsflag = 'N';
  op->systemflag = 'N';
  op->stelnkflag = 'Y';
  op->abrstflag = 'Y';
} /* set_default_config() */


int
set_config_from_file(char* p_filename, Z4OPEN_PARM* p_openparm)
{
  FILE* fp;
  char line[MAX_LINE_SZ];

  set_default_config(p_openparm);

  fp = fopen(p_filename, "r");
  if (fp == NULL) {
    fprintf(stderr, "Error: %s: File not found\n", p_filename);
    return -1;
  }

  while (fgets(line, MAX_LINE_SZ, fp) != NULL) {
    char* nlptr = strrchr(line, '\n');
    char* eqptr = strchr(line, '=');
    char* nameptr = line;
    char* valptr;

    if (nlptr) {
      *nlptr = '\0';
    }
    if (eqptr == NULL) {
      fprintf(stderr, "Warning: Ignoring invalid config line [%s]\n", line);
      continue;
    }

    *eqptr = '\0';
    valptr = eqptr + 1;

    while (isspace(*nameptr)) {
      nameptr++;
    }
    while (isspace(*(--eqptr))) {
      *eqptr = '\0';
    }
    while (isspace(*valptr)) {
      valptr++;
    }
    set_config_param(p_openparm, nameptr, valptr);
  }
  fclose(fp);
  return 0;
} /* set_config_from_file() */


void
set_config_param(Z4OPEN_PARM* p_openparm, char* name, char* val)
{
  Z4OPEN_PARM* op = p_openparm;
  CONFIG_PARM* cp = &(op->config);

  if (strcmp(name, "ADDRESS1") == 0) {
    cp->address1 = strdup(val);
  }
  else if (strcmp(name, "ADDRINDEX") == 0) {
    cp->addrindex = strdup(val);
  }
  else if (strcmp(name, "CITYSTATE") == 0) {
    cp->citystate = strdup(val);
  }
  else if (strcmp(name, "CROSSREF") == 0) {
    cp->crossref = strdup(val);
  }
  else if (strcmp(name, "SYSTEM") == 0) {
    cp->system = strdup(val);
  }
  else if (strcmp(name, "ELOT") == 0) {
    cp->elot = strdup(val);
  }
  else if (strcmp(name, "ELOTINDEX") == 0) {
    cp->elotindex = strdup(val);
  }
  else if (strcmp(name, "LLKPATH") == 0) {
    cp->llkpath = strdup(val);
  }
  else if (strcmp(name, "DPVPATH") == 0) {
    cp->dpvpath = strdup(val);
  }
  else if (strcmp(name, "FNSNPATH") == 0) {
    cp->fnsnpath = strdup(val);
  }
  else if (strcmp(name, "SLNKPATH") == 0) {
    cp->stelnkpath = strdup(val);
  }
  else if (strcmp(name, "ABRSTPATH") == 0) {
    cp->abrstpath = strdup(val);
  }
  else if (strcmp(name, "ELOTFLAG") == 0) {
    op->elotflag = toupper(*val);
  }
  else if (strcmp(name, "EWSFLAG") == 0) {
    op->ewsflag = toupper(*val);
  }
  else if (strcmp(name, "DPVFLAG") == 0) {
    op->dpvflag = toupper(*val);
  }
  else if (strcmp(name, "LLKFLAG") == 0) {
    op->llkflag = toupper(*val);
  }
  else if (strcmp(name, "SLNKFLAG") == 0) {
    op->stelnkflag = toupper(*val);
  }
  else if (strcmp(name, "ABRSTFLAG") == 0) {
    op->abrstflag = toupper(*val);
  }
  else if (strcmp(name, "SYSTEMFLAG") == 0) {
    op->systemflag = toupper(*val);
  }

  return;
} /* set_config_param() */


void
display_config(Z4OPEN_PARM* p_openparm)
{
  Z4OPEN_PARM* op = p_openparm;
  CONFIG_PARM* cp = &(op->config);
  fprintf(stderr, "Z4OPEN_PARM printout:\n"
                  "ADDRESS1=%s\n"
                  "ADDRINDEX=%s\n"
                  "CITYSTATE=%s\n"
                  "CROSSREF=%s\n"
                  "SYSTEM=%s\n"
                  "ELOT=%s\n"
                  "ELOTINDEX=%s\n"
                  "LLKPATH=%s\n"
                  "DPVPATH=%s\n"
                  "FNSNPATH=%s\n"
                  "SLNKPATH=%s\n"
                  "ABRSTPATH=%s\n"
                  "ELOTFLAG=%c\n"
                  "EWSFLAG=%c\n"
                  "DPVFLAG=%c\n"
                  "LLKFLAG=%c\n"
                  "SLNKFLAG=%c\n"
                  "ABRSTFLAG=%c\n"
                  "SYSTEMFLAG=%c\n",
                  cp->address1, cp->addrindex, cp->citystate, cp->crossref,
                  cp->system, cp->elot, cp->elotindex, cp->llkpath,
                  cp->dpvpath, cp->fnsnpath, cp->stelnkpath, cp->abrstpath,
                  op->elotflag, op->ewsflag, op->dpvflag, op->llkflag,
                  op->stelnkflag, op->abrstflag, op->systemflag);
  return;
} /* display_config() */
