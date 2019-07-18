package com.fan.news.web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SysController {
//    @RequestMapping("/error")
//    public String toErrorUI() {
//        return "common/error";
//    }
    @RequestMapping("/login")
    public String toLoginUI() {
        return "admin/index";
    }
    @RequestMapping("/menu")
    public String toMenuUI() {
        return "admin/menu";
    }
    @RequestMapping("/lnews")
    public String toListUI() {
        return "admin/lnews";
    }
    @RequestMapping("/inews")
    public String toInsertUI() {
        return "admin/inews";
    }
    @RequestMapping("/index")
    public String toUserUI() {
        return "user/index";
    }
    @RequestMapping("/details")
    public String toNewsDetailsUI() {
        return "user/details";
    }
    @RequestMapping("/admin/news/details")
    public String toAdminNewsDetailsUI() {
        return "admin/details";
    }
    @RequestMapping("/admin/user")
    public String toAdminUI() {
        return "admin/admin";
    }
}
