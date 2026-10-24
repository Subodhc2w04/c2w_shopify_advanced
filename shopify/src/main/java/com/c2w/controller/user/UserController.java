package com.c2w.controller.user;

import java.util.List;

import com.c2w.dao.User.UserData;
import com.c2w.model.user.Address;
import com.c2w.model.user.User;
import com.c2w.view.authentication.AuthPage;

public class  UserController {

    UserData userdao = new UserData();
    static String user_email = AuthPage.email;

    public void addUser(String fullName,
            String email,
            String mobile,
            String gender,
            final List<Address> addresses
            ) {

        User user = new User(fullName, email, mobile, gender,addresses);

        userdao.addUser(user);

    }
    public void updateUser(String fullName,
            String email,
            String mobile,
            String gender,
            final List<Address> addresses
            ) {

        
        User user = new User(fullName, AuthPage.email, mobile, gender,addresses);
        System.out.println("address in controller"+addresses);

        userdao.updateUser(user);

    }
    public User getUser(String user_email) {
        System.out.println("user email in controller:"+user_email);
        return userdao.getUser(user_email);
        
    }
    

}
