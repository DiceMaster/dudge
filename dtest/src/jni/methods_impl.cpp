#include "dudge_slave_dtest_SolutionLauncher.h"

#include <string>
#include <iostream>
#include <sstream>

using namespace std;

#include "../dtest.h"
#include "utilf.h"

JNIEXPORT jboolean JNICALL Java_dudge_slave_dtest_SolutionLauncher_init
  (JNIEnv *env, jclass thisClass)
{
	return dtest_init();
}

JNIEXPORT jint JNICALL Java_dudge_slave_dtest_SolutionLauncher_createContext(JNIEnv *env, jclass thisClass)
{
	return 0;
}

JNIEXPORT void JNICALL Java_dudge_slave_dtest_SolutionLauncher_freeContext(JNIEnv *env, jclass thisClass, jint id)
{
}

JNIEXPORT jobject JNICALL Java_dudge_slave_dtest_SolutionLauncher_checkSolution(
	JNIEnv *env,
	jobject thisObject,
	jobject limits,
	jstring command,
	jobject inps,
	jobject outs,
	jobject errorStream
	)
{	

	jclass thisClass = env->FindClass("dudge/slave/dtest/SolutionLauncher");

	jclass clsCheckingResult = env->FindClass(
					"dudge/slave/dtest/CheckingResult");
	
	jmethodID ctorCheckingResult = env->GetMethodID(
					clsCheckingResult,
					"<init>",
					"()V");
	jobject result = env->NewObject(
					clsCheckingResult,
					ctorCheckingResult);
	
	jclass clsInputStream = env->FindClass("java/io/InputStream");
	
	jmethodID methInpsRead = env->GetMethodID(
					clsInputStream,
					"read",
					"([B)I");
	
	jclass clsOutputStream = env->FindClass("java/io/OutputStream");

	jmethodID methOutsWrite = env->GetMethodID(
					clsOutputStream,
					"write",
					"([BII)V");
		
	jclass clsWriter = env->FindClass("java/io/Writer");

	jmethodID methWriterWriteString = env->GetMethodID(
				clsWriter,
				"write",
				"(Ljava/lang/String;)V");

	
	string exe_command = jstr_conv(env, command);
		
	string test_dir_path = jstr_conv(
		env,
		(jstring) env->GetObjectField(
			thisObject,
			env->GetFieldID(thisClass, "testDir","Ljava/lang/String;")
			)
		);
	
	run_limits lims;
	
	lims.cpu_msec = get_field<jint>(
					env,
					limits,
					"dudge/slave/dtest/CheckingLimits",
					"cpuTimeLimit",
					"int");
	lims.timeout_msec = int( lims.cpu_msec * (-0.012 * lims.cpu_msec + 144) / 100 );

	lims.mem_bytes = get_field<jint>(
					env,
					limits,
					"dudge/slave/dtest/CheckingLimits",
					"memoryLimit",
					"int");
	lims.out_bytes = get_field<jint>(
					env,
					limits,
					"dudge/slave/dtest/CheckingLimits",
					"outputLimit",
					"int");
	lims.proc_num = get_field<jint>(
					env,
					limits,
					"dudge/slave/dtest/CheckingLimits",
					"processLimit",
					"int");
	
	stringstream input_test;

        jfieldID bufferField = env->GetFieldID(thisClass, "buffer", "[B");
        jbyteArray bufferJava = (jbyteArray) env->GetObjectField(thisObject, bufferField);
        jbyte buffer[64 * 1024];

	//Записываем содержимое теста в поток.
	jint count;
	while(
		-1 != ( count = env->CallIntMethod(inps, methInpsRead, bufferJava) )
	)
	{
            env->GetByteArrayRegion(bufferJava, 0, count, buffer);
            input_test.write((const char*)buffer, count);
	}

	stringstream sol_output;

	bool use_privelege_drop =
		get_property(env, thisObject, "dtest.usePrivelegeDrop", "false") == "true";

	checking_result our_res;
	
	//Сохраняем старый поток ошибок.
	stringstream my_error_stream;
	//Запускаем проверку решения.
	if(!use_privelege_drop)
	{
		our_res = check_solution(
				lims,
				exe_command,
				test_dir_path,
				input_test,
				sol_output,
				my_error_stream
				);
	}
	else
	{
		our_res = check_solution_as_user(
				lims,
				exe_command,
				test_dir_path,
				input_test,
				sol_output,
				my_error_stream,
				get_property(env, thisObject, "dtest.username", ""),
				get_property(env, thisObject, "dtest.domain", "."),
				get_property(env, thisObject, "dtest.password", "")
				);
	}

	//Возвращаем результаты проверки.
	set_field<jbyte>(env,
		result,
		our_res.res_type,
		"dudge/slave/dtest/CheckingResult",
		"resultType",
		"byte");

	set_field<jint>(env,
		result,
		our_res.ret_value,
		"dudge/slave/dtest/CheckingResult",
		"returnedValue",
		"int");

	// Записываем полученный от программы вывод в выходной явовый поток.
        do
	{
            sol_output.read((char*)buffer, sizeof(buffer));
            count = sol_output.gcount();

            env->SetByteArrayRegion(bufferJava, 0, count, buffer);
            env->CallVoidMethod(outs, methOutsWrite, bufferJava, 0, count);
	} while(!sol_output.fail());
	
	// Записываем в поток ошибок выведенные системные ошибки.
	env->CallVoidMethod(errorStream, methWriterWriteString, jstr_conv(env, my_error_stream.str()) );

	return result;
}

JNIEXPORT jstring JNICALL Java_dudge_slave_dtest_SolutionLauncher_createTemporaryDirectory(
	JNIEnv *env,
	jobject thisObject
	)
{
	return jstr_conv(env, create_test_dir());
}

JNIEXPORT jboolean JNICALL Java_dudge_slave_dtest_SolutionLauncher_deleteTemporaryDirectory(
	JNIEnv *env,
	jobject thisObject,
	jstring path
	)
{
	return delete_test_dir(jstr_conv(env, path));
}
