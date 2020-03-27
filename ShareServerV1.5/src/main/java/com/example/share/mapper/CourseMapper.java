package com.example.share.mapper;

import com.example.share.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Insert(value = "insert into course(name,createTime,state) values(#{name},#{createTime},#{state})")
    void insert(Course course);

    @Delete(value = "delete from course where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update course set name=#{name},createTime=#{createTime},state=#{state} where id=#{id}")
    void updateById(Course course);

    @Select("select * from course where id=#{id}")
    Course selectById(Integer id);

    @Select("select * from course ${whereStr}")
    List<Course> selectList(@Param("whereStr") String whereStr);
}