package com.example.share.controller;

import com.example.share.entity.Course;
import com.example.share.entity.Folder;
import com.example.share.entity.User;
import com.example.share.service.FolderService;
import com.example.share.service.UserService;
import com.example.share.unit.pagehelper.PageBean;
import com.example.share.util.ResponseBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;

    @RequestMapping("/insert")
    public Object insert(Folder folder) {
        folderService.insert(folder);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/deleteByIds")
    public Object deleteByIds(String ids) {
        folderService.deleteByIds(ids);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/updateById")
    public Object updateById(Folder folder) {
        folderService.updateById(folder);
        return new ResponseBean(200, "", 1);
    }

    @RequestMapping("/selectById")
    public Object selectById(int id) {
        return new ResponseBean(200, "", folderService.selectById(id));
    }

    @RequestMapping("/selectList")
    public Object selectList() {
        return new ResponseBean(200, "", folderService.selectList(""));
    }

    @RequestMapping("/selectByPage")
    public Object selectByPage(int pageNum, int pageSize, String orderBy) {
        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        folderService.selectList("");
        return new ResponseBean(200, "", new PageBean(page));
    }


    @RequestMapping("upload")
    public Object upload(String title, String content, MultipartFile file, Integer courseId, String username, String password) {

        List<User> userList = userService.selectList("where username='" + username + "' and password='" + password + "'");
        if (userList.size() == 0) {
            return new ResponseBean(200, "", 0);
        }

        User user = userList.get(0);

        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 文件上传路径
        String filePath = "F:\\ZFILE\\";
        String suffix = "";
        try {
            suffix = "." + fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fullPath = filePath + UUID.randomUUID() + suffix;
        File dest = new File(fullPath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        Folder folder = new Folder();
        Course course = new Course();
        course.setId(courseId);
        folder.setCourse(course);
        folder.setUser(user);
        folder.setTitle(title);
        folder.setContent(content);

        folder.setPath(fullPath);
        folder.setFileName(fileName);
        folder.setCreateTime(new Date());
        folder.setState(1);
        folder.setLikeNum(0);
        folder.setDislikeNum(0);
        folderService.insert(folder);

        try {
            file.transferTo(dest);
            return new ResponseBean(200, "", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseBean(200, "", 0);
    }

    @RequestMapping("/download")
    public Object download(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getHeader("path");
        if (path != null) {
            //设置文件路径
            File file = new File(path);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


    @RequestMapping("/selectListByCourseId")
    public Object selectListByCourseId(Integer courseId) {
        return new ResponseBean(200, "", folderService.selectList("where course_id='" + courseId + "'"));
    }

}