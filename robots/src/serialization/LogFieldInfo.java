package serialization;

import log.LogEntry;
import java.util.LinkedList;

public class LogFieldInfo extends FieldInfo
{
    public final LogEntry[] logInfo;

    public LogFieldInfo(int x, int y, int height, int width, boolean isIcon,
                        boolean isMaximised, LinkedList<LogEntry> logInfo)
    {
        super(x, y, height, width, isIcon, isMaximised);
        this.logInfo = logInfo.toArray(new LogEntry[0]);
    }
}
