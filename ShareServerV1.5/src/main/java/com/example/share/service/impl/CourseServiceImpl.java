package com.example.share.service.impl;

import com.example.share.entity.Course;
import com.example.share.mapper.CourseMapper;
import com.example.share.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public void insert(Course course) {
        courseMapper.insert(course);
    }

    @Override
    public void deleteByIds(String ids) {
        courseMapper.deleteByIds(ids);
    }

    @Override
    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    @Override
    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<Course> selectList(String whereStr) {
        return courseMapper.selectList(whereStr);
    }
}
