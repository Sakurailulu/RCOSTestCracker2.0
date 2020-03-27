package com.example.share.service.impl;

import com.example.share.entity.Opinion;
import com.example.share.mapper.OpinionMapper;
import com.example.share.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpinionServiceImpl implements OpinionService {

    @Autowired
    private OpinionMapper opinionMapper;

    @Override
    public void insert(Opinion opinion) {
        opinionMapper.insert(opinion);
    }

    @Override
    public void deleteByIds(String ids) {
        opinionMapper.deleteByIds(ids);
    }

    @Override
    public void updateById(Opinion opinion) {
        opinionMapper.updateById(opinion);
    }

    @Override
    public Opinion selectById(Integer id) {
        return opinionMapper.selectById(id);
    }

    @Override
    public List<Opinion> selectList(String whereStr) {
        return opinionMapper.selectList(whereStr);
    }
}
