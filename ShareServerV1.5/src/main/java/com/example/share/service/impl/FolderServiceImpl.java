package com.example.share.service.impl;

import com.example.share.entity.Folder;
import com.example.share.mapper.FolderMapper;
import com.example.share.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderMapper folderMapper;

    @Override
    public void insert(Folder folder) {
        folderMapper.insert(folder);
    }

    @Override
    public void deleteByIds(String ids) {
        folderMapper.deleteByIds(ids);
    }

    @Override
    public void updateById(Folder folder) {
        folderMapper.updateById(folder);
    }

    @Override
    public Folder selectById(Integer id) {
        return folderMapper.selectById(id);
    }

    @Override
    public List<Folder> selectList(String whereStr) {
        return folderMapper.selectList(whereStr);
    }
}
