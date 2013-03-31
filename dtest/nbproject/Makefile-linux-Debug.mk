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
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_DLIB_EXT=so
CND_CONF=linux-Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/jni/methods_impl.o \
	${OBJECTDIR}/src/unix/unix_ops.o \
	${OBJECTDIR}/src/win/dtest_win.o \
	${OBJECTDIR}/src/win/threads.o \
	${OBJECTDIR}/src/win/stdafx.o \
	${OBJECTDIR}/src/unix/dtest_unix.o \
	${OBJECTDIR}/src/win/winapi_ops.o \
	${OBJECTDIR}/src/jni/utilf.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-fvisibility=hidden
CXXFLAGS=-fvisibility=hidden

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/libdtest.${CND_DLIB_EXT}

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/libdtest.${CND_DLIB_EXT}: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -shared -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/libdtest.${CND_DLIB_EXT} -fPIC ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/jni/methods_impl.o: src/jni/methods_impl.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/jni
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/jni/methods_impl.o src/jni/methods_impl.cpp

${OBJECTDIR}/src/unix/unix_ops.o: src/unix/unix_ops.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/unix
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/unix/unix_ops.o src/unix/unix_ops.cpp

${OBJECTDIR}/src/win/dtest_win.o: src/win/dtest_win.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/dtest_win.o src/win/dtest_win.cpp

${OBJECTDIR}/src/win/threads.o: src/win/threads.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/threads.o src/win/threads.cpp

${OBJECTDIR}/src/win/stdafx.o: src/win/stdafx.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/stdafx.o src/win/stdafx.cpp

${OBJECTDIR}/src/unix/dtest_unix.o: src/unix/dtest_unix.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/unix
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/unix/dtest_unix.o src/unix/dtest_unix.cpp

${OBJECTDIR}/src/win/winapi_ops.o: src/win/winapi_ops.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/win
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/win/winapi_ops.o src/win/winapi_ops.cpp

${OBJECTDIR}/src/jni/utilf.o: src/jni/utilf.cpp 
	${MKDIR} -p ${OBJECTDIR}/src/jni
	${RM} $@.d
	$(COMPILE.cc) -g -I/usr/include/c++/4.7.2 -I/usr/java/latest/include/linux -I/usr/java/latest/include -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/jni/utilf.o src/jni/utilf.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/libdtest.${CND_DLIB_EXT}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
