package com.challenge.spring;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class NavigationController {
    Navigation nav = new Navigation();

    @RequestMapping
    public JSONObject jsonObject(@RequestParam(value = "id", defaultValue = "root") String id) {
        JSONObject newNavObj = new JSONObject();
        newNavObj = nav.searchNavigation(id);
        return newNavObj;
    }

}

