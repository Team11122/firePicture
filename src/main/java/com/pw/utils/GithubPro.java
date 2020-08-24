package com.pw.utils;

import com.alibaba.fastjson.JSON;
import com.pw.pojo.AccessToken;
import com.pw.pojo.GitUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubPro {

    //根据授权登录成功返回的code解析出token
    public  String getAccressToken(AccessToken accessToken) throws IOException {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessToken));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String  string= response.body().string();//这是获取请求体的信息
            //返回的string是  access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
            String acess_token=string.split("&")[0].split("=")[1];
            System.err.println("解析出来的token:"+acess_token);
            return acess_token;
        }
    }
    //在根据解析出来的token获取用户的信息（json字符串形式）
    public GitUser getUsers(String  token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+token)//这个token就是自己创建的令牌的密钥。也就是code解析出来的token
                .build();
        Response response = client.newCall(request).execute();
        String  string= response.body().string();//这时候的string就是用户的json字符串
        GitUser user = JSON.parseObject(string, GitUser.class);//将json字符串转换未User对象的属性
        System.out.println(user+"======================");
        return user;
    }

}


