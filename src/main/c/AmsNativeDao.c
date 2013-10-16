#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <zip4.h>
#include <time.h>
#include <sys/time.h>

#include "AmsNativeDao.h"

/* Cached class ids */
static jclass AmsSettingsCls;
static jclass AddressCls;
static jclass AddressRecordCls;
static jclass ParsedAddressCls;
static jclass USPSAddressCls;
static jclass ReturnCodeCls;
static jclass AddressInquiryResultCls;

/* Cached constructors */
static jmethodID AddressConstr;
static jmethodID ParsedAddressConstr;
static jmethodID AddressRecordConstr;
static jmethodID USPSAddressConstr;
static jmethodID AddressInquiryResultConstr;

/* Cached methods */
static jmethodID Address_getFirmName;
static jmethodID Address_getAddr1;
static jmethodID Address_getAddr2;
static jmethodID Address_getCity;
static jmethodID Address_getState;
static jmethodID Address_getZip5;

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv * env, jobject jThis, jobject jAmsSettings)
{
    /* Cache all method/constructor ids */
    cacheIDs(env);

    /* Initialize the config param struct */
    Z4OPEN_PARM openparm;
    memset(&openparm, 0, sizeof(openparm));

    /* Retrieve the paths to the data components stored in the java AmsSettings object. */
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

    jmethodID constructor;
    jobject inquiryResult;

    ZIP4_PARM parm;
    memset(&parm, 0, sizeof(ZIP4_PARM));

    /* Retrieve fields from input address. */
    jstring firmName, addr1, addr2, city, state, zip5, zip4;
    firmName = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getFirmName);
    addr1 = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getAddr1);
    addr2 = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getAddr2);
    city = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getCity);
    state = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getState);
    zip5 = (jstring)(*env)->CallObjectMethod(env, jAddress, Address_getZip5);

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

    /* Free the c strings */
    releaseC_String(env, cFirmName, firmName);
    releaseC_String(env, cAddr1, addr1);
    releaseC_String(env, cAddr2, addr2);
    releaseC_String(env, cCity, city);
    releaseC_String(env, cState, state);
    releaseC_String(env, cZip5, zip5);

    /* Call the AMS address inquiry and standardization methods */
    int responseCode, statusCode;
    responseCode = z4adrinq(&parm);

    /* Handle successful response */
    if (responseCode == 0) {
        statusCode = parm.retcc;

        /* In the event of multiple matches,standardize on the first result */
        if (statusCode != Z4_SINGLE && statusCode != Z4_DEFAULT && parm.respn > 0) {
            z4adrstd(&parm, 1);
        }

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

        /* Get additional address data */
        jstring poCity, poState, abbrCity, deliveryPoint, carrierRoute, addressKey, footnotes, fipsCounty;
        poCity = (*env)->NewStringUTF(env, parm.dctys);
        poState = (*env)->NewStringUTF(env, parm.dstas);
        abbrCity = (*env)->NewStringUTF(env, parm.abcty);
        deliveryPoint = (*env)->NewStringUTF(env, parm.dpbc);
        carrierRoute = (*env)->NewStringUTF(env, parm.cris);
        addressKey = (*env)->NewStringUTF(env, parm.adrkey);
        footnotes = (*env)->NewStringUTF(env, parm.footnotes);
        fipsCounty = (*env)->NewStringUTF(env, parm.county);

        /* Create the Address object */
        jobject addressObj = (*env)->NewObject(env, AddressCls, AddressConstr, firmName, addr1, addr2, city, state, zip5, zip4);

        /* Create the ParsedAddress object */
        jobject parsedAddressObj = (*env)->NewObject(env, ParsedAddressCls, ParsedAddressConstr,
            primaryNum, secondaryNum, ruralRouteNum, secondaryNumUnit, leftPre, rightPre, firstSuffix,
            secondSuffix, leftPost, rightPost, primaryName);

        /* Create the USPSAddress object */
        jobject uspsAddressObj = (*env)->NewObject(env, USPSAddressCls, USPSAddressConstr,
            addressObj, parsedAddressObj, poCity, poState, abbrCity, deliveryPoint, carrierRoute, addressKey, fipsCounty);

        /* Create an array of the address records in the stack */
        jobjectArray addressRecordArray = NULL;
        if (parm.respn > 0) {
            int recordID;
            int recordCount = (parm.respn > ADDRESS_REC_STACK_SIZE) ? ADDRESS_REC_STACK_SIZE : parm.respn;
            addressRecordArray = (*env)->NewObjectArray(env, recordCount, AddressRecordCls, NULL);
            for (recordID = 0; recordID < parm.respn && recordID < ADDRESS_REC_STACK_SIZE; recordID++) {
                jint recordNum = (jint) recordID;
                jstring zipCode = (*env)->NewStringUTF(env, parm.stack[recordID].zip_code);
                jstring updateKeyNum = (*env)->NewStringUTF(env, parm.stack[recordID].update_key);
                jchar actionCode = (jchar) parm.stack[recordID].action_code;
                jchar recordType = (jchar) parm.stack[recordID].rec_type;
                jstring preDir = (*env)->NewStringUTF(env, parm.stack[recordID].pre_dir);
                jstring streetName = (*env)->NewStringUTF(env, parm.stack[recordID].str_name);
                jstring suffix = (*env)->NewStringUTF(env, parm.stack[recordID].suffix);
                jstring postDir = (*env)->NewStringUTF(env, parm.stack[recordID].post_dir);
                jstring primaryLow = (*env)->NewStringUTF(env, parm.stack[recordID].prim_low);
                jstring primaryHigh = (*env)->NewStringUTF(env, parm.stack[recordID].prim_high);
                jchar primaryEO = (jchar) parm.stack[recordID].prim_code;
                jstring bldgFirmName = (*env)->NewStringUTF(env, parm.stack[recordID].sec_name);
                jstring unit = (*env)->NewStringUTF(env, parm.stack[recordID].unit);
                jstring secLow = (*env)->NewStringUTF(env, parm.stack[recordID].sec_low);
                jstring secHigh = (*env)->NewStringUTF(env, parm.stack[recordID].sec_high);
                jchar secCode = parm.stack[recordID].sec_code;
                jstring addonLow = (*env)->NewStringUTF(env, parm.stack[recordID].addon_low);
                jstring addonHigh = (*env)->NewStringUTF(env, parm.stack[recordID].addon_high);
                jchar lacsStatus = (jchar) parm.stack[recordID].lacs_status;
                jstring financeCode = (*env)->NewStringUTF(env, parm.stack[recordID].finance);
                jstring stateAbbr = (*env)->NewStringUTF(env, parm.stack[recordID].state_abbrev);
                jstring countyNum = (*env)->NewStringUTF(env, parm.stack[recordID].county_no);
                jstring congressDist = (*env)->NewStringUTF(env, parm.stack[recordID].congress_dist);
                jstring municipality = (*env)->NewStringUTF(env, parm.stack[recordID].municipality);
                jstring urbanization = (*env)->NewStringUTF(env, parm.stack[recordID].urbanization);
                jstring lastline = (*env)->NewStringUTF(env, parm.stack[recordID].last_line);

                /* Construct an AddressRecord */
                jobject addressRecord = (*env)->NewObject(env, AddressRecordCls, AddressRecordConstr,
                    recordNum, zipCode, preDir, streetName, suffix, postDir, primaryLow, primaryHigh, bldgFirmName, unit,
                    secLow, secHigh, addonLow, addonHigh, financeCode, stateAbbr, countyNum, congressDist, municipality,
                    urbanization, lastline, primaryEO, secCode, recordType);

                /* Append to address record array object */
                (*env)->SetObjectArrayElement(env, addressRecordArray, recordID, addressRecord);
            }
        }

        /* Create the AddressInquiryResult object */
        inquiryResult = (*env)->NewObject(env, AddressInquiryResultCls, AddressInquiryResultConstr,
            (jint) responseCode, uspsAddressObj, (jint) statusCode, footnotes, addressRecordArray);
    }
    /* Handle error response, typically due to a system level error */
    else {
        inquiryResult = (*env)->NewObject(env, AddressInquiryResultCls, AddressInquiryResultConstr,
            (jint) responseCode, NULL, (jint) parm.retcc, NULL, NULL); // ? parm.retcc
    }

    return inquiryResult;
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

void cacheIDs(JNIEnv * env) 
{
    /* Cached class ids */
    jclass tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/AmsSettings");
    AmsSettingsCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/Address");
    AddressCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/AddressRecord");
    AddressRecordCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/ParsedAddress");
    ParsedAddressCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/USPSAddress");
    USPSAddressCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/RecordType");
    ReturnCodeCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    tempClassRef = (*env)->FindClass(env, "gov/nysenate/ams/model/AddressInquiryResult");
    AddressInquiryResultCls = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    (*env)->DeleteLocalRef(env, tempClassRef);

    /* Cached constructors */
    AddressConstr = (*env)->GetMethodID(env, AddressCls, "<init>", "(" REP7(STRING_TYPE) ")V");
    ParsedAddressConstr = (*env)->GetMethodID(env, ParsedAddressCls, "<init>", "(" REP11(STRING_TYPE) ")V");
    AddressRecordConstr = (*env)->GetMethodID(env, AddressRecordCls, "<init>", "(" INT_TYPE REP10(STRING_TYPE) REP10(STRING_TYPE) REP3(CHAR_TYPE) ")V");
    USPSAddressConstr = (*env)->GetMethodID(env, USPSAddressCls, "<init>", "(" ADDRESS_TYPE PARSED_ADDRESS_TYPE REP7(STRING_TYPE) ")V");
    AddressInquiryResultConstr = (*env)->GetMethodID(env, AddressInquiryResultCls, "<init>", "(" INT_TYPE USPS_ADDRESS_TYPE INT_TYPE STRING_TYPE ARRAY_TYPE ADDRESS_RECORD_TYPE")V");

    /* Cached Methods */
    Address_getFirmName = (*env)->GetMethodID(env, AddressCls, "getFirmName", NO_ARGS STRING_TYPE);
    Address_getAddr1 = (*env)->GetMethodID(env, AddressCls, "getAddr1", NO_ARGS STRING_TYPE);
    Address_getAddr2 = (*env)->GetMethodID(env, AddressCls, "getAddr2", NO_ARGS STRING_TYPE);
    Address_getCity = (*env)->GetMethodID(env, AddressCls, "getCity", NO_ARGS STRING_TYPE);
    Address_getState = (*env)->GetMethodID(env, AddressCls, "getState", NO_ARGS STRING_TYPE);
    Address_getZip5 = (*env)->GetMethodID(env, AddressCls, "getZip5", NO_ARGS STRING_TYPE);
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

/*
 *
 */
char * getC_String(JNIEnv * env, const jstring javaString)
{
    return (*env)->GetStringUTFChars(env, javaString, 0);
}

/*
 *
 */
void releaseC_String(JNIEnv * env, const char * cString, const jstring javaString)
{
    (*env)->ReleaseStringUTFChars(env, javaString, cString);
}

/*
 *
 */
void printJString(JNIEnv * env, const jstring string)
{
    const char * nativeString = getC_String(env, string);
    printf("%s\n", nativeString);
    releaseC_String(env, nativeString, string);
}