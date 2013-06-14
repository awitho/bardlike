import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class NameGenerator {
	private static JsonObject itemNames;
	
	public static void setNames(JsonObject names) {
	//	System.out.println(names);
		itemNames = names;
	}
	
	public static String generateName(String type) {
	//	System.out.println(itemNames);
		JsonObject subnames = itemNames.get(type).getAsJsonObject();
		String prefix = subnames.get("prefix").getAsJsonArray().get((int) (Math.random() * subnames.get("prefix").getAsJsonArray().size())).getAsString();
		String midname = subnames.get("midname").getAsJsonArray().get((int) (Math.random() * subnames.get("midname").getAsJsonArray().size())).getAsString();
		String suffix = subnames.get("suffix").getAsJsonArray().get((int) (Math.random() * subnames.get("suffix").getAsJsonArray().size())).getAsString();
		return prefix + " " + midname + " " + suffix;
	}

}
