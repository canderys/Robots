package serialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class JsonWindowsSaver implements WindowsSaver {
	
	private Map<String, String> idFilesPair = new HashMap<String, String>();
	private List<ISavableWindow> savingWindows = new ArrayList<ISavableWindow>();
	private String configFileName = "JsonWindowsSaverConfig";
	
	public JsonWindowsSaver()
	{
		File configFile = new File(configFileName);
		if(configFile.exists())
		{
			Gson gson = new Gson();
			StringBuilder json = new StringBuilder();
	        try(FileReader reader = new FileReader(configFile))
	        {
	            int c;
	            try {
					while((c=reader.read())!=-1){
						json.append((char)c);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} 
	        } catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        idFilesPair = (Map<String, String>)gson.fromJson(json.toString() , Map.class);
		}
	}
	
	
	public void save() {
		Gson gson = new Gson();
		for(ISavableWindow window : savingWindows) {
			String json = gson.toJson(window.getWindowDescriptor());
			try(FileWriter writer = new FileWriter(window.getFileName(), false))
			{
				writer.write(json);
				WindowDescriptor windowDescriptor = gson.fromJson(json, WindowDescriptor.class);
			}
			catch(IOException ex){
             
				System.out.println(ex.getMessage());
			} 
		}
		
		String json = gson.toJson(idFilesPair);
        try(FileWriter writer = new FileWriter(configFileName))
        {
            writer.write(json);
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}
	
	public WindowDescriptor load(String fileName) {
		Gson gson = new Gson();
		StringBuilder json = new StringBuilder();
        try(FileReader reader = new FileReader(fileName))
        {
            int c;
            try {
				while((c=reader.read())!=-1){
					json.append((char)c);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
        } catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        WindowDescriptor windowDescriptor = gson.fromJson(json.toString(), WindowDescriptor.class);
        return windowDescriptor;
	}

	public String getFileNameWindowById(String windowId) {
		return idFilesPair.get(windowId);
	}

	public void registerWindow(ISavableWindow window, String windowId) {
		savingWindows.add(window);
		idFilesPair.put(windowId, window.getFileName());
	}


	@Override
	public boolean isSavesExits() {
		File configFile = new File(configFileName);
		return configFile.exists();
	}
}
