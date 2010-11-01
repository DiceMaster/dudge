#pragma once

#include "ECheckResult.h"

class ICheckerListener
{
public:
	virtual void OnCheckResult(User *from, int iTask, ECheckResult result, int iTest, int error) = 0;

};
