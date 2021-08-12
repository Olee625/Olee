package com.olee.project.dto;

import java.util.Map;

public class AuthorizationThreadLocal {
    //把构造函数私有，外面不能new，只能通过下面两个方法操作
    private AuthorizationThreadLocal() {

    }

    //todo 调研一下static 并发状态下有问题吗
    //ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不干扰。
    ////这里的static不会影响线程，threadLocal在创建时跟随线程
    private static final ThreadLocal<Map<String, String>> LOCAL = new ThreadLocal<>();

    public static void set(Map<String, String> authorization) {
        LOCAL.set(authorization);
    }

    public static Map<String, String> get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
