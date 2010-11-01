#include "utilf.h"

using namespace std;

jstring jstr_conv(JNIEnv *env, const string& s)
{
	return env->NewStringUTF(s.c_str());
}

string jstr_conv(JNIEnv *env, jstring s)
{
	const char* buf = env->GetStringUTFChars(s, 0);
	string result_str(buf);
    env->ReleaseStringUTFChars(s, buf);
	
	return result_str;
}

string get_property(
		JNIEnv *env,
		jobject obj,
		const string& key,
		const string& defvalue)
{
	jclass clsSolutionLauncher = env->FindClass("dudge/slave/dtest/SolutionLauncher");
	jclass clsProperties = env->FindClass("java/util/Properties");
	
	jfieldID propsID = env->GetFieldID(clsSolutionLauncher, "props","Ljava/util/Properties;");

    jobject props = env->GetObjectField(obj, propsID);

	jmethodID getPropID = env->GetMethodID(
					clsProperties,
					"getProperty",
					"(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
					);

	jstring value = (jstring) env->CallObjectMethod(props, getPropID, jstr_conv(env, key), jstr_conv(env, defvalue));

	return jstr_conv(env, value);
}
