#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <zip4.h>
#include <time.h>
#include <sys/time.h>

#include "AmsNativeDao.h"

void printJString(JNIEnv * env, const jstring string);
jobject getObjectFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName, const char * returnType);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv * env, jobject jThis, jobject jAmsSettings)
{
    jclass AmsSettingsCls = (*env)->GetObjectClass(env, jAmsSettings);
    jstring systemPathStr = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getSystemPath", "()Ljava/lang/String;");
    jstring address1PathStr = (jstring) getObjectFromMethod(env, AmsSettingsCls, jAmsSettings, "getAddress1Path", "()Ljava/lang/String;");

    printJString(env, systemPathStr);
    printJString(env, address1PathStr);

    jboolean ret = JNI_TRUE;
    return ret;
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

void printJString(JNIEnv * env, const jstring string) {
    const char *nativeString = (*env)->GetStringUTFChars(env, string, 0);
    printf("%s\n", nativeString);
    (*env)->ReleaseStringUTFChars(env, string, nativeString);
}
