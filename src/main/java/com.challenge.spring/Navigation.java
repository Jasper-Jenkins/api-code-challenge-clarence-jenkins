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
    JSONObject alteredNavigation;
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
        //root of json
        JSONObject navObj = this.getNavigation();

        //will become root with child elements
        JSONObject newNavObj = new JSONObject();

        newNavObj = createRootObjectAndChildArray(navObj, navId);
        return newNavObj;
    }

    public JSONObject createRootObjectAndChildArray(JSONObject navigationObject, String navigationId){
        JSONArray childrenOfRootObject = (JSONArray) navigationObject.get("children");
        JSONObject newRootObjectWithChildren = new JSONObject();
        //IF Id matches root Id
        if(navigationId.equals(navigationObject.get("id"))){
            System.out.println("The id you input was ---ROOT---");
            newRootObjectWithChildren.put("id", navigationObject.get("id"));
            newRootObjectWithChildren.put("name", navigationObject.get("name"));
            newRootObjectWithChildren.put("url", navigationObject.get("url"));
            newRootObjectWithChildren.put("children", createChildArrayFromChildrenOfObject(childrenOfRootObject, navigationId));
            return newRootObjectWithChildren;
        }
        //searching through the children of root
        for(int i = 0; i < childrenOfRootObject.size(); i++){
            JSONObject objectIdCheck = new JSONObject();
            objectIdCheck = (JSONObject) childrenOfRootObject.get(i);
            //if Id matches any of the root children Id
            if(navigationId.equals(objectIdCheck.get("id"))){
                System.out.println("The id you input was ---One of the ROOTS children---");
                newRootObjectWithChildren.put("id", navigationObject.get("id"));
                newRootObjectWithChildren.put("name", navigationObject.get("name"));
                newRootObjectWithChildren.put("url", navigationObject.get("url"));
                newRootObjectWithChildren.put("children", createChildArrayFromChildrenOfObject(childrenOfRootObject, navigationId));
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

    //Use when id matches the root id or its childrens id
    public JSONArray createChildArrayFromChildrenOfObject(JSONArray childrenArray, String navigationId){
        JSONArray newChildArrayForObject = new JSONArray();
        for(int i = 0; i < childrenArray.size(); i++) {
            JSONObject newObjectForChildArray = new JSONObject();
            //create object to extract values
            JSONObject newObject = new JSONObject();
            newObject = (JSONObject) childrenArray.get(i);
            System.out.println("CHILD OF --ROOT-- object");
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
    //so we need to check the child elements of each of those objects [store, training_main, articles-training, bodyspace, member-overview my home]
    public JSONArray createObjectFromChildArray(JSONArray childrenArray, String navigationId){
        JSONArray newChildArrayForObject = new JSONArray();

        for(int i = 0; i < childrenArray.size(); i++) {
            JSONObject newObjectForChildArray = new JSONObject();
            //  create object to extract values
            JSONObject newObject = new JSONObject();
            //  individual object from the passed in array
            newObject = (JSONObject) childrenArray.get(i);
            //  add values to the object that will be added to the return array
            newObjectForChildArray.put("id", newObject.get("id"));
            newObjectForChildArray.put("name", newObject.get("name"));
            newObjectForChildArray.put("url", newObject.get("url"));
            //  checking the child elements of the object. Will return a blank array, or array with a single object matching id,
            //  or array with a single object with child element that has matching id.
            newObjectForChildArray.put("children", createArrayWithObjectMatchingId((JSONArray) newObject.get("children"), navigationId, 1));
            //  newObjectForChildArray.put("children", new JSONArray()); //add blank array so children are not present
            //  add to object to the return array
            newChildArrayForObject.add(newObjectForChildArray);
        }
        return newChildArrayForObject;
    }



    //searching through child array of the roots child elements    [store.children[],training-main.children[find-a-plan],...]
    //level can be removed as its on there to track at what level in the object we are at

    public JSONArray createArrayWithObjectMatchingId(JSONArray childArray, String navigationId, Integer level){
        JSONObject objectWithMatchingId = new JSONObject();
      System.out.println("Level: "+ level);
        for(int i = 0; i < childArray.size(); i++) {
            JSONArray childArrayWithObjectMatchingId = new JSONArray();
            JSONObject childObject = new JSONObject();
            childObject = (JSONObject) childArray.get(i);
            if (navigationId.equals(childObject.get("id"))) {
                objectWithMatchingId.put("id", childObject.get("id"));
                objectWithMatchingId.put("name", childObject.get("name"));
                objectWithMatchingId.put("url", childObject.get("url"));
                objectWithMatchingId.put("children", new JSONArray());
                childArrayWithObjectMatchingId.add(objectWithMatchingId);
                return childArrayWithObjectMatchingId;
            }
        }
        for(int j = 0; j < childArray.size(); j++){
            JSONArray childArrayWithObjectMatchingId = new JSONArray();
            Integer release = j;
            JSONObject childObject = new JSONObject();
            childObject = (JSONObject) childArray.get(j);
            if (!(navigationId.equals(childObject.get("id")))) {

                objectWithMatchingId.put("id", childObject.get("id"));
                objectWithMatchingId.put("name", childObject.get("name"));
                objectWithMatchingId.put("url", childObject.get("url"));
                Integer count = level + 1;
                objectWithMatchingId.put("children", createArrayWithObjectMatchingId((JSONArray) childObject.get("children"), navigationId, count));
                childArrayWithObjectMatchingId.add(objectWithMatchingId);
                return childArrayWithObjectMatchingId; //this is causing problems with being able to access further child elements need to rework
            }

        }
        return new JSONArray();
    }

    public Navigation (){
        setNavigation("./src/main/resources/navigation.json");
        System.out.println("Json file was loaded");
    }

}
