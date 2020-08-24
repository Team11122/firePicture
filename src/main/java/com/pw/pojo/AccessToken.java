package com.pw.pojo;

import lombok.Data;

/**
 * @author 武祥市
 */
@Data
public class AccessToken {
    private String client_id;
    private String client_secret;
    private String code;
    private  String  redirect_uri;
    private  String state;

}

