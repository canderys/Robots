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
    public void save(String filename, GameFieldInfo gameInfo)
    {
        JSONObject mainObj = new JSONObject();
        mainObj.put(JSONKey.GameField.key, prepareGameFieldInfo(gameInfo));

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
        mainObj.put(JSONKey.LogField.key, prepareLogFieldInfo(logInfo));

        try(FileWriter file = new FileWriter(filename))
        {
            file.write(mainObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMainFrame(String filename, FieldInfo mainFrameInfo)
    {
        JSONObject mainObj = new JSONObject();
        mainObj.put(JSONKey.MainFrame.key, prepareFieldInfo(mainFrameInfo));

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
            JSONObject gameObj = (JSONObject) mainObj.get(JSONKey.GameField.key);
            FieldInfo fieldInfo = parseFieldInfo(gameObj);
            double xRobot = (double) gameObj.get(JSONKey.RobotX.key);
            double yRobot = (double) gameObj.get(JSONKey.RobotY.key);
            double direction = (double) gameObj.get(JSONKey.Direction.key);
            double xTarget = (double) gameObj.get(JSONKey.TargetX.key);
            double yTarget = (double) gameObj.get(JSONKey.TargetY.key);
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
            JSONObject logObj = (JSONObject) mainObj.get(JSONKey.LogField.key);
            FieldInfo fieldInfo = parseFieldInfo(logObj);

            LinkedList<LogEntry> logData = new LinkedList<>();
            JSONArray logInfo = (JSONArray) logObj.get(JSONKey.LogInfo.key);
            Iterator<JSONArray> iter = logInfo.iterator();
            while (iter.hasNext())
            {
                JSONArray pair = iter.next();
                LogLevel level = LogLevel.getTypeByValue(((Long)pair.get(0)).intValue());
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

    public FieldInfo loadMainFrameInfo(String filename)
    {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filename))
        {
            JSONObject mainObj = (JSONObject) parser.parse(reader);
            JSONObject mainFrameObj = (JSONObject) mainObj.get(JSONKey.MainFrame.key);
            return parseFieldInfo(mainFrameObj);

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
        fieldObj.put(JSONKey.FieldX.key, info.xCoord);
        fieldObj.put(JSONKey.FieldY.key, info.yCoord);
        fieldObj.put(JSONKey.Height.key, info.height);
        fieldObj.put(JSONKey.Width.key, info.width);
        fieldObj.put(JSONKey.IsMaximal.key, info.isMaximised);
        fieldObj.put(JSONKey.IsIcon.key, info.isIcon);
        return fieldObj;
    }

    private JSONObject prepareGameFieldInfo(GameFieldInfo gameFieldInfo)
    {
        JSONObject gameFieldObj = prepareFieldInfo(gameFieldInfo);
        gameFieldObj.put(JSONKey.RobotX.key, gameFieldInfo.xRobot);
        gameFieldObj.put(JSONKey.RobotY.key, gameFieldInfo.yRobot);
        gameFieldObj.put(JSONKey.Direction.key, gameFieldInfo.robotDirection);
        gameFieldObj.put(JSONKey.TargetX.key, gameFieldInfo.xTarget);
        gameFieldObj.put(JSONKey.TargetY.key, gameFieldInfo.yTarget);
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
        logFieldObj.put(JSONKey.LogInfo.key, logData);
        return logFieldObj;
    }

    private FieldInfo parseFieldInfo(JSONObject fieldObj)
    {
        Long x = (Long)fieldObj.get(JSONKey.FieldX.key);
        Long y = (Long)fieldObj.get(JSONKey.FieldY.key);
        Long height = (Long)fieldObj.get(JSONKey.Height.key);
        Long width = (Long)fieldObj.get(JSONKey.Width.key);
        boolean isMaximal = (boolean)fieldObj.get(JSONKey.IsMaximal.key);
        boolean isIcon = (boolean)fieldObj.get(JSONKey.IsIcon.key);
        return new FieldInfo(x.intValue(), y.intValue(), height.intValue(), width.intValue(), isIcon, isMaximal);
    }
}
