package serialization;

import log.LogEntry;
import log.LogLevel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;

public class JSONSaveLoader extends BasicSaver
{
    public void save(String filename, GameFieldInfo gameInfo, LogFieldInfo logInfo)
    {
        JSONObject mainObj = new JSONObject();
        mainObj.put(JSONKey.GameField, prepareGameFieldInfo(gameInfo));
        mainObj.put(JSONKey.LogField, prepareLogFieldInfo(logInfo));

        try(FileWriter file = new FileWriter(filename))
        {
            file.write(mainObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String filename, GameFieldInfo gameInfo)
    {
        JSONObject mainObj = new JSONObject();
        mainObj.put(JSONKey.GameField, prepareGameFieldInfo(gameInfo));

        try(FileWriter file = new FileWriter(filename))
        {
            file.write(mainObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String filename, LogFieldInfo logInfo)
    {
        JSONObject mainObj = new JSONObject();
        mainObj.put(JSONKey.LogField, prepareLogFieldInfo(logInfo));

        try(FileWriter file = new FileWriter(filename))
        {
            file.write(mainObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameFieldInfo loadGameInfo(String filename)
    {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filename))
        {
            JSONObject mainObj = (JSONObject) parser.parse(reader);
            JSONObject gameObj = (JSONObject) mainObj.get(JSONKey.GameField);
            FieldInfo fieldInfo = parseFieldInfo(gameObj);
            double xRobot = (double) gameObj.get(JSONKey.RobotX);
            double yRobot = (double) gameObj.get(JSONKey.RobotY);
            double direction = (double) gameObj.get(JSONKey.Direction);
            double xTarget = (double) gameObj.get(JSONKey.TargetX);
            double yTarget = (double) gameObj.get(JSONKey.TargetY);
            return new GameFieldInfo(fieldInfo, xRobot, yRobot, direction, xTarget, yTarget);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LogFieldInfo loadLogInfo(String filename)
    {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filename))
        {
            JSONObject mainObj = (JSONObject) parser.parse(reader);
            JSONObject logObj = (JSONObject) mainObj.get(JSONKey.LogField);
            FieldInfo fieldInfo = parseFieldInfo(logObj);

            LinkedList<LogEntry> logData = new LinkedList<>();
            JSONArray logInfo = (JSONArray) logObj.get(JSONKey.LogInfo);
            Iterator<JSONArray> iter = logInfo.iterator();
            while (iter.hasNext())
            {
                JSONArray pair = iter.next();
                LogLevel level = LogLevel.getTypeByValue((int)pair.get(0));
                String message = (String) pair.get(1);
                logData.add(new LogEntry(level, message));
            }
            return new LogFieldInfo(fieldInfo, logData);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject prepareFieldInfo(FieldInfo info)
    {
        JSONObject fieldObj = new JSONObject();
        fieldObj.put(JSONKey.FieldX, info.xCoord);
        fieldObj.put(JSONKey.FieldY, info.yCoord);
        fieldObj.put(JSONKey.Height, info.height);
        fieldObj.put(JSONKey.Width, info.width);
        fieldObj.put(JSONKey.IsMaximal, info.isMaximised);
        fieldObj.put(JSONKey.IsIcon, info.isIcon);
        return fieldObj;
    }

    private JSONObject prepareGameFieldInfo(GameFieldInfo gameFieldInfo)
    {
        JSONObject gameFieldObj = prepareFieldInfo(gameFieldInfo);
        gameFieldObj.put(JSONKey.RobotX, gameFieldInfo.xRobot);
        gameFieldObj.put(JSONKey.RobotY, gameFieldInfo.yRobot);
        gameFieldObj.put(JSONKey.Direction, gameFieldInfo.robotDirection);
        gameFieldObj.put(JSONKey.TargetX, gameFieldInfo.xTarget);
        gameFieldObj.put(JSONKey.TargetY, gameFieldInfo.yTarget);
        return gameFieldObj;
    }

    private JSONObject prepareLogFieldInfo(LogFieldInfo logFieldInfo)
    {
        JSONObject logFieldObj = prepareFieldInfo(logFieldInfo);
        JSONArray logData = new JSONArray();
        for (LogEntry logEntry : logFieldInfo.logInfo)
        {
            JSONArray pair = new JSONArray();
            pair.add(logEntry.getLevel().level());
            pair.add(logEntry.getMessage());
            logData.add(pair);
        }
        logFieldObj.put(JSONKey.LogInfo, logData);
        return logFieldObj;
    }

    private FieldInfo parseFieldInfo(JSONObject fieldObj)
    {
        int x = (int)fieldObj.get(JSONKey.FieldX);
        int y = (int)fieldObj.get(JSONKey.FieldY);
        int height = (int)fieldObj.get(JSONKey.Height);
        int width = (int)fieldObj.get(JSONKey.Width);
        boolean isMaximal = (boolean)fieldObj.get(JSONKey.IsMaximal);
        boolean isIcon = (boolean)fieldObj.get(JSONKey.IsIcon);
        return new FieldInfo(x, y, height, width, isIcon, isMaximal);
    }
}
