package com.example.share.mapper;

import com.example.share.entity.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TopicMapper {

    @Insert(value = "insert into topic(title,content,createTime,user_id,state) values(#{title},#{content},#{createTime},#{user.id},#{state})")
    void insert(Topic topic);

    @Delete(value = "delete from topic where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update topic set title=#{title},content=#{content},createTime=#{createTime},user_id=#{user.id},state=#{state} where id=#{id}")
    void updateById(Topic topic);

    @Select("select * from topic where id=#{id}")
    @Results(id = "topicMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "user", one = @One(select = "com.example.share.mapper.UserMapper.selectById"))
    })
    Topic selectById(Integer id);

    @Select("select * from topic ${whereStr}")
    @ResultMap("topicMap")
    List<Topic> selectList(@Param("whereStr") String whereStr);

    @Delete(value = "delete from topic")
    void deleteAll();
}