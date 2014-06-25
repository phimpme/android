package com.phimpme.phimpme;

import java.io.Serializable;

/**
 * Created by sensing on 14-6-24.
 */
public class AccountInfo implements Serializable {
    private String accountCategory;
    private String userName;
    private String passWord;
    private String imagePath;
    private String userUrl;

    public AccountInfo(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getAccountCategory() {
        return this.accountCategory;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserUrl() {
        return this.userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}