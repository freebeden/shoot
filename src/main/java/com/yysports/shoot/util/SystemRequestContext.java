package com.yysports.shoot.util;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SystemRequestContext {

    private static ThreadLocal<HttpServletRequest> httpRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();

    public static void removeHttpSession() {
        session.remove();
    }

    public static HttpSession getSession() {
        return session.get();
    }

    public static HttpServletRequest getRequest() {
        return httpRequest.get();
    }

    public static void setSession(HttpSession _session) {
        session.set(_session);
    }

    public static void setHttpRequest(HttpServletRequest request) {
        httpRequest.set(request);

    }

    public static void removeHttpRequest() {
        httpRequest.remove();

    }
}