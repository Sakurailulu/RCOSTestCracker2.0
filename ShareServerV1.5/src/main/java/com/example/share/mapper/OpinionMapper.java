package com.example.share.mapper;

import com.example.share.entity.Opinion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OpinionMapper {

    @Insert(value = "insert into opinion(folder_id,user_id,content,attitude,createTime,state) values(#{folder.id},#{user.id},#{content},#{attitude},#{createTime},#{state})")
    void insert(Opinion opinion);

    @Delete(value = "delete from opinion where id in (${ids})")
    void deleteByIds(@Param("ids") String ids);

    @Update(value = "update opinion set folder_id=#{folder.id},user_id=#{user.id},content=#{content},attitude=#{attitude},createTime=#{createTime},state=#{state} where id=#{id}")
    void updateById(Opinion opinion);

    @Select("select * from opinion where id=#{id}")
    @Results(id = "folderMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "user", one = @One(select = "com.example.share.mapper.UserMapper.selectById")),
            @Result(column = "folder_id", property = "folder", one = @One(select = "com.example.share.mapper.FolderMapper.selectById"))
    })
    Opinion selectById(Integer id);

    @Select("select * from opinion ${whereStr}")
    List<Opinion> selectList(@Param("whereStr") String whereStr);
}