
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
    openparm.config.address1 = getPath(env, jAmsSettings, "address1Path");
    openparm.config.addrindex = getPath(env, jAmsSettings, "addrIndexPath");
    openparm.config.citystate = getPath(env, jAmsSettings, "cityStatePath");
    openparm.config.crossref = getPath(env, jAmsSettings, "crossRefPath");
    openparm.config.system = getPath(env, jAmsSettings, "systemPath");
    openparm.config.elot = getPath(env, jAmsSettings, "elotPath");
    openparm.config.elotindex = getPath(env, jAmsSettings, "elotIndexPath");
    openparm.config.llkpath = getPath(env, jAmsSettings, "lacsLinkPath");
    openparm.config.dpvpath = getPath(env, jAmsSettings, "dpvPath");
    openparm.config.fnsnpath = getPath(env, jAmsSettings, "fnsPath");
    openparm.config.stelnkpath = getPath(env, jAmsSettings, "suiteLinkPath");
    openparm.config.abrstpath = getPath(env, jAmsSettings, "abrstPath");

    /* Set flags to enable/disable various services. */
    openparm.ewsflag = getFlag(env, jAmsSettings, "ewsEnabled");
    openparm.elotflag = getFlag(env, jAmsSettings, "elotEnabled");
    openparm.systemflag = getFlag(env, jAmsSettings, "systemEnabled");
    openparm.stelnkflag = getFlag(env, jAmsSettings, "suiteLinkEnabled");
    openparm.abrstflag = getFlag(env, jAmsSettings, "abrstEnabled");

    /* Open the USPS AMS using the config. */
    int errorCode = z4opencfg(&openparm);
    if (errorCode == 0) {
        return JNI_TRUE;
    }
    fprintf(stderr, "Failed to open USPS Address Matching System. Error Code %d\n", errorCode);
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

    setAddrField(env, jAddress, Address_addr1, TO_CHAR_FIELD(parm.iadl1));
    setAddrField(env, jAddress, Address_addr2, TO_CHAR_FIELD(parm.iadl3));
    setAddrField(env, jAddress, Address_city, TO_CHAR_FIELD(parm.ictyi));
    setAddrField(env, jAddress, Address_state, TO_CHAR_FIELD(parm.istai));
    setAddrField(env, jAddress, Address_zip9, TO_CHAR_FIELD(parm.izipc));

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
    char* cZip5 = (char*) (*env)->GetStringUTFChars(env, jZip, NULL);
    int errorCode = z4ctyget(&city, cZip5);
    (*env)->ReleaseStringUTFChars(env, jZip, cZip5);

    if (errorCode != 0) {
        return (*env)->NewObject(env, CityStateResult.clazz, CityStateResult.constr, errorCode, NULL);
    }

    jstring zipCode = getJavaString(env, city.zip_code);
    jstring cityName  = getJavaString(env, city.city_name);
    jstring stateAbbrev = getJavaString(env, city.state_abbrev);
    jstring cityKey = getJavaString(env, city.city_key);
    jchar zipClassCode = (jchar)city.zip_class_code;
    jstring cityAbbrev = getJavaString(env, city.city_abbrev);
    jchar facilityCd = (jchar) city.facility_cd;
    jchar mailingNameInd = (jchar) city.mailing_name_ind;
    jstring lastLineNum = getJavaString(env, city.last_line_num);
    jstring lastLineName = getJavaString(env, city.last_line_name);
    jchar cityDelvInd = (jchar) city.city_delv_ind;
    jchar autoZoneInd = (jchar) city.auto_zone_ind;
    jchar uniqueZipInd = (jchar) city.unique_zip_ind;
    jstring countyNo =  getJavaString(env, city.county_no);
    jstring countyName = getJavaString(env, city.county_name);

    jobject cityRecordObj = (*env)->NewObject(env, CityRecord.clazz, CityRecord.constr, countyName, stateAbbrev,
                                                   zipCode,  lastLineName, lastLineNum, cityAbbrev,
                                                   cityName, cityKey, countyNo, zipClassCode,
                                                   mailingNameInd, (jchar) city.detail_code, facilityCd, cityDelvInd,
                                                   autoZoneInd, uniqueZipInd);

    return (*env)->NewObject(env, CityStateResult.clazz, CityStateResult.constr, 0, cityRecordObj);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    zip9seInquiry
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_zip9Inquiry
    (JNIEnv* env, jobject jThis, jstring jZip9) {

    ZIP4_PARM parm;
    memset(&parm, 0, sizeof(ZIP4_PARM));
    setField(env, jZip9, TO_CHAR_FIELD(parm.iadl1));

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
JNIEXPORT jstring JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getAmsVersion(JNIEnv* env, jobject jThis) {
    char ams_version[32];
    z4ver(ams_version);
    return getJavaString(env, ams_version);
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getDataExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getDataExpireDays(JNIEnv* env, jobject jThis) {
    return (jint) z4GetDataExpireDays();
}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getLibraryExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getLibraryExpireDays(JNIEnv* env, jobject jThis) {
    // TODO: deprecated
    return (jint) z4GetCodeExpireDays();
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

    short recordCount = parm->respn;
    /* In the event of multiple matches, standardize on the first result */
    if (responseCode != Z4_SINGLE && responseCode != Z4_DEFAULT && recordCount > 0) {
        z4adrstd(parm, 0);
    }

    /* Create an array of the address records in the stack */
    jobjectArray addressRecordArray = (*env)->NewObjectArray(env, recordCount, AddressRecord.clazz, NULL);
    for (int recordId = 0; recordId < recordCount; recordId++) {
        /* Since only ten records are stored at one time, scroll to the next ten
           when we're done with the current stack. */
        int index = recordId % ARRAY_LENGTH(parm->stack);
        if (index == 0 && recordId != 0) {
            z4scroll(parm);
        }

        ADDR_REC currRec = parm->stack[index];
        jstring streetName = getJavaString(env, currRec.str_name);
        jchar primaryEO = (jchar) currRec.prim_code;
        jstring financeCode = getJavaString(env, currRec.finance);
        jstring countyNum = getJavaString(env, currRec.county_no);

        /* Construct an AddressRecord */
        jobject addressRecord = (*env)->NewObject(env, AddressRecord.clazz, AddressRecord.constr,
                    (jint) recordId, getJavaString(env, currRec.zip_code), getJavaString(env, currRec.pre_dir),
                    streetName, getJavaString(env, currRec.suffix), getJavaString(env, currRec.post_dir),
                    getJavaString(env, currRec.prim_low), getJavaString(env, currRec.prim_high),
                    getJavaString(env, currRec.sec_name), getJavaString(env, currRec.unit),
                    getJavaString(env, currRec.sec_low), getJavaString(env, currRec.sec_high),
                    getJavaString(env, currRec.addon_low), getJavaString(env, currRec.addon_high),
                    financeCode, getJavaString(env, currRec.state_abbrev), countyNum,
                    getJavaString(env, currRec.congress_dist), getJavaString(env, currRec.municipality),
                    getJavaString(env, currRec.urbanization), getJavaString(env, currRec.last_line),
                    primaryEO, (jchar) currRec.sec_code, (jchar) currRec.rec_type);

        /* Append to address record array object */
        (*env)->SetObjectArrayElement(env, addressRecordArray, recordId, addressRecord);
    }

    /* Create the AddressInquiryResult object */
    return (*env)->NewObject(env, AddressInquiryResult.clazz, AddressInquiryResult.constr,
                (jint) 0, getUspsAddress(env, parm), (jint) responseCode, getJavaString(env, parm->footnotes), addressRecordArray);
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

ClassData getConstructorData(JNIEnv* env, const char* classLocation, const char* signature) {
    jclass tempClassRef = (*env)->FindClass(env, classLocation);
    jclass permClassRef = (jclass) (*env)->NewGlobalRef(env, tempClassRef);
    jmethodID constructor = (*env)->GetMethodID(env, permClassRef, "<init>", signature);
    return (ClassData) {permClassRef, constructor};
}

char* getPath(JNIEnv* env, jobject settingsObj, const char* methodName) {
    jclass settingsClass = (*env)->GetObjectClass(env, settingsObj);
    jmethodID methodID = (*env)->GetMethodID(env, settingsClass, methodName, NO_ARGS STRING_TYPE);
    jstring valueString = (jstring) (*env)->CallObjectMethod(env, settingsObj, methodID);
    return (char*) (*env)->GetStringUTFChars(env, valueString, NULL);
}

char getFlag(JNIEnv* env, jobject settingsObj, const char* methodName) {
    jclass settingsClass = (*env)->GetObjectClass(env, settingsObj);
    jmethodID methodID = (*env)->GetMethodID(env, settingsClass, methodName, NO_ARGS BOOLEAN_TYPE);
    jboolean boolFlag = (*env)->CallBooleanMethod(env, settingsObj, methodID);
    return boolFlag == JNI_TRUE ? 'Y' : 'N';
}

void setAddrField(JNIEnv* env, jobject jAddress, jmethodID method, CharArray charArray) {
    jstring fieldString = (jstring) (*env)->CallObjectMethod(env, jAddress, method);
    setField(env, fieldString, charArray);
}

void setField(JNIEnv* env, jstring javaString, CharArray charArray) {
    char* cString = (char*) (*env)->GetStringUTFChars(env, javaString, NULL);
    strncpy(charArray.chars, cString, charArray.length);
    // Ensures the field is null-terminated.
    charArray.chars[charArray.length - 1] = '\0';
    (*env)->ReleaseStringUTFChars(env, javaString, cString);
}

jstring getJavaString(JNIEnv* env, const char* cString) {
    return (*env)->NewStringUTF(env, cString);
}

jobject getUspsAddress(JNIEnv* env, ZIP4_PARM* parm) {
    /* Get the validated address data */
    jstring firmName = getJavaString(env, parm->dadl2);
    jstring addr1 = getJavaString(env, parm->dadl1);
    jstring addr2 = getJavaString(env, parm->dadl3);
    jstring city = getJavaString(env, parm->dctya);
    jstring state = getJavaString(env, parm->dstaa);
    jstring zip5 = getJavaString(env, parm->zipc);
    jstring zip4 = getJavaString(env, parm->addon);

    /* Create the Address object */
    jobject addressObj = (*env)->NewObject(env, Address.clazz, Address.constr, firmName, addr1, addr2, city, state, zip5, zip4);

    /* Get the parsed input data */
    jstring primaryNum = getJavaString(env, parm->ppnum);
    jstring secondaryNum = getJavaString(env, parm->psnum);
    jstring ruralRouteNum = getJavaString(env, parm->prote);
    jstring secondaryNumUnit = getJavaString(env, parm->punit);
    jstring leftPre = getJavaString(env, parm->ppre1);
    jstring rightPre = getJavaString(env, parm->ppre2);
    jstring firstSuffix = getJavaString(env, parm->psuf1);
    jstring secondSuffix = getJavaString(env, parm->psuf2);
    jstring leftPost = getJavaString(env, parm->ppst1);
    jstring rightPost = getJavaString(env, parm->ppst2);
    jstring primaryName = getJavaString(env, parm->ppnam);
    /* Create the ParsedAddress object */
    jobject parsedAddressObj = (*env)->NewObject(env, ParsedAddress.clazz, ParsedAddress.constr,
        primaryNum, secondaryNum, ruralRouteNum, secondaryNumUnit, leftPre, rightPre, firstSuffix, secondSuffix,
        leftPost, rightPost, primaryName);

    /* Get additional address data */
    jstring poCity = getJavaString(env, parm->dctys);
    jstring poState = getJavaString(env, parm->dstas);
    jstring abbrCity = getJavaString(env, parm->abcty);
    jstring deliveryPoint = getJavaString(env, parm->dpbc);
    jstring carrierRoute = getJavaString(env, parm->cris);
    jstring addressKey = getJavaString(env, parm->adrkey);
    jstring fipsCounty = getJavaString(env, parm->county);

    return (*env)->NewObject(env, USPSAddress.clazz, USPSAddress.constr,
                addressObj, parsedAddressObj, poCity, poState, abbrCity, deliveryPoint, carrierRoute, addressKey, fipsCounty);
}
