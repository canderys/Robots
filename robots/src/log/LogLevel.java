package log;

import java.util.HashMap;
import java.util.Map;

public enum LogLevel
{
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private int m_iLevel;
    private static final Map<Integer, LogLevel> typesByValue = new HashMap<>();
    static {
        for (LogLevel type : LogLevel.values()) {
            typesByValue.put(type.level(), type);
        }
    }

    private LogLevel(int iLevel)
    {
        m_iLevel = iLevel;
    }
    
    public int level()
    {
        return m_iLevel;
    }

    public static LogLevel getTypeByValue(int value)
    {
        return typesByValue.get(value);
    }
}

