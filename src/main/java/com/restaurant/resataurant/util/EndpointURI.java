package com.restaurant.resataurant.util;

public final class EndpointURI {

    public static final String SLASH = "/";
    public static final String ID = "{id}";
    public static final String UPDATE = "update";


    //USER APIS
    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String GET_USER_BY_ID = USER + SLASH + ID;

    //AUTHENTICATION APIS

    public static final String USER_UPDATE = USER + SLASH + ID + SLASH + UPDATE;
    public static final String USER_BY_ID = USER + SLASH + ID;

    private EndpointURI() {
    }
}
