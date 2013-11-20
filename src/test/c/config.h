/*
** Project: USPS-AMS-WebService
** Author: Ken Zalewski
** Organization: New York State Senate
*/

#ifndef CONFIG_H
#define CONFIG_H

#include <zip4.h>	/* Z4OPEN_PARM */

#define DATA_PATH   "/data/usps_ams"
#define AMS_PATH    DATA_PATH "/ams_comm"
#define DPV_PATH    DATA_PATH "/ams_dpv"
#define ELOT_PATH   DATA_PATH "/ams_elot"
#define LACS_PATH   DATA_PATH "/lacslink"
#define SUITE_PATH  DATA_PATH "/suitelink"
#define SYSTEM_PATH "./"

void set_default_config(Z4OPEN_PARM* p_openparm);
int set_config_from_file(char* filename, Z4OPEN_PARM* p_openparm);
void set_config_param(Z4OPEN_PARM* p_openparm, char* name, char* val);
void display_config(Z4OPEN_PARM* p_openparm);


#endif
