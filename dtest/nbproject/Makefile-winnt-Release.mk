#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=MinGW-Windows
CND_CONF=winnt-Release
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/unix/unix_ops.o \
	${OBJECTDIR}/src/jni/methods_impl.o \
	${OBJECTDIR}/src/jni/utilf.o \
	${OBJECTDIR}/src/win/threads.o \
	${OBJECTDIR}/src/win/dtest_win.o \
	${OBJECTDIR}/src/win/winapi_ops.o \
	${OBJECTDIR}/src/win/stdafx.o \
	${OBJECTDIR}/src/unix/dtest_unix.o

# C Compiler Flags
CFLAGS=-mno-cygwin

# CC Compiler Flags
CCFLAGS=-mno-cygwin -Wl,--kill-at
CXXFLAGS=-mno-cygwin -Wl,--kill-at

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-winnt-Release.mk dist/winnt-Release/MinGW-Windows/dtest.dll

dist/winnt-Release/MinGW-Windows/dtest.dll: ${OBJECTFILES}
	${MKDIR} -p dist/winnt-Release/MinGW-Windows
	${LINK.cc} -shared -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/dtest.dll -fPIC ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/unix/unix_ops.o: nbproject/Makefile-${CND_CONF}.mk src/unix/unix_ops.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/unix
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/unix/unix_ops.o src/unix/unix_ops.cpp

${OBJECTDIR}/src/jni/methods_impl.o: nbproject/Makefile-${CND_CONF}.mk src/jni/methods_impl.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/jni
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/jni/methods_impl.o src/jni/methods_impl.cpp

${OBJECTDIR}/src/jni/utilf.o: nbproject/Makefile-${CND_CONF}.mk src/jni/utilf.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/jni
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/jni/utilf.o src/jni/utilf.cpp

${OBJECTDIR}/src/win/threads.o: nbproject/Makefile-${CND_CONF}.mk src/win/threads.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/threads.o src/win/threads.cpp

${OBJECTDIR}/src/win/dtest_win.o: nbproject/Makefile-${CND_CONF}.mk src/win/dtest_win.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/dtest_win.o src/win/dtest_win.cpp

${OBJECTDIR}/src/win/winapi_ops.o: nbproject/Makefile-${CND_CONF}.mk src/win/winapi_ops.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/winapi_ops.o src/win/winapi_ops.cpp

${OBJECTDIR}/src/win/stdafx.o: nbproject/Makefile-${CND_CONF}.mk src/win/stdafx.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/stdafx.o src/win/stdafx.cpp

${OBJECTDIR}/src/unix/dtest_unix.o: nbproject/Makefile-${CND_CONF}.mk src/unix/dtest_unix.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/unix
	${RM} $@.d
	$(COMPILE.cc) -O2 -D_WIN32_WINNT=0x0501 -I\"${JDK_HOME}\include\" -I\"${JDK_HOME}\include\win32\" -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/unix/dtest_unix.o src/unix/dtest_unix.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/winnt-Release
	${RM} dist/winnt-Release/MinGW-Windows/dtest.dll

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
