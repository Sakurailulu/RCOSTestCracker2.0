package com.example.share.service;

import com.example.share.entity.Course;

import java.util.List;

public interface CourseService {

    void insert(Course course);

    void deleteByIds(String ids);

    void updateById(Course course);

    Course selectById(Integer id);

    List<Course> selectList(String whereStr);
}