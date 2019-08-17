package com.challenge.spring;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Navigation {
    JSONObject navigation;

    public JSONObject getNavigation(){
        return this.navigation;
    }

    public void setNavigation(String filename) {
        JSONParser jsonParser = new JSONParser();
        Object obj = new Object();
        JSONObject navigationObject = new JSONObject();
        JSONArray childrenArray = new JSONArray();
        try {
            FileReader reader = new FileReader(filename);
            obj = jsonParser.parse(reader);
            //The root object of navigation
            navigationObject = (JSONObject) obj;

        }catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        catch(ParseException e){e.printStackTrace();}

        this.navigation = navigationObject;
    }

    public JSONObject searchNavigation(String navId){
        JSONObject navObj = this.getNavigation();
        JSONObject newNavObj = new JSONObject();

        String id = navId;

        //the base object and children always added
        newNavObj = addJsonObjectforRoot(navObj);
        if(id.equals((navObj.get("id")))){
            return newNavObj;
        }

        JSONArray childArray = (JSONArray) navObj.get("children");
        JSONArray childArrayAgain = searchChildArray(childArray, navId);

        nameofChildElements(childArray);

        newNavObj.put("children", childArrayAgain);

        return newNavObj;
    }

    public void nameofChildElements(JSONArray array){
        JSONObject childObjects = new JSONObject();
        for(int i =0; i < array.size(); i++){
            childObjects = (JSONObject) array.get(i);
            String id = (String) childObjects.get("id");
            System.out.println("root child ids: " + id);
        }
    }

    public JSONObject addJsonObjectforRoot(JSONObject navObj){
        JSONObject newObject =  new JSONObject();
        JSONArray newArray = new JSONArray();
        JSONArray childArray = (JSONArray) navObj.get("children");

        String id = (String) navObj.get("id");
        String name = (String) navObj.get("name");
        String url = (String) navObj.get("url");

        for(int i = 0; i < childArray.size(); i++){
            JSONObject addObj = addJsonObjectWithoutChildren((JSONObject) childArray.get(i));
            newArray.add(addObj);
        }

        newObject.put("id", id);
        newObject.put("name", name);
        newObject.put("url", url);
        newObject.put("children", newArray);

        return newObject;
    }

    public JSONObject addJsonObjectWithoutChildren(JSONObject navObj){
        JSONObject newObject =  new JSONObject();
        JSONArray newArray = new JSONArray();
        String id = (String) navObj.get("id");
        String name = (String) navObj.get("name");
        String url = (String) navObj.get("url");

        newObject.put("id", id);
        newObject.put("name", name);
        newObject.put("url", url);
        newObject.put("children", newArray);

        return newObject;
    }

    public JSONArray searchChildArray(JSONArray childArray, String navId){
        JSONArray childArr = (JSONArray) childArray;
        JSONArray newChildArr = new JSONArray();
        Integer count = 0;
        for(int i = 0; i < childArr.size(); i++){
             JSONObject childObject = (JSONObject) childArr.get(i);
             String id = (String) childObject.get("id");
             if(id.equals(navId)){
                 count = i;
                 break;
             }
        }
        for(int j = count; j > -1; j--){
            JSONObject newChildObj = (JSONObject) childArr.get(j);
        //    System.out.println("Here is the child object: "+ newChildObj);
            newChildArr.add(newChildObj);
        }

        return newChildArr;
    }

    public Navigation (){
        setNavigation("./src/main/resources/navigation.json");
        System.out.println("Json file was loaded");
    }

}
