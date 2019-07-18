package com.fan.news.interceptor;

import com.fan.news.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
//        System.out.println(url);
        String regex = "/news/.*/news/.*";
        String regex2 = "/news/details/.*";
        if (url.equals("/news/login") || url.equals("/news/user") || url.equals("/news/index") || url.equals("/news/newsinfo") ||
                url.matches(regex) || url.equals("/news/details") || url.matches(regex2)) {
            // 页面放行
//            System.out.println("*****" + url);

            return true;
        }
        User user = (User) request.getSession().getAttribute("sessionUser");
//        System.out.println(user);
        if (user == null || user.getNickname() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}