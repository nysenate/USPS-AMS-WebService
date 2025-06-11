
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>

#include <jni.h>
#include <zip4.h>

#include "AmsNativeDao.h"

/* Cached class data */
static ClassData Address;
static ClassData AddressRecord;
static ClassData ParsedAddress;
static ClassData USPSAddress;
static ClassData AddressInquiryResult;
static ClassData CityRecord;
static ClassData CityStateResult;

/* Cached methods */
static jmethodID Address_addr1;
static jmethodID Address_addr2;
static jmethodID Address_city;
static jmethodID Address_state;
static jmethodID Address_zip9;

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv* env, jobject jThis, jobject jAmsSettings) {
    /* Cache all method/constructor ids */
    cacheIDs(env);

    /* Initialize the config param struct */
    Z4OPEN_PARM openparm;
    memset(&openparm, 0, sizeof(openparm));


    /* Construct the openparm structure to pass to the z4opencfg method. */
    openparm.config.system = getC_StringFromSettings(env, jAmsSettings, "systemPath");
    openparm.config.address1 = getC_StringFromSettings(env, jAmsSettings, "address1Path");
    openparm.config.addrindex = getC_StringFromSettings(env, jAmsSettings, "addrIndexPath");
    openparm.config.citystate = getC_StringFromSettings(env, jAmsSettings, "cityStatePath");
    openparm.config.crossref = getC_StringFromSettings(env, jAmsSettings, "crossRefPath");
    openparm.config.elot = getC_StringFromSettings(env, jAmsSettings, "elotPath");
    openparm.config.elotindex = getC_StringFromSettings(env, jAmsSettings, "elotIndexPath");
    openparm.config.llkpath = getC_StringFromSettings(env, jAmsSettings, "lacsLinkPath");
    openparm.config.dpvpath = getC_StringFromSettings(env, jAmsSettings, "dpvPath");
    openparm.config.fnsnpath = getC_StringFromSettings(env, jAmsSettings, "fnsPath");
    openparm.config.stelnkpath = getC_StringFromSettings(env, jAmsSettings, "suiteLinkPath");
    openparm.config.abrstpath = getC_StringFromSettings(env, jAmsSettings, "abrstPath");

    /* Set flags to enable/disable various services. */
    openparm.elotflag = getFlagFromSettings(env, jAmsSettings, "elotEnabled");
    openparm.ewsflag = getFlagFromSettings(env, jAmsSettings, "ewsEnabled");
    openparm.stelnkflag = getFlagFromSettings(env, jAmsSettings, "suiteLinkEnabled");
    openparm.abrstflag = getFlagFromSettings(env, jAmsSettings, "abrstEnabled");
    openparm.systemflag = getFlagFromSettings(env, jAmsSettings, "systemEnabled");

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
  (JNIEnv* env, jobject jThis) {
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
  (JNIEnv* env, jobject jThis, jobject jAddress) {
    ZIP4_PARM parm;
    memset(&parm, 0, sizeof(ZIP4_PARM));

    // TODO: use struct with length?
    setAddrField(env, jAddress, Address_addr1, parm.iadl1, ARRAY_LENGTH(parm.iadl1));
    setAddrField(env, jAddress, Address_addr2, parm.iadl3, ARRAY_LENGTH(parm.iadl3));
    setAddrField(env, jAddress, Address_city, parm.ictyi, ARRAY_LENGTH(parm.ictyi));
    setAddrField(env, jAddress, Address_state, parm.istai, ARRAY_LENGTH(parm.istai));
    setAddrField(env, jAddress, Address_zip9, parm.izipc, ARRAY_LENGTH(parm.izipc));

    /* Call the AMS address inquiry and standardization methods */
    int errorCode = z4adrinq(&parm);

    /* Create the AddressInquiryResult object using the data in parm. */
    return handleAddressInquiryResult(env, &parm, errorCode);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    cityStateLookup
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/CityStateResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_cityStateLookup
    (JNIEnv* env, jobject jThis, jstring jZip) {

    CITY_REC city;
    char* cZip5 = getC_String(env, jZip);
    int errorCode = z4ctyget(&city, cZip5);

    if (errorCode != 0) {
        return (*env)->NewObject(env, CityStateResult.clazz, CityStateResult.constr, errorCode, NULL);
    }

    jstring zipCode = (*env)->NewStringUTF(env, city.zip_code);
    jstring cityName  = (*env)->NewStringUTF(env, city.city_name);
    jstring stateAbbrev = (*env)->NewStringUTF(env, city.state_abbrev);
    jchar detailCode = (jchar)city.detail_code;
    jstring cityKey = (*env)->NewStringUTF(env, city.city_key);
    jchar zipClassCode = (jchar)city.zip_class_code;
    jstring cityAbbrev = (*env)->NewStringUTF(env, city.city_abbrev);
    jchar facilityCd = (jchar) city.facility_cd;
    jchar mailingNameInd = (jchar) city.mailing_name_ind;
    jstring lastLineNum = (*env)->NewStringUTF(env, city.last_line_num);
    jstring lastLineName = (*env)->NewStringUTF(env, city.last_line_name);
    jchar cityDelvInd = (jchar) city.city_delv_ind;
    jchar autoZoneInd = (jchar) city.auto_zone_ind;
    jchar uniqueZipInd = (jchar) city.unique_zip_ind;
    jstring countyNo =  (*env)->NewStringUTF(env, city.county_no);
    jstring countyName = (*env)->NewStringUTF(env, city.county_name);

    jobject cityRecordObj = (*env)->NewObject(env, CityRecord.clazz, CityRecord.constr, countyName, stateAbbrev,
                                                   zipCode,  lastLineName, lastLineNum, cityAbbrev,
                                                   cityName, cityKey, countyNo, zipClassCode,
                                                   mailingNameInd, detailCode, facilityCd, cityDelvInd,
                                                   autoZoneInd, uniqueZipInd);

    return (*env)->NewObject(env, CityStateResult.clazz, CityStateResult.constr, 0, cityRecordObj);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    zip9Inquiry
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_zip9Inquiry
    (JNIEnv* env, jobject jThis, jstring jZip9) {

    ZIP4_PARM parm;
    memset(&parm, 0, sizeof(ZIP4_PARM));
    setField(env, jZip9, parm.iadl1, ARRAY_LENGTH(parm.iadl1));

    /* Call the AMS address inquiry and standardization methods */
    int errorCode = z4xrfinq(&parm);
    /* Create the AddressInquiryResult object using the data in parm. */
    return handleAddressInquiryResult(env, &parm, errorCode);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getAmsVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getAmsVersion (JNIEnv* env, jobject jThis) {
    char ams_version[32];
    z4ver(ams_version);
    return (*env)->NewStringUTF(env, ams_version);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getDataExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getDataExpireDays (JNIEnv* env, jobject jThis) {
    return (jint)z4GetDataExpireDays();
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getLibraryExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getLibraryExpireDays (JNIEnv* env, jobject jThis) {
    // TODO: deprecated
    return (jint)z4GetCodeExpireDays();
}

/**
* After an address inquiry method has been called, this method will create a Java AddressInquiryResult
* object using the data stored in the ZIP4_PARM struct.
*/
jobject handleAddressInquiryResult(JNIEnv* env, ZIP4_PARM* parm, int errorCode) {
    int responseCode = parm->retcc;

    if (errorCode != 0) {
        return (*env)->NewObject(env, AddressInquiryResult.clazz, AddressInquiryResult.constr,
                    (jint) errorCode, NULL, (jint) responseCode, NULL, NULL);
    }
    /* In the event of multiple matches,standardize on the first result */
    if (responseCode != Z4_SINGLE && responseCode != Z4_DEFAULT && parm->respn > 0) {
        z4adrstd(parm, 0);
    }

    /* Get the validated address data */
    jstring firmName = (*env)->NewStringUTF(env, parm->dadl2);
    jstring addr1 = (*env)->NewStringUTF(env, parm->dadl1);
    jstring addr2 = (*env)->NewStringUTF(env, parm->dadl3);
    jstring city = (*env)->NewStringUTF(env, parm->dctya);
    jstring state = (*env)->NewStringUTF(env, parm->dstaa);
    jstring zip5 = (*env)->NewStringUTF(env, parm->zipc);
    jstring zip4 = (*env)->NewStringUTF(env, parm->addon);

    /* Get the parsed input data */
    jstring primaryNum = (*env)->NewStringUTF(env, parm->ppnum);
    jstring secondaryNum = (*env)->NewStringUTF(env, parm->psnum);
    jstring ruralRouteNum = (*env)->NewStringUTF(env, parm->prote);
    jstring secondaryNumUnit = (*env)->NewStringUTF(env, parm->punit);
    jstring leftPre = (*env)->NewStringUTF(env, parm->ppre1);
    jstring rightPre = (*env)->NewStringUTF(env, parm->ppre2);
    jstring firstSuffix = (*env)->NewStringUTF(env, parm->psuf1);
    jstring secondSuffix = (*env)->NewStringUTF(env, parm->psuf2);
    jstring leftPost = (*env)->NewStringUTF(env, parm->ppst1);
    jstring rightPost = (*env)->NewStringUTF(env, parm->ppst2);
    jstring primaryName = (*env)->NewStringUTF(env, parm->ppnam);

    /* Get additional address data */
    jstring poCity = (*env)->NewStringUTF(env, parm->dctys);
    jstring poState = (*env)->NewStringUTF(env, parm->dstas);
    jstring abbrCity = (*env)->NewStringUTF(env, parm->abcty);
    jstring deliveryPoint = (*env)->NewStringUTF(env, parm->dpbc);
    jstring carrierRoute = (*env)->NewStringUTF(env, parm->cris);
    jstring addressKey = (*env)->NewStringUTF(env, parm->adrkey);
    jstring footnotes = (*env)->NewStringUTF(env, parm->footnotes);
    jstring fipsCounty = (*env)->NewStringUTF(env, parm->county);

    /* Create the Address object */
    jobject addressObj = (*env)->NewObject(env, Address.clazz, Address.constr, firmName, addr1, addr2, city, state, zip5, zip4);

    /* Create the ParsedAddress object */
    jobject parsedAddressObj = (*env)->NewObject(env, ParsedAddress.clazz, ParsedAddress.constr,
                primaryNum, secondaryNum, ruralRouteNum, secondaryNumUnit, leftPre, rightPre, firstSuffix,
                secondSuffix, leftPost, rightPost, primaryName);

    /* Create the USPSAddress object */
    jobject uspsAddressObj = (*env)->NewObject(env, USPSAddress.clazz, USPSAddress.constr,
                addressObj, parsedAddressObj, poCity, poState, abbrCity, deliveryPoint, carrierRoute, addressKey, fipsCounty);

    short recordCount = parm->respn;
    unsigned int recordId, index;
    /* Create an array of the address records in the stack */
    jobjectArray addressRecordArray = (*env)->NewObjectArray(env, recordCount, AddressRecord.clazz, NULL);
    for (recordId = 0; recordId < recordCount; recordId++) {
        /* Since only ten records are stored at one time, scroll to the next ten
           when we're done with the current stack. */
        index = recordId % ARRAY_LENGTH(parm->stack);
        if (index == 0 && recordId != 0) {
            z4scroll(parm);
        }

        jstring zipCode = (*env)->NewStringUTF(env, parm->stack[index].zip_code);
        jchar recordType = (jchar) parm->stack[index].rec_type;
        jstring preDir = (*env)->NewStringUTF(env, parm->stack[index].pre_dir);
        jstring streetName = (*env)->NewStringUTF(env, parm->stack[index].str_name);
        jstring suffix = (*env)->NewStringUTF(env, parm->stack[index].suffix);
        jstring postDir = (*env)->NewStringUTF(env, parm->stack[index].post_dir);
        jstring primaryLow = (*env)->NewStringUTF(env, parm->stack[index].prim_low);
        jstring primaryHigh = (*env)->NewStringUTF(env, parm->stack[index].prim_high);
        jchar primaryEO = (jchar) parm->stack[index].prim_code;
        jstring bldgFirmName = (*env)->NewStringUTF(env, parm->stack[index].sec_name);
        jstring unit = (*env)->NewStringUTF(env, parm->stack[index].unit);
        jstring secLow = (*env)->NewStringUTF(env, parm->stack[index].sec_low);
        jstring secHigh = (*env)->NewStringUTF(env, parm->stack[index].sec_high);
        jchar secCode = parm->stack[index].sec_code;
        jstring addonLow = (*env)->NewStringUTF(env, parm->stack[index].addon_low);
        jstring addonHigh = (*env)->NewStringUTF(env, parm->stack[index].addon_high);
        jstring financeCode = (*env)->NewStringUTF(env, parm->stack[index].finance);
        jstring stateAbbr = (*env)->NewStringUTF(env, parm->stack[index].state_abbrev);
        jstring countyNum = (*env)->NewStringUTF(env, parm->stack[index].county_no);
        jstring congressDist = (*env)->NewStringUTF(env, parm->stack[index].congress_dist);
        jstring municipality = (*env)->NewStringUTF(env, parm->stack[index].municipality);
        jstring urbanization = (*env)->NewStringUTF(env, parm->stack[index].urbanization);
        jstring lastline = (*env)->NewStringUTF(env, parm->stack[index].last_line);

        /* Construct an AddressRecord */
        jobject addressRecord = (*env)->NewObject(env, AddressRecord.clazz, AddressRecord.constr,
                    (jint) recordId, zipCode, preDir, streetName, suffix, postDir, primaryLow, primaryHigh, bldgFirmName, unit,
                    secLow, secHigh, addonLow, addonHigh, financeCode, stateAbbr, countyNum, congressDist, municipality,
                    urbanization, lastline, primaryEO, secCode, recordType);

        /* Append to address record array object */
        (*env)->SetObjectArrayElement(env, addressRecordArray, recordId, addressRecord);
    }

    /* Create the AddressInquiryResult object */
    return (*env)->NewObject(env, AddressInquiryResult.clazz, AddressInquiryResult.constr,
                (jint) 0, uspsAddressObj, (jint) responseCode, footnotes, addressRecordArray);
}

/**
* To minimize overhead, classes and their constructors are globally cached using this method.
* This method should be called during the setup/config stage.
*/
void cacheIDs(JNIEnv* env) {
    Address = getConstructorData(env, PATH_OF("Address"), "(" REP7(STRING_TYPE) ")V");
    AddressRecord = getConstructorData(env, PATH_OF("AddressRecord"), "(" INT_TYPE REP20(STRING_TYPE) REP3(CHAR_TYPE) ")V");
    ParsedAddress = getConstructorData(env, PATH_OF("ParsedAddress"), "(" REP11(STRING_TYPE) ")V");
    USPSAddress = getConstructorData(env, PATH_OF("USPSAddress"), "(" TYPE_OF("Address") TYPE_OF("ParsedAddress") REP7(STRING_TYPE) ")V");
    AddressInquiryResult = getConstructorData(env, PATH_OF("AddressInquiryResult"), "(" INT_TYPE TYPE_OF("USPSAddress") INT_TYPE STRING_TYPE ARRAY_TYPE TYPE_OF("AddressRecord") ")V");
    CityRecord = getConstructorData(env, PATH_OF("CityRecord"), "(" REP9(STRING_TYPE) REP7(CHAR_TYPE)")V");
    CityStateResult = getConstructorData(env, PATH_OF("CityStateResult"), "("INT_TYPE TYPE_OF("CityRecord") ")V");

    /* Cached Methods */
    Address_addr1 = (*env)->GetMethodID(env, Address.clazz, "addr1", NO_ARGS STRING_TYPE);
    Address_addr2 = (*env)->GetMethodID(env, Address.clazz, "addr2", NO_ARGS STRING_TYPE);
    Address_city = (*env)->GetMethodID(env, Address.clazz, "city", NO_ARGS STRING_TYPE);
    Address_state = (*env)->GetMethodID(env, Address.clazz, "state", NO_ARGS STRING_TYPE);
    Address_zip9 = (*env)->GetMethodID(env, Address.clazz, "zip9", NO_ARGS STRING_TYPE);
}

char* getC_StringFromSettings(JNIEnv* env, jobject instance, const char* methodName) {
    jclass settingsClass = (*env)->GetObjectClass(env, instance);
    jmethodID methodID = (*env)->GetMethodID(env, settingsClass, methodName, NO_ARGS STRING_TYPE);
    jstring javaString = (jstring)(*env)->CallObjectMethod(env, instance, methodID);
    return getC_String(env, javaString);
}

char getFlagFromSettings(JNIEnv* env, jobject instance, const char* methodName) {
    jclass settingsClass = (*env)->GetObjectClass(env, instance);
    jmethodID methodID = (*env)->GetMethodID(env, settingsClass, methodName, NO_ARGS BOOLEAN_TYPE);
    jboolean boolFlag = (*env)->CallBooleanMethod(env, instance, methodID);
    return boolFlag == JNI_TRUE ? 'Y' : 'N';
}

ClassData getConstructorData(JNIEnv* env, const char* classLocation, const char* signature) {
    jclass tempClassRef = (*env)->FindClass(env, classLocation);
    jclass permClassRef = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    jmethodID constructor = (*env)->GetMethodID(env, permClassRef, "<init>", signature);
    ClassData data = {permClassRef, constructor};
    return data;
}

void setAddrField(JNIEnv* env, jobject jAddress, jmethodID method, char* field, unsigned int length) {
    jstring javaString = (jstring)(*env)->CallObjectMethod(env, jAddress, method);
    setField(env, javaString, field, length);
}

void setField(JNIEnv* env, jstring javaString, char* field, unsigned int length) {
    char* cString = getC_String(env, javaString);
    // Ensures the last character is NULL.
    strncpy(field, cString, length - 1);
    (*env)->ReleaseStringUTFChars(env, javaString, cString);
}

char* getC_String(JNIEnv* env, const jstring javaString) {
    return (char*) (*env)->GetStringUTFChars(env, javaString, 0);
}
