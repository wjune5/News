package com.fan.news.web.Controller;

import com.fan.news.pojo.News;
import com.fan.news.pojo.NewsFile;
import com.fan.news.pojo.NewsFileDto;
import com.fan.news.pojo.NewsImg;
import com.fan.news.service.NewsService;
import com.fan.news.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NewsController {

    private NewsService newsService;

    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    private Sid sid;

    @GetMapping("/delete/{newsId}")
    public JSONResult deleteNews(@PathVariable("newsId") String newsId) {
        if (StringUtils.isNotBlank(newsId)) {
            int rows = newsService.deleteNewsByNewsId(newsId);
            if (rows > 0) {
                return JSONResult.ok();
            }
        }
        return JSONResult.errorMsg(Constant.SYS_ERROR);
    }

    /**
     * 更新新闻内容
     * @param newsId
     * @param request
     * @return
     */
    @PostMapping("/newsinfo/{newsId}")
    public JSONResult updateNews(@PathVariable("newsId") String newsId, HttpServletRequest request) {
        String newsJson = request.getParameter("newsJson");
        if (newsJson != null) {
            News news = JSONUtil.jsonToObj(newsJson, News.class);

            if (news != null) {
                try {
                    news.setNewsId(newsId);
                    news.setLastEditTime(new Date());
                    int rows = newsService.updateNews(news);
                    if (rows > 0) {
                        return JSONResult.ok();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    return JSONResult.errorException(Constant.SYS_ERROR);
                }
            }
        }

        return JSONResult.errorMsg(Constant.SYS_ERROR);
    }

    /*@GetMapping("/files/{fileName}")
    public JSONResult downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        if (fileName != null) {
            try (
                    InputStream inputStream = new FileInputStream(new File(PathUtil.getBasePath() + PathUtil.getPublicPath("news/") + fileName));
                    OutputStream outputStream = response.getOutputStream();
                    // localhost:8080/news/1/news/新闻发布系统.doc
            ) {

                fileName = URLDecoder.decode(fileName, "UTF-8");
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);


                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONResult.ok();
    }
*/

    @GetMapping("/details/{newsId}")
    public JSONResult getNewsDetails(@PathVariable("newsId") String newsId) {
        NewsFileDto news = null;
        if (newsId != null) {
             news = newsService.getNewsByNewsId(newsId);
             if (news != null) {
                 return JSONResult.ok(news);
             }
        }
        return JSONResult.errorMsg(Constant.NEWS_CONTENT_NULL);
    }

    /**
     * 获取首页新闻列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/newsinfo")
    public JSONResult getNewsToUser(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        PageUtil pageResult = null;
        try {
            pageResult = newsService.getHotNews(page, Constant.PAGE_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.errorException(Constant.SYS_ERROR);
        }

        return JSONResult.ok(pageResult);
    }

    /**
     * 获取新闻列表
     *
     * @param userId
     * @param page
     * @return
     */
    @GetMapping("/news/{userId}")
    public JSONResult getNews(@PathVariable("userId") String userId, Integer page) {
        if (StringUtils.isNotBlank(userId)) {
            PageUtil pageResult = null;
            if (page == null) {
                page = 1;
            }
            try {
                pageResult = newsService.getNewsById(page, Constant.PAGE_SIZE, userId);
            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.errorException(Constant.SYS_ERROR);
            }

            return JSONResult.ok(pageResult);
        }
        return JSONResult.errorMsg(Constant.USER_NULL);
    }

    /**
     * 上传文件
     * @param multipartFile
     * @param userId
     * @return
     */
    @PostMapping("/news/file/{userId}")
    public JSONResult insertNewsFile(@RequestParam("file") MultipartFile multipartFile, @PathVariable("userId") String userId) {
        if (!multipartFile.isEmpty()) {
            // 上传图片
            boolean flag = FileUtil.checkFileSize((long) 10485760, multipartFile.getSize(), "M");
            if (flag) {
                String fileName = multipartFile.getOriginalFilename();
                String newsFilePath = PathUtil.getUserPath(userId, "news/") + fileName;

                File file = new File(PathUtil.getBasePath() + newsFilePath);
                if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    multipartFile.transferTo(file);
                    return JSONResult.ok(newsFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return JSONResult.errorException(Constant.SYS_ERROR);
                }
            }
        }
        return JSONResult.errorException(Constant.FILE_NULL);

    }

    /**
     * 上传新闻图片
     * @param multipartFile
     * @param userId
     * @return
     */
    @PostMapping("/news/img/{userId}")
    public Map<String, Object> insertNewsImg(@RequestParam("file") MultipartFile multipartFile, @PathVariable("userId") String userId) {
        String newsPath = null;
        Map<String, Object> modelMap = new HashMap<>();
        DataResult dataResult = new DataResult();

        if (!multipartFile.isEmpty()) {
            // 上传图片
            boolean flag = FileUtil.checkFileSize((long) 10485760, multipartFile.getSize(), "M");
            if (flag) {
                FileOutputStream fileOutputStream = null;
                InputStream inputStream = null;

                try {
                    String originalFileName = multipartFile.getOriginalFilename();
                    if (StringUtils.isNotBlank(originalFileName)) {

                        newsPath = PathUtil.getUserPath(userId, "news/" + originalFileName);

                        File file = new File(PathUtil.getBasePath() + newsPath);
                        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
                            file.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        inputStream = multipartFile.getInputStream();
                        IOUtils.copy(inputStream, fileOutputStream);

                        modelMap.put("code", 0);
                        modelMap.put("msg", "上传成功~");
                        dataResult.setSrc("/news" + newsPath);

                        modelMap.put("data", dataResult);
                        return modelMap;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    modelMap.put("code", 1);
                    modelMap.put("msg", "上传失败~");
                    dataResult.setSrc("");
                    modelMap.put("data", dataResult);
                    return modelMap;
//                    return JSONResult.errorException(Constant.SYS_ERROR);
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                modelMap.put("code", 1);
                modelMap.put("msg", "文件过大~");
                modelMap.put("data", dataResult);
                return modelMap;
//                return JSONResult.errorMsg(Constant.FILE_LARGE);
            }
        }
        modelMap.put("code", 1);
        modelMap.put("msg", Constant.FILE_NULL);
        modelMap.put("data", dataResult);
        return modelMap;
//        return JSONResult.ok(newsPath);
    }

    /**
     * 添加新闻表单
     * @param request
     * @return
     */
    @PostMapping("/news")
    public JSONResult insertNews(HttpServletRequest request) {
        String newsJson = request.getParameter("newsJson");
        String imgAddress = request.getParameter("imgAddress");
        String fileAddress = request.getParameter("fileAddress");
        String[] imgs = imgAddress.split(",");

        News news = null;
        if (newsJson != null) {
            try {
                news = JSONUtil.jsonToObj(newsJson, News.class);
            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.errorException(Constant.SYS_ERROR);
            }
        } else {
            return JSONResult.errorException(Constant.SYS_ERROR);
        }
        String title = news.getTitle();
        String content = news.getContent();
        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content)) {
            String newsId = sid.nextShort();
            news.setNewsId(newsId);
            news.setCreateTime(new Date());
            news.setLastEditTime(new Date());

            NewsImg newsImg = null;
            int count = 0;

            if (imgs.length > 0) {
                for (int i = 1; i < imgs.length; i++) {
                    // 添加文件
                    newsImg = new NewsImg();

                    newsImg.setImgAddress(imgs[i]);
                    newsImg.setNewsImgId(sid.nextShort());
                    newsImg.setNewsId(newsId);

                }
                count++;
            }
            NewsFile newsFile = null;
            if (StringUtils.isNotBlank(fileAddress)) {
                newsFile = new NewsFile();
                newsFile.setFileAddress(fileAddress);
                newsFile.setNewsId(newsId);
                newsFile.setNewsFileId(sid.nextShort());
                count++;
            }
            try {
                int rows = newsService.insertNews(news, newsImg, newsFile);
                if ((rows >= 3 && count == 2) || (rows >= 2 && count == 1) || (rows >= 1 && count == 0)) {
                    return JSONResult.ok();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.errorException(Constant.SYS_ERROR);
            }
        }
        return JSONResult.errorMsg(Constant.NEWS_CONTENT_NULL);
    }
}
