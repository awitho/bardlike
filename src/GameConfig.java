import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Generic config file the game, basically just a wrapper.
 * 
 * @version 0.1
 * @since 5/20/13
 * @author 13thompsona
 */
public class GameConfig {
    public final static String pathPrefix = "conf/"; // Since everything is going to be in the same directory, static this!
    public final static Gson json = new Gson(); // Since we don't need multiple copies of this, make one static one to parse everything!
	
    private String filePath = "";
    private JsonObject data;
	private JsonArray aData;
    
    public GameConfig(String confPath) {
        filePath = pathPrefix + confPath;
        BufferedReader reader;
        String str = "";
        
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while (reader.ready()) {
                str += reader.readLine();
            }
        } catch (Exception ex) {
            Misc.showDialog(ex);
            return;
        }

	try {
            data = json.fromJson(str, JsonObject.class);
    } catch (ClassCastException ex) {
			aData = json.fromJson(str, JsonArray.class);
			data = null;
		} catch (Exception ex) {
			Misc.showDialog(ex);
			return;
		}
    }

    public double getValueAsDouble(String key) {
		if (aData != null) { return 0.0; }
        return data.get(key).getAsDouble();
    }
	
	public boolean getValueAsBool(String key) {
		if (aData != null) { return false; }
        return data.get(key).getAsBoolean();
    }
	
	public int getValueAsInt(String key) {
		if (aData != null) { return 0; }
        return data.get(key).getAsInt();
    }
	
	public String getValueAsString(String key) {
		if (aData != null) { return null; }
		return data.get(key).getAsString();
	}
	
	public JsonArray getArray() {
		if (aData != null) {
			return aData.getAsJsonArray();
		}
		return data.getAsJsonArray();
	}
	
	public JsonObject getSubObject(String key) {
		if (aData != null) { return null; }
		JsonElement ele = data.get(key);
		if (!ele.isJsonObject()) { return null; }
		return ele.getAsJsonObject();
	}
	
	public JsonArray getSubArray(String key) {
		if (aData != null) { return null; }
		JsonElement ele = data.get(key);
		if (!ele.isJsonArray()) { return null; }
		return ele.getAsJsonArray();
	}

    public void dumpJson() {
       System.out.println(data.toString());
    }
}