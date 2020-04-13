package com.example.share.mapper;

import com.example.share.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert(value = "insert into comment(content,createTime,topic_id,user_id,state) values(#{content},#{createTime},#{topic.id},#{user.id},#{state})")
    void insert(Comment comment);

    @Delete(value = "delete from comment where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update comment set content=#{content},createTime=#{createTime},topic_id=#{topic.id},user_id=#{user.id},state=#{state} where id=#{id}")
    void updateById(Comment comment);

    @Select("select * from comment where id=#{id}")
    @Results(id = "commentMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "user", one = @One(select = "com.example.share.mapper.UserMapper.selectById"))
    })
    Comment selectById(Integer id);

    @Select("select * from comment ${whereStr}")
    @ResultMap("commentMap")
    List<Comment> selectList(@Param("whereStr") String whereStr);

    @Delete(value = "delete from comment")
    void deleteAll();
}