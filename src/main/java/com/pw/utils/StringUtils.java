package com.pw.utils;

import com.pw.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 武祥市
 */
public class StringUtils {
    @Autowired
    AccountService accountService;
    public static boolean isNum(String s){
        char[] chars = s.toCharArray();
        boolean flag = true;
        for (char aChar : chars) {
            if (!Character.isDigit(aChar)) {
                flag = false;
            }
        }
        return flag;
    }
}
