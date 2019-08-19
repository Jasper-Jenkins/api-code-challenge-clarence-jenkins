package com.challenge.spring;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NavigationController {
    Navigation nav = new Navigation();

    @RequestMapping
    public JSONObject jsonObject(@RequestParam(value = "id", defaultValue = "root") String id) {
        JSONObject newNavObj = new JSONObject();
        //JSONObject navObj = nav.getNavigation();
       // String userInput = id;
        System.out.println("Displaying what the user inputs: " + id);
        newNavObj = nav.searchNavigation(id);
        return newNavObj;
    }

}

