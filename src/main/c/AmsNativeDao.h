#include <jni.h>

/* Header for class gov_nysenate_ams_dao_AmsNativeDao */

#ifndef _Included_AmsNativeDao_Wrapper
#define _Included_AmsNativeDao_Wrapper
#ifdef __cplusplus
extern "C" {
#endif

#define ADDRESS_REC_STACK_SIZE 10

/* JNI Type mappings */

#define STRING_TYPE "Ljava/lang/String;"
#define BOOLEAN_TYPE "Z"
#define BYTE_TYPE "B"
#define CHAR_TYPE "C"
#define SHORT_TYPE "S"
#define INT_TYPE "I"
#define LONG_TYPE "J"
#define FLOAT_TYPE "F"
#define DOUBLE_TYPE "D"
#define ARRAY_TYPE "["
#define NO_ARGS "()"

/* Java Client Specific mappings */

#define ADDRESS_TYPE "Lgov/nysenate/ams/model/Address;"
#define ADDRESS_RECORD_TYPE "Lgov/nysenate/ams/model/AddressRecord;"
#define CITY_RECORD_TYPE "Lgov/nysenate/ams/model/CityRecord;"
#define PARSED_ADDRESS_TYPE "Lgov/nysenate/ams/model/ParsedAddress;"
#define USPS_ADDRESS_TYPE "Lgov/nysenate/ams/model/USPSAddress;"
#define STATUS_CODE_TYPE "Lgov/nysenate/ams/model/StatusCode;"

/* Macro repeaters for convenience */

#define REP1(X) X
#define REP2(X) REP1(X) REP1(X)
#define REP3(X) REP2(X) REP1(X)
#define REP4(X) REP2(X) REP2(X)
#define REP5(X) REP3(X) REP2(X)
#define REP6(X) REP3(X) REP3(X)
#define REP7(X) REP4(X) REP3(X)
#define REP8(X) REP4(X) REP4(X)
#define REP9(X) REP5(X) REP4(X)
#define REP10(X) REP5(X) REP5(X)
#define REP11(X) REP6(X) REP5(X)
#define REP12(X) REP6(X) REP6(X)
#define REP13(X) REP7(X) REP6(X)
#define REP14(X) REP8(X) REP6(X)
#define REP15(X) REP9(X) REP6(X)

void cacheIDs(JNIEnv *);

/* Method callers */

jobject getObjectFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName, const char * returnType);
jstring getStringFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName);
jboolean getBooleanFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName);
jint getIntFromMethod(JNIEnv * env, jclass cls, jobject instance, const char * methodName);

char * getC_String(JNIEnv * env, const jstring javaString);
void releaseC_String(JNIEnv * env, const char * cString, const jstring javaString);
void printJString(JNIEnv * env, const jstring string);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv *, jobject, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    closeAmsLibrary
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_closeAmsLibrary
  (JNIEnv *, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    addressInquiry
 * Signature: (Lgov/nysenate/ams/model/Address;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_addressInquiry
  (JNIEnv *, jobject, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    cityStateLookup
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/CityStateResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_cityStateLookup
  (JNIEnv *, jobject, jstring);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    zip9Inquiry
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_zip9Inquiry
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif