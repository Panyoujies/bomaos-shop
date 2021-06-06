package cn.zlianpay.common.system.controller;

import cn.zlianpay.common.core.web.JsonResult;
import cn.zlianpay.common.core.annotation.OperLog;
import cn.zlianpay.common.core.utils.FileUploadUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 文件服务器
 * Created by wangfan on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @RequiresPermissions("sys:file:view")
    @RequestMapping("/manage")
    public String view() {
        return "system/file.html";
    }

    /**
     * 上传文件
     */
    @OperLog(value = "文件管理", desc = "上传文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file, HttpServletRequest request) {
        return FileUploadUtil.upload(file, request);
    }

    /**
     * 上传base64文件
     */
    @OperLog(value = "文件管理", desc = "上传base64文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload/base64")
    public JsonResult uploadBase64(String base64, HttpServletRequest request) {
        return FileUploadUtil.upload(base64, request);
    }

    /**
     * 预览文件
     */
    @GetMapping("/{dir}/{name:.+}")
    public void file(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.preview(dir + "/" + name, response);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{dir}/{name:.+}")
    public void downloadFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.download(dir + "/" + name, response);
    }

    /**
     * 查看缩略图
     */
    @GetMapping("/thumbnail/{dir}/{name:.+}")
    public void smFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.thumbnail(dir + "/" + name, response);
    }

    /**
     * 删除文件
     */
    @OperLog(value = "文件管理", desc = "删除文件", result = true)
    @RequiresPermissions("sys:file:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(String path) {
        if (path == null || path.trim().isEmpty()) {
            return JsonResult.error("参数不能为空");
        }
        if (FileUploadUtil.delete(path)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 查询文件列表
     */
    @OperLog(value = "文件管理", desc = "查询全部")
    @RequiresPermissions("sys:file:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(String dir) {
        List<Map<String, Object>> list = FileUploadUtil.list(dir);
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return ((Long) o2.get("updateTime")).compareTo((Long) o1.get("updateTime"));
            }
        });
        return JsonResult.ok().setData(list);
    }

}
