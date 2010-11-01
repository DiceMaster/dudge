#ifndef DTEST_UTILF_H
#define DTEST_UTILF_H

#include <jni.h>

#include <string>
#include <typeinfo>
#include <cstdio>

using std::string;

jstring jstr_conv(JNIEnv *env, const std::string& s);

/* Конвертирует строку, полученную от
Java-машины, в строку STL. */
std::string jstr_conv(JNIEnv *env, jstring s);

std::string get_property(
		JNIEnv *env,
		jobject obj,
		const std::string& key,
		const std::string& defvalue);

#pragma warning( push )
#pragma warning( disable : 4244 )
//#pragma warning( disable : 4312 )

/* Возвращает значение поля объекта. */
template<class FIELD_TYPE>
FIELD_TYPE get_field(JNIEnv *env,
		jobject obj,
		const std::string& clsname,		//Со слешами вместо точек.
		const std::string& fieldname,
		const std::string& javatname
		)
{
	//Получаем ссылку на класс.
	jclass cls = env->FindClass( clsname.c_str() );
	
	if(javatname == "boolean")
	return (FIELD_TYPE)env->GetBooleanField(obj, env->GetFieldID(cls, fieldname.c_str(),"Z"));
	
	if(javatname == "byte")
	return (FIELD_TYPE)env->GetByteField(obj, env->GetFieldID(cls, fieldname.c_str(),"B"));
	
	if(javatname == "char")
	return (FIELD_TYPE)env->GetCharField(obj, env->GetFieldID(cls, fieldname.c_str(),"C"));
	
	if(javatname == "short")
	return (FIELD_TYPE)env->GetShortField(obj, env->GetFieldID(cls, fieldname.c_str(),"S"));
	
	if(javatname == "int")
	return (FIELD_TYPE)env->GetIntField(obj, env->GetFieldID(cls, fieldname.c_str(),"I"));
	
	if(javatname == "long")
	return (FIELD_TYPE)env->GetLongField(obj, env->GetFieldID(cls, fieldname.c_str(),"J"));
	
	if(javatname == "float")
	return (FIELD_TYPE)env->GetFloatField(obj, env->GetFieldID(cls, fieldname.c_str(),"F"));
	
	if(javatname == "double")
	return (FIELD_TYPE)env->GetDoubleField(obj, env->GetFieldID(cls, fieldname.c_str(),"D"));
	
	FIELD_TYPE foo = 0;
	return foo;
}

/* Устанавливает значение поля объекта. */
template<class FIELD_TYPE>
void set_field(JNIEnv *env,
		jobject obj,
		FIELD_TYPE value,
		const std::string& clsname, //Со слешами вместо точек.
		const std::string& fieldname,
		const std::string& javatname
		)
{
	//Получаем ссылку на класс.
	jclass cls = env->FindClass( clsname.c_str() );
	
	if(javatname == "boolean")
	env->SetBooleanField(obj, env->GetFieldID(cls, fieldname.c_str(),"Z"), value);
	
	if(javatname == "byte")
	env->SetByteField(obj, env->GetFieldID(cls, fieldname.c_str(),"B"), value);
	
	if(javatname == "char")
	env->SetCharField(obj, env->GetFieldID(cls, fieldname.c_str(),"C"), value);
	
	if(javatname == "short")
	env->SetShortField(obj, env->GetFieldID(cls, fieldname.c_str(),"S"), value);
	
	if(javatname == "int")
	env->SetIntField(obj, env->GetFieldID(cls, fieldname.c_str(),"I"), value);
	
	if(javatname == "long")
	env->SetLongField(obj, env->GetFieldID(cls, fieldname.c_str(),"J"), value);
	
	if(javatname == "float")
	env->SetFloatField(obj, env->GetFieldID(cls, fieldname.c_str(),"F"), value);
	
	if(javatname == "double")
	env->SetDoubleField(obj, env->GetFieldID(cls, fieldname.c_str(),"D"), value);
}

#pragma warning( pop )

#endif
