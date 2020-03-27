package com.example.share.service;

import com.example.share.entity.Folder;

import java.util.List;

public interface FolderService {

    void insert(Folder folder);

    void deleteByIds(String ids);

    void updateById(Folder folder);

    Folder selectById(Integer id);

    List<Folder> selectList(String whereStr);
}