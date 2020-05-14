package serialization;

import java.util.HashMap;

public class WindowDescriptor {
	public int x;
	public int y;
	public int height;
	public int width;
	public boolean isIcon;
	public boolean isMaximum;
	public HashMap<String, Object> windowInfo;
	public String fileName;
	
	public WindowDescriptor(int x, int y, int height, int width, 
			boolean isIcon, boolean isMaximum, HashMap<String, Object> windowInfo, String fileName)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.isIcon = isIcon;
		this.isMaximum = isMaximum;
		this.windowInfo = windowInfo;
		this.fileName = fileName;
	}
}
