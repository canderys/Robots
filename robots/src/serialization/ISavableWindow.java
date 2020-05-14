package serialization;

import java.util.HashMap;

public interface ISavableWindow  {
	public int getX();
	public int getY();
	public int getHeight();
	public int getWidth();
	public boolean isIcon();
	public boolean isMaximised();
	public HashMap<String, Object> getWindowInfo();
	public String getFileName();
	
	public default WindowDescriptor getWindowDescriptor()
	{
		return new WindowDescriptor(this.getX(), this.getY(), 
				this.getHeight(), this.getWidth(), this.isIcon(), this.isMaximised(), 
				this.getWindowInfo(), this.getFileName());
	}
}
