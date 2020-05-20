package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class LogWindowSource
{
    private int m_iQueueLength;	
    
    private final LinkedList<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new LinkedList<LogEntry>();
        m_listeners = new ArrayList<LogChangeListener>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (m_messages)
        {
            m_messages.add(entry);
            if (m_messages.size() > m_iQueueLength)
            {
                m_messages.removeFirst();
            }
        }
        synchronized (m_listeners)
        {
            for (LogChangeListener listener : m_listeners)
            {
                listener.onLogChanged();
            }
        }
    }
    
    public int size()
    {
        //return m_messages.size();
    	return this.m_listeners.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }
}
