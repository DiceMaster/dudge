#pragma once

enum ECheckResult
{
	eCheckInternalError,
	eCheckOk,
	eCheckCompileError,
	eCheckWrongAnswer,
	eCheckTimeLimit,
	eCheckMemoryLimit,
	eCheckOutputLimit,
	eCheckProcessLimit,
	eCheckRuntimeError,
};