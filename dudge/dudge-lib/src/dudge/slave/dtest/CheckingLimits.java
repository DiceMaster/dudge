package dudge.slave.dtest;

import java.io.Serializable;

/**
 *  !!!Этот класс используется в JNI-части системы!!!
 *  Класс ограничений на среду выполнения задачи.
 *  @author Vladimir Shabanov
 */
public class CheckingLimits implements Serializable, Cloneable
{
	public int memoryLimit = 64 * 1024 * 1024; // bytes
	public int cpuTimeLimit = 1000; // milliseconds
	public int realTimeLimit = 10000; // milliseconds
	public int outputLimit = 1024 * 1024; // bytes
	public int processLimit = 1;

	public CheckingLimits() {
	}
	
	public boolean equals(Object otherObject)
	{
		if (otherObject == null)
		{
			return false;
		}
		
		if (getClass() != otherObject.getClass())
		{
			return false;
		}
		
		if (otherObject == this)
		{
			return true;
		}
		
		CheckingLimits other = (CheckingLimits) otherObject;
		
		return memoryLimit == other.memoryLimit && cpuTimeLimit == other.cpuTimeLimit &&
			realTimeLimit == other.realTimeLimit && outputLimit == other.outputLimit &&
			processLimit == other.processLimit;
	}

	public String toString()
	{
		StringBuffer result = new StringBuffer(getClass().getName());
		
		result.append("[memoryLimit=").append(memoryLimit).
				append(",cputimeLimit=").append(cpuTimeLimit).
				append(",realTimeLimit=").append(realTimeLimit).
				append(",outputLimit=").append(outputLimit).
				append("processLimit=").append(processLimit).
				append("]");
		
		return result.toString();
	}

	public int hashCode()
	{
		return 3 * memoryLimit + 5 * cpuTimeLimit + 7 * processLimit +
				11 * realTimeLimit + 13 * outputLimit;
	}

	protected Object clone() throws CloneNotSupportedException
	{
		CheckingLimits cloned = (CheckingLimits) super.clone();
		
		return cloned;
	}
}
