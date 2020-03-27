package com.example.share.mapper;

import com.example.share.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper {
    @Insert(value = "insert into folder(course_id,user_id,title,content,path,fileName,createTime,likeNum,dislikeNum,state) values(#{course.id},#{user.id},#{title},#{content},#{path},#{fileName},#{createTime},#{likeNum},#{dislikeNum},#{state})")
    void insert(Folder folder);

    @Delete(value = "delete from folder where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update folder set course_id=#{course.id},user_id=#{user.id},title=#{title},content=#{content},path=#{path},fileName=#{fileName},createTime=#{createTime},likeNum=#{likeNum},dislikeNum=#{dislikeNum},state=#{state} where id=#{id}")
    void updateById(Folder folder);

    @Select("select * from folder where id=#{id}")
    @Results(id = "folderMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "user", one = @One(select = "com.example.share.mapper.UserMapper.selectById")),
            @Result(column = "course_id", property = "course", one = @One(select = "com.example.share.mapper.CourseMapper.selectById"))
    })
    Folder selectById(Integer id);

    @Select("select * from folder ${whereStr}")
    @ResultMap("folderMap")
    List<Folder> selectList(@Param("whereStr") String whereStr);
}