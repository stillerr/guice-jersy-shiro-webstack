package com.nil.backend.user;


import java.util.ArrayList;
import java.util.List;

public class UserService {
    public List<String> getUsers() {
        ArrayList<String> users = new ArrayList<String>();
        users.add("user 1");
        users.add("user 2");
        users.add("user 3");
        return users;
    }
}
