package serialization;

public class FieldInfo
{
    public final boolean isIcon;
    public final boolean isMaximised;
    public final int xCoord;
    public final int yCoord;
    public final int height;
    public final int width;

    public FieldInfo(int x, int y, int height, int width,
                     boolean isIcon, boolean isMaximised )
    {
        this.isIcon = isIcon;
        this.isMaximised = isMaximised;
        xCoord = x;
        yCoord = y;
        this.height = height;
        this.width = width;
    }

    public FieldInfo(FieldInfo info)
    {
        isIcon = info.isIcon;
        isMaximised = info.isMaximised;
        xCoord = info.xCoord;
        yCoord = info.yCoord;
        height = info.height;
        width = info.width;
    }
}
