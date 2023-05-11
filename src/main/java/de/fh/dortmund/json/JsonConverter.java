package de.fh.dortmund.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;

public class JsonConverter {

    public static String jsonArrayToString(JsonArray jsonArray){
        return new Gson().toJson(jsonArray);
    }

}
