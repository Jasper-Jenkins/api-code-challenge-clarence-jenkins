package com.challenge.spring;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

class NavigationTest {
    private String jsonStringRoot = "./src/test/resources/navigation.json";
    private String jsonStringCalves = "./src/test/resources/validIdCalves.json";
    private JSONObject navigationJsonRootObject;
    private JSONObject navigationJsonCalveObject;
    private String validId = "Calves";
    private String invalidId = "Cheeseburgers";
    Navigation rootNavigation = new Navigation();
    Navigation calveNavigation = new Navigation();

    @Before
    public void getRootNav() {
        rootNavigation.setNavigation(jsonStringRoot);
        navigationJsonRootObject = rootNavigation.getNavigation();
    }

    @Before
    public void getCalveNav(){
        calveNavigation.setNavigation(jsonStringCalves);
        navigationJsonCalveObject = calveNavigation.getNavigation();
    }

    @Test
    void createObjectWithValidId() {
        JSONObject calveNavObject = Navigation.createRootObjectAndChildArray(navigationJsonCalveObject, validId);
        assertEquals(calveNavigation,calveNavObject);
    }

/*
    @Test //not sure how to test this yet as I currently get a valid root.(children) object no matter the input
    void createObjectWithInvalidId() {
        JSONObject NavObject = Navigation.createRootObjectAndChildArray(navigationJsonRootObject, invalidId);

    }*/
}