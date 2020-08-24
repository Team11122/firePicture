package com.pw.pojo;


import lombok.Data;

@Data
public class GitUser {
    private  Long id;
    private  String login;
    private  String  name;
    private String  bio;
    //以上字段是token解析出来的json字符串中三个，具体其他属性根据需要在添加
}

