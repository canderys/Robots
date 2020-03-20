package serialization;

public enum JSONKey
{
    FieldX("fieldX"),
    FieldY("fieldY"),
    Width("width"),
    Height("height"),
    IsMaximal("isMaximal"),
    IsIcon("isIcon"),
    RobotX("robotX"),
    RobotY("robotY"),
    Direction("robotDirection"),
    TargetX("targetX"),
    TargetY("targetY"),
    LogInfo("logInfo"),
    LogField("LogField"),
    GameField("GameField"),
    MainFrame("MainFrame");

    public String key;

    JSONKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

}
