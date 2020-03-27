package com.example.share.controller;

import com.example.share.entity.Course;
import com.example.share.service.CourseService;
import com.example.share.unit.pagehelper.PageBean;
import com.example.share.util.ResponseBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/insert")
    public Object insert(Course course) {
        courseService.insert(course);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        courseService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(Course course) {
        courseService.updateById(course);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", courseService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", courseService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        courseService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }

    @RequestMapping("/add")
    public Object add(Course course) {
        course.setCreateTime(new Date());
        course.setState(1);
        courseService.insert(course);
        return new ResponseBean(200, "", 1);
    }
}