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
    jstring systemPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getSystemPath", NO_ARGS STRING_TYPE);
    jstring addr1Path = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getAddress1Path", NO_ARGS STRING_TYPE);
    jstring addrIndexPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getAddrIndexPath", NO_ARGS STRING_TYPE);
    jstring cityStatePath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getCityStatePath", NO_ARGS STRING_TYPE);
    jstring crossRefPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getCrossRefPath", NO_ARGS STRING_TYPE);
    jstring elotPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getElotPath", NO_ARGS STRING_TYPE);
    jstring elotIndexPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getElotIndexPath", NO_ARGS STRING_TYPE);
    jstring lacsLinkPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getLacslinkPath", NO_ARGS STRING_TYPE);
    jstring dpvPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getDpvPath", NO_ARGS STRING_TYPE);
    jstring fnsPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getFnsPath", NO_ARGS STRING_TYPE);
    jstring suitelinkPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getSuitelinkPath", NO_ARGS STRING_TYPE);
    jstring abrstPath = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getAbrstPath", NO_ARGS STRING_TYPE);

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

}

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    addressInquiry
 * Signature: (Lgov/nysenate/ams/model/Address;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_addressInquiry
  (JNIEnv * env, jobject jThis, jobject jAddress)
{
    jclass thisClass = (*env)->GetObjectClass(env, jThis);
    jclass addressClass = (*env)->GetObjectClass(env, jAddress);
    jmethodID mid;
    jfieldID fid;
    jstring addr1, city;
    mid = (*env)->GetMethodID(env, addressClass, "getAddr1", "()Ljava/lang/String;");
    addr1 = (jstring)(*env)->CallObjectMethod(env, jAddress, mid);
    mid = (*env)->GetMethodID(env, addressClass, "getCity", "()Ljava/lang/String;");
    city = (jstring)(*env)->CallObjectMethod(env, jAddress, mid);

    printJString(env, addr1);
    printJString(env, city);

    printf("[C] Got to the end of this function!\n");
    return jAddress;
    //const char * addr1Str = (*env)->GetStringUTFChars(env, addr1)
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
    const char *nativeString = (*env)->GetStringUTFChars(env, string, 0);
    printf("%s\n", nativeString);
    (*env)->ReleaseStringUTFChars(env, string, nativeString);
}