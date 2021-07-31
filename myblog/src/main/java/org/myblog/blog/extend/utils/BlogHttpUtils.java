package org.myblog.blog.extend.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlogHttpUtils {

    public void RefreshCookie(HttpServletRequest request , HttpServletResponse response){
        if(request.getCookies() != null){
            HashMap<String,Object> cookie_map = new HashMap<>();
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getValue() != null && !cookie.getValue().isEmpty()){
                    cookie_map.put(cookie.getName(),cookie.getValue());
                }
            }
            addCookie(cookie_map,response);
        }
    }
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        return ip;
    }

    public String getRamdonString(int len) {
        StringBuffer sb = new StringBuffer();
        String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    public int getRandom(int max , int min){
        return new Random().nextInt(max)%(max-min+1) + min;
    }

    public String getDATE(){
        return new SimpleDateFormat("YYYYMMddHHmmssSS").format(new Date());
    }

    public String getRandom2(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();
    }

    public String getRequestCookieUid(HttpServletRequest request){
        String uid = null;
        if(request.getCookies() != null){
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("uid")) {
                    uid = cookie.getValue();
                }
            }
        }
        return  uid;
    }

    public void addCookie(Map<String,Object> cookie_map, HttpServletResponse response){
        if(cookie_map.size() > 0){
            cookie_map.forEach((k,v)->{
                Cookie cookie = new Cookie(k, (String) v);
                if(k.equals("uid") && v.toString().isEmpty()){
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/");
                }else{
                    cookie.setMaxAge(60*10);
                    cookie.setPath("/");
                }
                response.addCookie(cookie);
            });
        }
    }

    public void loginout(HttpServletRequest request,HttpServletResponse response){
        if(request.getCookies() != null){
            HashMap<String,Object> cookie_map = new HashMap<>();
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }

        }
    }

}
