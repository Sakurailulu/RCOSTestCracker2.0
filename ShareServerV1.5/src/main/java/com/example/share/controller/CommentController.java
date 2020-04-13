package com.example.share.controller;

import com.example.share.entity.Comment;
import com.example.share.entity.Topic;
import com.example.share.entity.User;
import com.example.share.service.CommentService;
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
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TopicService topicService;

    @RequestMapping("/insert")
    public Object insert(Comment comment) {
        commentService.insert(comment);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        commentService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(Comment comment) {
        commentService.updateById(comment);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", commentService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", commentService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        commentService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }

    @RequestMapping("/selectListByTopicId")
    public Object selectListByTopicId(Integer topicId) {
        return new ResponseBean(200, "", commentService.selectList("where topic_id='" + topicId + "'"));
    }

    @RequestMapping("/add")
    public Object add(Comment comment, int topicId, String username, String password) {
        Topic topic = topicService.selectById(topicId);
        User user = userService.selectByUsername(username);
        comment.setTopic(topic);
        comment.setUser(user);
        comment.setCreateTime(new Date());
        commentService.insert(comment);
        return new ResponseBean(200, "", 1);
    }
}