package com.example.share.mapper;

import com.example.share.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert(value = "insert into user(username,password,name,phone,createTime,loginTime,state) values(#{username},#{password},#{name},#{phone},#{createTime},#{loginTime},#{state})")
    void insert(User user);

    @Delete(value = "delete from user where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update user set username=#{username},password=#{password},name=#{name},phone=#{phone},createTime=#{createTime},loginTime=#{loginTime},state=#{state} where id=#{id}")
    void updateById(User user);

    @Select("select * from user where id=#{id}")
    User selectById(Integer id);

    @Select("select * from user ${whereStr}")
    List<User> selectList(@Param("whereStr") String whereStr);

    @Select("select * from user where username=#{username}")
    User selectByUsername(String username);

    @Delete(value = "delete from user")
    void deleteAll();
}