package serialization;

import java.util.HashMap;
import java.util.List;

public interface WindowsSaver {
	public void save();
	public WindowDescriptor load(String fileName);
	public String getFileNameWindowById(String id);
	public void registerWindow(ISavableWindow window, String id);
	public boolean isSavesExits();
}
