package com.example.share.controller;

import com.example.share.entity.User;
import com.example.share.service.UserService;
import com.example.share.unit.pagehelper.PageBean;
import com.example.share.util.ResponseBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/insert")
    public Object insert(User user) {
        userService.insert(user);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        userService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(User user) {
        userService.updateById(user);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", userService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", userService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        userService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }

    @RequestMapping("/login")
    public Object login(String username, String password) {
        List<User> userList = userService.selectList("where username='" + username + "' and password='" + password + "'");
        if (userList.size() > 0) {
            User user = userList.get(0);
            user.setLoginTime(new Date());
            userService.updateById(user);
            return new ResponseBean(200, "", 1);
        } else {
            return new ResponseBean(200, "", 0);
        }
    }

    @RequestMapping("/register")
    public Object register(String username, String password, String name, String phone) {
        List<User> userList = userService.selectList("where username='" + username + "'");
        if (userList.size() > 0) {
            return new ResponseBean(200, "", 2);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setPhone(phone);
        user.setCreateTime(new Date());
        user.setState(1);
        userService.insert(user);
        return new ResponseBean(200, "", 1);
    }
}