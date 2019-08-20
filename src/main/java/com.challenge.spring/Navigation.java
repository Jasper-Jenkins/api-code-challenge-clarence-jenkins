package com.challenge.spring;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
@SuppressWarnings("unchecked")
public class Navigation {
    private static JSONObject navigation;
    private static boolean found;

    public boolean getFound(){ return this.found;}
    public static void setFound(boolean alterFound){ found = alterFound;}

    public static JSONObject getNavigation(){
        return navigation;
    }

    public static void setNavigation(String filename) {
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

        navigation = navigationObject;
        System.out.println("Json file was loaded");
    }

    public static JSONObject searchNavigation(String navigationId){

        JSONObject navObj = getNavigation(); //root of json
        setFound(false); //This was life changing
        JSONObject newNavObj = new JSONObject(); //will become the new object that returns
        newNavObj = createRootObjectAndChildArray(navObj, navigationId);

        return newNavObj;
    }

    public static JSONObject createRootObjectAndChildArray(JSONObject navigationObject, String navigationId){

            JSONArray childrenOfRootObject = new JSONArray();
            childrenOfRootObject = (JSONArray) navigationObject.get("children");
            JSONObject newRootObjectWithChildren = new JSONObject();

            //If Id matches root id
            if(navigationId.equals(navigationObject.get("id"))){
                newRootObjectWithChildren.put("id", navigationObject.get("id"));
                newRootObjectWithChildren.put("name", navigationObject.get("name"));
                newRootObjectWithChildren.put("url", navigationObject.get("url"));
                newRootObjectWithChildren.put("children", createChildArrayFromChildrenOfRoot(childrenOfRootObject, navigationId));
                return newRootObjectWithChildren;
            }

            //if Id matches any of the root children id's
            for(int i = 0; i < childrenOfRootObject.size(); i++){
                JSONObject objectIdCheck = new JSONObject();
                objectIdCheck = (JSONObject) childrenOfRootObject.get(i);
            /*
                Had to lowercase these because user input from the url was formatted to match the object id
                (user input of TRAINING_MAIN would convert to Training_Main without me causing the conversion)
                all of the root children object id's [store, Training_Main,...] would convert automatically to their respective values except "My Home"
                "My Home" would always come through as "my home" no matter how it was input in the url.
                Had to do this later on as well to catch same formatting issue.
            */
                if(navigationId.toLowerCase().equals(((String) objectIdCheck.get("id")).toLowerCase())){
                    newRootObjectWithChildren.put("id", navigationObject.get("id"));
                    newRootObjectWithChildren.put("name", navigationObject.get("name"));
                    newRootObjectWithChildren.put("url", navigationObject.get("url"));
                    newRootObjectWithChildren.put("children", createChildArrayFromChildrenOfRoot(childrenOfRootObject, navigationId));
                    return newRootObjectWithChildren;
                }
            }
            //if id doesn't match root or its children's id start building the object
            newRootObjectWithChildren.put("id", navigationObject.get("id"));
            newRootObjectWithChildren.put("name", navigationObject.get("name"));
            newRootObjectWithChildren.put("url", navigationObject.get("url"));
            //passing in the array of child elements from the root
            newRootObjectWithChildren.put("children", createObjectFromChildArray(childrenOfRootObject, navigationId));
            return newRootObjectWithChildren;
        }






    //Use when id matches the root id or the root childrens id
    public static JSONArray createChildArrayFromChildrenOfRoot(JSONArray childrenArray, String navigationId){
        JSONArray newChildArrayForObject = new JSONArray();

        for(int i = 0; i < childrenArray.size(); i++) {
            JSONObject newObjectForChildArray = new JSONObject();
            //create object to extract values
            JSONObject newObject = new JSONObject();
            newObject = (JSONObject) childrenArray.get(i);
            //add values to the object that will be added to the return array
            newObjectForChildArray.put("id", newObject.get("id"));
            newObjectForChildArray.put("name", newObject.get("name"));
            newObjectForChildArray.put("url", newObject.get("url"));
            newObjectForChildArray.put("children", new JSONArray()); //add blank array so children are not present
            //add to object to the return array
            newChildArrayForObject.add(newObjectForChildArray);
        }
        return newChildArrayForObject;
    }

    //The children from the root element are being passed in, and at this point none of those objects in the array have matching id
    //so we need to check the child elements of each of those objects [store, training_main, articles-training, bodyspace, member-overview, my home]
    public static JSONArray createObjectFromChildArray(JSONArray childrenArray, String navigationId) {
        JSONArray newChildArrayForObject = new JSONArray();
            for (int i = 0; i < childrenArray.size(); i++) {
                JSONObject newObjectForChildArray = new JSONObject();
                JSONObject newObject = new JSONObject();
                newObject = (JSONObject) childrenArray.get(i);
                newObjectForChildArray.put("id", newObject.get("id"));
                newObjectForChildArray.put("name", newObject.get("name"));
                newObjectForChildArray.put("url", newObject.get("url"));
                //  checking the child elements of the object. Will return a blank array, or array with a single object matching id,
                //  or array with a single object with child elements that has matching id.
                newObjectForChildArray.put("children", createArrayWithObjectMatchingId((JSONArray) newObject.get("children"), navigationId));
                newChildArrayForObject.add(newObjectForChildArray);
            }
            return newChildArrayForObject;
    }

    public static JSONArray createArrayWithObjectMatchingId(JSONArray childArray, String navigationId){

        JSONArray returnChildArray = new JSONArray();

        for(int i = 0; i < childArray.size(); i++){
            JSONObject childObject = new JSONObject();
            JSONArray childrenArray = new JSONArray();
            JSONArray childrenArrayWithChildren = new JSONArray();

            if(!found){
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) childArray.get(i);
                if(!(navigationId.toLowerCase().equals(((String) jsonObject.get("id")).toLowerCase()))){
                    JSONArray areMoreChildrenPresent = (JSONArray) jsonObject.get("children");
                    if(areMoreChildrenPresent.size() > 0) {
                        childrenArrayWithChildren = createArrayWithObjectMatchingId(areMoreChildrenPresent, navigationId);
                        childObject.put("id", jsonObject.get("id"));
                        childObject.put("name", jsonObject.get("name"));
                        childObject.put("url", jsonObject.get("url"));
                        childObject.put("children", childrenArrayWithChildren);
                        returnChildArray.add(childObject);
                    }
                }
                if((navigationId.toLowerCase().equals(((String) jsonObject.get("id")).toLowerCase()))){
                    childObject.put("id", jsonObject.get("id"));
                    childObject.put("name", jsonObject.get("name"));
                    childObject.put("url", jsonObject.get("url"));
                    childObject.put("children", new JSONArray());
                    childrenArray.add(childObject);
                    setFound(true);
                    return childrenArray;
                }
                if(!found){
                    returnChildArray.removeAll(returnChildArray);
                }
            }
        }
        return returnChildArray;
    }


    public Navigation (){
        setFound(false);
        setNavigation("./src/main/resources/navigation.json");
    }

}
