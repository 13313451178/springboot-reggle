package com.jkh.reggie.Utils;

import java.util.Random;

public class SMSUtils {
    public static int getCode(){
        Random random = new Random();
        int i = random.nextInt(1000) + 1000;
        return i;
    }

}
