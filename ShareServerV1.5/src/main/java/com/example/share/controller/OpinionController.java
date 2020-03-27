package com.example.share.controller;

import com.example.share.entity.Folder;
import com.example.share.entity.Opinion;
import com.example.share.entity.User;
import com.example.share.service.FolderService;
import com.example.share.service.OpinionService;
import com.example.share.service.UserService;
import com.example.share.unit.pagehelper.PageBean;
import com.example.share.util.ResponseBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/opinion")
public class OpinionController {

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;

    @RequestMapping("/insert")
    public Object insert(Opinion opinion) {
        opinionService.insert(opinion);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        opinionService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(Opinion opinion) {
        opinionService.updateById(opinion);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", opinionService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", opinionService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        opinionService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }

    @RequestMapping("/sendOpinion")
    public Object sendOpinion(Integer folderId, String username, String password, String content) {
        List<User> userList = userService.selectList("where username='" + username + "' and password='" + password + "'");
        if (userList.size() == 0) {
            return new ResponseBean(200, "", 0);
        }
        User user = userList.get(0);

        Opinion opinion = new Opinion();
        Folder folder = new Folder();
        folder.setId(folderId);
        opinion.setFolder(folder);
        opinion.setUser(user);
        opinion.setAttitude(0);
        opinion.setContent(content);
        opinion.setState(1);
        opinionService.insert(opinion);

        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/sendAttitude")
    public Object sendAttitude(Integer folderId, String username, String password, Integer attitude) {
        List<User> userList = userService.selectList("where username='" + username + "' and password='" + password + "'");
        if (userList.size() == 0) {
            return new ResponseBean(200, "", 0);
        }
        User user = userList.get(0);

        List<Opinion> opinionList = opinionService.selectList("where user_id='" + user.getId() + "' and folder_id='" + folderId + "' and attitude!='" + 0 + "'");
        if (opinionList.size() != 0) {
            return new ResponseBean(200, "", 2);
        }
        Folder folder = folderService.selectById(folderId);

        Opinion opinion = new Opinion();
        opinion.setFolder(folder);
        opinion.setUser(user);
        opinion.setAttitude(1);
        opinion.setContent(null);
        opinion.setCreateTime(new Date());
        opinion.setState(1);
        opinionService.insert(opinion);

        if (attitude == 1) {
            folder.setLikeNum(folder.getLikeNum() + 1);
        } else if (attitude == -1) {
            folder.setDislikeNum(folder.getDislikeNum() + 1);
        }
        folderService.updateById(folder);

        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectListByFolderId")
    public Object selectListByFolderId(Integer folderId) {
        return new ResponseBean(200, "", opinionService.selectList("where content is not null and folder_id='" + folderId + "'"));
    }
}