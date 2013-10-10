#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <zip4.h>
#include <time.h>
#include <sys/time.h>

#include "AmsNativeDao.h"

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv * env, jobject jThis, jobject jAmsSettings)
{
    /* Initialize the config param struct */
    Z4OPEN_PARM openparm;
    memset(&openparm, 0, sizeof(openparm));

    /* Retrieve the paths to the data components stored in the java AmsSettings object. */
    jclass AmsSettingsCls = (*env)->GetObjectClass(env, jAmsSettings);
    jstring systemPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getSystemPath");
    jstring addr1Path = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getAddress1Path");
    jstring addrIndexPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getAddrIndexPath");
    jstring cityStatePath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getCityStatePath");
    jstring crossRefPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getCrossRefPath");
    jstring elotPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getElotPath");
    jstring elotIndexPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getElotIndexPath");
    jstring lacsLinkPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getLacslinkPath");
    jstring dpvPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getDpvPath");
    jstring fnsPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getFnsPath");
    jstring suitelinkPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getSuitelinkPath");
    jstring abrstPath = getStringFromMethod(env, AmsSettingsCls, jAmsSettings, "getAbrstPath");

    /* Read the flags set in jAmsSettings that indicate which services to enable/disable */
    jboolean elotEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isElotEnabled");
    jboolean ewsEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isEwsEnabled");
    jboolean dpvEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isDpvEnabled");
    jboolean lacslinkEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isLacslinkEnabled");
    jboolean suitelinkEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isSuitelinkEnabled");
    jboolean abrstEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isAbrstEnabled");
    jboolean systemEnabled = getBooleanFromMethod(env, AmsSettingsCls, jAmsSettings, "isSystemEnabled");

    /* Construct the openparm structure to pass to the z4opencfg method. */
    openparm.config.system = getC_String(env, systemPath);
    openparm.config.address1 = getC_String(env, addr1Path);
    openparm.config.addrindex = getC_String(env, addrIndexPath);
    openparm.config.citystate = getC_String(env, cityStatePath);
    openparm.config.crossref = getC_String(env, crossRefPath);
    openparm.config.elot = getC_String(env, elotPath);
    openparm.config.elotindex = getC_String(env, elotIndexPath);
    openparm.config.llkpath = getC_String(env, lacsLinkPath);
    openparm.config.dpvpath = getC_String(env, dpvPath);
    openparm.config.fnsnpath = getC_String(env, fnsPath);
    openparm.config.stelnkpath = getC_String(env, suitelinkPath);
    openparm.config.abrstpath = getC_String(env, abrstPath);
    openparm.elotflag = (elotEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.ewsflag = (ewsEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.dpvflag = (dpvEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.llkflag = (lacslinkEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.stelnkflag = (suitelinkEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.abrstflag = (abrstEnabled == JNI_TRUE) ? 'Y' : 'N';
    openparm.systemflag = (systemEnabled == JNI_TRUE) ? 'Y' : 'N';

    /* Open the USPS AMS using the config. */
    int retCode = z4opencfg(&openparm);
    if (retCode == 0) {
        return JNI_TRUE;
    }
    fprintf(stderr, "Failed to open USPS Address Matching System. Error Code %d\n", retCode);
    return JNI_FALSE;
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    closeAmsLibrary
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_closeAmsLibrary
  (JNIEnv * env, jobject jThis)
{
    /* Close The USPS Address Matching System */
    int ret = z4close();
    if (ret == 0) {
        return JNI_TRUE;
    }
    printf("Failed to close the Address Matching System. Return code: %d\n", ret);
    return JNI_FALSE;
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    addressInquiry
 * Signature: (Lgov/nysenate/ams/model/Address;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_addressInquiry
  (JNIEnv * env, jobject jThis, jobject jAddress)
{
    jclass thisCls = (*env)->GetObjectClass(env, jThis);
    jclass AddressCls = (*env)->FindClass(env, "gov/nysenate/ams/model/Address");
    jclass ParsedAddressCls = (*env)->FindClass(env, "gov/nysenate/ams/model/ParsedAddress");
    jclass USPSAddressCls = (*env)->FindClass(env, "gov/nysenate/ams/model/USPSAddress");
    jclass ReturnCodeCls = (*env)->FindClass(env, "gov/nysenate/ams/model/RecordType");
    jclass AddressInquiryResultCls = (*env)->FindClass(env, "gov/nysenate/ams/model/AddressInquiryResult");

    ZIP4_PARM parm;
    memset(&parm, 0, sizeof(ZIP4_PARM));

    /* Retrieve fields from input address. */
    jstring firmName, addr1, addr2, city, state, zip5, zip4;
    firmName = getStringFromMethod(env, AddressCls, jAddress, "getFirmName");
    addr1 = getStringFromMethod(env, AddressCls, jAddress, "getAddr1");
    addr2 = getStringFromMethod(env, AddressCls, jAddress, "getAddr2");
    city = getStringFromMethod(env, AddressCls, jAddress, "getCity");
    state = getStringFromMethod(env, AddressCls, jAddress, "getState");
    zip5 = getStringFromMethod(env, AddressCls, jAddress, "getZip5");

    /* Convert jstrings to c style strings */
    char * cFirmName, * cAddr1, * cAddr2, * cCity, * cState, * cZip5;
    cFirmName = getC_String(env, firmName);
    cAddr1 = getC_String(env, addr1);
    cAddr2 = getC_String(env, addr2);
    cCity = getC_String(env, city);
    cState = getC_String(env, state);
    cZip5 = getC_String(env, zip5);

    /* Construct the input address struct to pass into the inquiry method. */
    strcpy(parm.iadl1, cAddr1);
    strcpy(parm.iadl2, cFirmName);
    strcpy(parm.iadl3, cAddr2);
    strcpy(parm.iprurb, "");
    strcpy(parm.ictyi, cCity);
    strcpy(parm.istai, cState);
    strcpy(parm.izipc, cZip5);

    /* Release the strings */
    releaseC_String(env, cFirmName, firmName);
    releaseC_String(env, cAddr1, addr1);
    releaseC_String(env, cAddr2, addr2);
    releaseC_String(env, cCity, city);
    releaseC_String(env, cState, state);
    releaseC_String(env, cZip5, zip5);

    /* Call the AMS address inquiry and standardization methods */
    z4adrinq(&parm);
    z4adrstd(&parm, 1);

    /* Get the validated address data */
    firmName = (*env)->NewStringUTF(env, parm.dadl2);
    addr1 = (*env)->NewStringUTF(env, parm.dadl1);
    addr2 = (*env)->NewStringUTF(env, parm.dadl3);
    city = (*env)->NewStringUTF(env, parm.dctya);
    state = (*env)->NewStringUTF(env, parm.dstaa);
    zip5 = (*env)->NewStringUTF(env, parm.zipc);
    zip4 = (*env)->NewStringUTF(env, parm.addon);

    /* Get the parsed input data */
    jstring primaryNum, secondaryNum, rightSecondaryNum, ruralRouteNum, secondaryNumUnit, rightSecondaryNumUnit,
            leftPre, rightPre, firstSuffix, secondSuffix, leftPost, rightPost, primaryName;

    primaryNum = (*env)->NewStringUTF(env, parm.ppnum);
    secondaryNum = (*env)->NewStringUTF(env, parm.psnum);
    rightSecondaryNum = (*env)->NewStringUTF(env, parm.psnum2);
    ruralRouteNum = (*env)->NewStringUTF(env, parm.prote);
    secondaryNumUnit = (*env)->NewStringUTF(env, parm.punit);
    rightSecondaryNumUnit = (*env)->NewStringUTF(env, parm.punit2);
    leftPre = (*env)->NewStringUTF(env, parm.ppre1);
    rightPre = (*env)->NewStringUTF(env, parm.ppre2);
    firstSuffix = (*env)->NewStringUTF(env, parm.psuf1);
    secondSuffix = (*env)->NewStringUTF(env, parm.psuf2);
    leftPost = (*env)->NewStringUTF(env, parm.ppst1);
    rightPost = (*env)->NewStringUTF(env, parm.ppst2);
    primaryName = (*env)->NewStringUTF(env, parm.ppnam);

    /* Create the Address object */
    jmethodID constructor = (*env)->GetMethodID(env, AddressCls, "<init>", "(" REP7(STRING_TYPE) ")V");
    jobject addressObj = (*env)->NewObject(env, AddressCls, constructor, firmName, addr1, addr2, city, state, zip5, zip4);

    /* Create the ParsedAddress object */
    constructor = (*env)->GetMethodID(env, ParsedAddressCls, "<init>", "(" REP11(STRING_TYPE) ")V");
    jobject parsedAddressObj = (*env)->NewObject(env, ParsedAddressCls, constructor,
        primaryNum, secondaryNum, ruralRouteNum, secondaryNumUnit, leftPre, rightPre, firstSuffix,
        secondSuffix, leftPost, rightPost, primaryName);

    /* Create the USPSAddress object */
    constructor = (*env)->GetMethodID(env, USPSAddressCls, "<init>", "(" ADDRESS_TYPE PARSED_ADDRESS_TYPE REP8(STRING_TYPE) CHAR_TYPE ")V");
    jobject uspsAddressObj = (*env)->NewObject(env, USPSAddressCls, constructor, )

    /*printf("Response Code: %d\n", parm.retcc);
    printf("Footnote: %s\n", parm.footnotes);
    printf("Num Responses: %d\n", parm.respn);
    printf("Output Line 1: %s\n", parm.dadl1);
    printf("Output Firm Name: %s\n", parm.dadl2);
    printf("Output Line 2: %s\n", parm.dadl3);
    printf("PR Code: %s\n", parm.dprurb);
    printf("Output City: %s\n", parm.dctya);
    printf("Output State: %s\n", parm.dstaa);
    printf("Output CSZ: %s\n", parm.dlast);
    printf("Main PO City: %s\n", parm.dctys);
    printf("Main PO State: %s\n", parm.dstas);
    printf("Abbrev Output City: %s\n", parm.abcty);
    printf("5 Digit Zip: %s\n", parm.zipc);
    printf("4 Digit Addon: %s\n", parm.addon);
    printf("4 Digit Carrier: %s\n", parm.cris);
    printf("3 Digit County Code: %s\n", parm.county);
    printf("Delivery Point: %s\n", parm.dpbc);
    printf("Matched Primary Num: %s\n", parm.mpnum);
    printf("Matched Secondary Num: %s\n", parm.msnum);
    printf("ELOT Num: %s\n", parm.elot_num);
    printf("ELOT Code: %c\n", parm.elot_code);
    printf("LACS Return Code: %s\n", parm.llk_rc);
    printf("LACS Indicator: %c\n", parm.llk_ind);
    printf("Address Database Key: %s\n", parm.adrkey);

    printf("Parsed Input -----------------\n");
    printf("Primary Number: %s\n", parm.ppnum);
    printf("Secondary Number: %s\n", parm.psnum);
    printf("Second or Right Secondary Number: %s\n", parm.psnum2);
    printf("Rural Route Number: %s\n", parm.prote);
    printf("Secondary Number Unit: %s\n", parm.punit);
    printf("Secondary or Right Secondary Number Unit: %s\n", parm.punit2);
    printf("First or Left Pre: %s\n", parm.ppre1);
    printf("Second or Right Pre: %s\n", parm.ppre2);
    printf("First Suffix: %s\n", parm.psuf1);
    printf("Second Suffix: %s\n", parm.psuf2);
    printf("First or Left Post: %s\n", parm.ppst1);
    printf("Second or Right Post: %s\n", parm.ppst2);
    printf("Primary Name: %s\n", parm.ppnam);
    printf("Matched Primary Number: %s\n", parm.mpnum);
    printf("Matched Secondary Number: %s\n", parm.msnum);
    printf("PMB Unit Designator: %s\n", parm.pmb);
    printf("PMB Number: %s\n", parm.pmbnum);

    int stackSize = sizeof(parm.stack) / sizeof(ADDR_REC);
    printf("Stack size: %d\n", stackSize);
    int i;
    for (i = 0; i < parm.respn && i < stackSize; i++) {
        printf("Record %d: ---------------\n", i);
        printf("Zip Code: %s\n", parm.stack[i].zip_code);
        printf("Update Key Num: %s\n", parm.stack[i].update_key);
        printf("Action Code: %c\n", parm.stack[i].action_code);
        printf("Record Type: %c\n", parm.stack[i].rec_type);
        printf("Pre Dir: %s\n", parm.stack[i].pre_dir);
        printf("Street Name: %s\n", parm.stack[i].str_name);
        printf("Suffix: %s\n", parm.stack[i].suffix);
        printf("Post Dir: %s\n", parm.stack[i].post_dir);
        printf("Primary Low: %s\n", parm.stack[i].prim_low);
        printf("Primary High: %s\n", parm.stack[i].prim_high);
        printf("Primary E/O: %c\n", parm.stack[i].prim_code);
        printf("Bldg Firm Name: %s\n", parm.stack[i].sec_name);
        printf("Unit: %s\n", parm.stack[i].unit);
        printf("Sec Low: %s\n", parm.stack[i].sec_low);
        printf("Sec High: %s\n", parm.stack[i].sec_high);
        printf("Sec Code: %c\n", parm.stack[i].sec_code);
        printf("Addon Low: %s\n", parm.stack[i].addon_low);
        printf("Addon High: %s\n", parm.stack[i].addon_high);
        printf("Base Alternate Code: %c\n", parm.stack[i].base_alt_code);
        printf("Lacs Status: %c\n", parm.stack[i].lacs_status);
        printf("Finance Code: %s\n", parm.stack[i].finance);
        printf("State Abbrev: %s\n", parm.stack[i].state_abbrev);
        printf("County Num: %s\n", parm.stack[i].county_no);
        printf("Congress Dist: %s\n", parm.stack[i].congress_dist);
        printf("Municipality: %s\n", parm.stack[i].municipality);
        printf("Urbanization: %s\n", parm.stack[i].urbanization);
        printf("Last line: %s\n", parm.stack[i].last_line);
    }
    printf("[C] Got to the end of this function!\n");     */

    return addressObj;
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    cityStateLookup
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/CityStateResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_cityStateLookup
  (JNIEnv * env, jobject jThis, jstring jZip);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    zip9Inquiry
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_zip9Inquiry
  (JNIEnv * env, jobject jThis, jstring jZip9)
{

}

jobject getObjectFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName, const char * returnType)
{
    jmethodID methodID = (*env)->GetMethodID(env, cls, methodName, returnType);
    return (*env)->CallObjectMethod(env, instance, methodID);
}

jstring getStringFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName)
{
    jmethodID methodID = (*env)->GetMethodID(env, cls, methodName, NO_ARGS STRING_TYPE);
    return (jstring)(*env)->CallObjectMethod(env, instance, methodID);
}

jboolean getBooleanFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName)
{
    jmethodID methodID = (*env)->GetMethodID(env, cls, methodName, NO_ARGS BOOLEAN_TYPE);
    return (*env)->CallBooleanMethod(env, instance, methodID);
}

jint getIntFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName)
{
    jmethodID methodID = (*env)->GetMethodID(env, cls, methodName, NO_ARGS INT_TYPE);
    return (*env)->CallIntMethod(env, instance, methodID);
}

jchar getCharFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName)
{
    jmethodID methodID = (*env)->GetMethodID(env, cls, methodName, NO_ARGS CHAR_TYPE);
    return (*env)->CallCharMethod(env, instance, methodID);
}

/**
*
*/
char * getC_String(JNIEnv * env, const jstring javaString)
{
    return (*env)->GetStringUTFChars(env, javaString, 0);
}

/**
*
*/
void releaseC_String(JNIEnv * env, const char * cString, const jstring javaString)
{
    (*env)->ReleaseStringUTFChars(env, javaString, cString);
}

/**
*
*/
void printJString(JNIEnv * env, const jstring string)
{
    const char * nativeString = getC_String(env, string);
    printf("%s\n", nativeString);
    releaseC_String(env, nativeString, string);
}