package com.example.share.service;

import com.example.share.entity.Opinion;

import java.util.List;

public interface OpinionService {

    void insert(Opinion opinion);

    void deleteByIds(String ids);

    void updateById(Opinion opinion);

    Opinion selectById(Integer id);

    List<Opinion> selectList(String whereStr);
}