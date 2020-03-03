package com.example.share.model;

import java.io.Serializable;
import java.util.Date;

public class userAdmin implements Serializable {

    private Integer id;
    private String useradminname;
    private String password;
    private String name;
    private String phone;
    private Date createTime;
    private Date loginTime;
    private Integer state;

    public Admin(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserAdminname() {
        return useradminname;
    }

    public void setUsername(String username) {
        this.username = useradminname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
}