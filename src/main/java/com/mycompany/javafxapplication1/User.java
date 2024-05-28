package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class User {
    private SimpleStringProperty user;
    private SimpleStringProperty pass;

    User(String user, String pass) {
        this.user = new SimpleStringProperty(user);
        this.pass = new SimpleStringProperty(pass);
    }

    public String getUser() {
        return user.get();
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getPass() {
        return pass.get();
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }
}
