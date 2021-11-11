package dtu.group42.server.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class JsonHelper {
    public static JSONObject load(String filename) throws FileNotFoundException{
        var file = new File(filename);
        System.out.println(file.getAbsolutePath());

        var scanner = new Scanner(file);
        var sb = new StringBuilder();
        while (scanner.hasNext())
            sb.append(scanner.nextLine());
        scanner.close();

        var jsonObj = new JSONObject(sb.toString());
        return jsonObj;
    }

    public static List<String> getStringList(JSONObject obj, String key){
        return obj.getJSONArray(key)
        .toList()
        .stream()
        .map(x -> x.toString())
        .collect(Collectors.toList());
    }
}
