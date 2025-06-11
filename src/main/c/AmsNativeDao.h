
#include <jni.h>
#include <zip4.h>

/* JNI Header for class gov.nysenate.ams.dao.AmsNativeDao */

#ifndef _Included_AmsNativeDao_Wrapper
#define _Included_AmsNativeDao_Wrapper
#ifdef __cplusplus
extern "C" {
#endif

#define ARRAY_LENGTH(x) sizeof(x)/sizeof(x[0])

/* JNI Type mappings */
#define STRING_TYPE "Ljava/lang/String;"
#define BOOLEAN_TYPE "Z"
#define CHAR_TYPE "C"
#define INT_TYPE "I"
#define ARRAY_TYPE "["
#define NO_ARGS "()"

/* Java Client Specific mappings */
#define MODEL_PATH "gov/nysenate/ams/model/"
#define PATH_OF(className) MODEL_PATH className
#define TYPE_OF(className) "L" PATH_OF(className) ";"

/* Macro repeaters for convenience */
#define REP2(X) X X
#define REP3(X) REP2(X) X
#define REP7(X) REP3(X) REP3(X) X
#define REP9(X) REP3(X) REP3(X) REP3(X)
#define REP11(X) REP7(X) REP3(X) X
#define REP20(X) REP11(X) REP9(X)

typedef struct {
    jclass clazz;
    jmethodID constr;
} ClassData;

/* Create global references to class/method IDs */
void cacheIDs(JNIEnv*);

/* Used to construct AddressInquiryResult object */
jobject handleAddressInquiryResult(JNIEnv* env, ZIP4_PARM* zip4_parm, int responseCode);

/* Helpers */
char* getC_StringFromSettings(JNIEnv* env, jobject instance, const char* methodName);
char getFlagFromSettings(JNIEnv* env, jobject instance, const char* methodName);
ClassData getConstructorData(JNIEnv* env, const char* classLocation, const char* signature);
void setAddrField(JNIEnv* env, jobject jAddress, jmethodID method, char* field, unsigned int length);
void setField(JNIEnv* env, jstring javaString, char* field, unsigned int length);
char* getC_String(JNIEnv* env, const jstring javaString);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    setupAmsLibrary
 * Signature: (Lgov/nysenate/ams/model/AmsSettings;)Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_setupAmsLibrary
  (JNIEnv*, jobject, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    closeAmsLibrary
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_closeAmsLibrary
  (JNIEnv*, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    addressInquiry
 * Signature: (Lgov/nysenate/ams/model/Address;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_addressInquiry
  (JNIEnv*, jobject, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    cityStateLookup
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/CityStateResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_cityStateLookup
  (JNIEnv*, jobject, jstring);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    zip9Inquiry
 * Signature: (Ljava/lang/String;)Lgov/nysenate/ams/model/AddressInquiryResult;
 */
JNIEXPORT jobject JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_zip9Inquiry
  (JNIEnv*, jobject, jstring);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getAmsVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getAmsVersion
  (JNIEnv*, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getDataExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getDataExpireDays
  (JNIEnv*, jobject);

/*
 * Class:     gov_nysenate_ams_dao_AmsNativeDao
 * Method:    getLibraryExpireDays
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_gov_nysenate_ams_dao_AmsNativeDao_getLibraryExpireDays
  (JNIEnv*, jobject);

#ifdef __cplusplus
}
#endif
#endif
