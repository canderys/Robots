package serialization;

public class LoadStatus {
	private boolean load = false;
	private boolean response = false;
	
	public void setLoad(boolean a)
	{
		load = a;
		response = true;
	}
	
	public boolean getLoad()
	{
		return load;
	}
	
	public boolean getResponse()
	{
		return response;
	}
}
