package com.example.share.controller;

import com.example.share.entity.Topic;
import com.example.share.entity.User;
import com.example.share.service.TopicService;
import com.example.share.service.UserService;
import com.example.share.unit.pagehelper.PageBean;
import com.example.share.util.ResponseBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @RequestMapping("/insert")
    public Object insert(Topic topic) {
        topicService.insert(topic);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        topicService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(Topic topic) {
        topicService.updateById(topic);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", topicService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", topicService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        topicService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }

    @RequestMapping("/add")
    public Object add(Topic topic, String username, String password) {
        User user = userService.selectByUsername(username);
        topic.setUser(user);
        topic.setCreateTime(new Date());
        topicService.insert(topic);
        return new ResponseBean(200, "", 1);
    }
}