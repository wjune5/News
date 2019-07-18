package com.fan.news.web.Controller;

import com.fan.news.pojo.User;
import com.fan.news.service.UserService;
import com.fan.news.utils.Constant;
import com.fan.news.utils.JSONResult;
import com.fan.news.utils.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/userinfo")
    public JSONResult getUser(HttpServletRequest request) {
        String userJson = request.getParameter("userJson");
        System.out.println(userJson);
        if (StringUtils.isNotBlank(userJson)) {
            User user = JSONUtil.jsonToObj(userJson, User.class);
            if (user != null) {
                int rows = 0;
                try {
                    rows = userService.update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    return JSONResult.errorMsg(Constant.SYS_ERROR);
                }
                if (rows > 0) {
//                request.getSession().removeAttribute("sessionUser");
                    request.getSession().setAttribute("sessionUser", user);
                    return JSONResult.ok();
                }
            }
        }
        return JSONResult.errorMsg(Constant.USER_NULL);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("/")
    public JSONResult bye(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute("sessionUser") != null) {
            request.getSession().removeAttribute("sessionUser");

        }
        try {
            response.sendRedirect("/news/login");
        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorException(Constant.SYS_ERROR);
        }
        return JSONResult.ok();
    }

    /**
     * 用户登录
     * @param request
     * @return
     */
    @GetMapping("/user")
    public JSONResult login(HttpServletRequest request) {
        String userJson = request.getParameter("userJson");
        if (userJson != null) {
            try {
                User user = JSONUtil.jsonToObj(userJson, User.class);
                if (user != null && StringUtils.isNotBlank(user.getPassword()) && StringUtils.isNotBlank(user.getUserName())) {
                    User loginUser = userService.login(user);
                    if (loginUser != null && StringUtils.isNotBlank(loginUser.getNickname())) {
                        request.getSession().setAttribute("sessionUser", loginUser);

                        return JSONResult.ok(loginUser);
                    }

                }
                return JSONResult.errorMsg(Constant.USER_ERROR);  // 用户账号或密码出错
            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.errorException(Constant.SYS_ERROR);
            }

        }
        return JSONResult.errorMsg(Constant.SYS_ERROR);
    }
}
