package com.project.esh_an.portal1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class randnum {
    String num;

    public String getNum() {
        return num;
    }

    public randnum(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyymmddhhmmssSSSSSS");
        String strDate= formatter.format(date);
        this.num=new String(strDate);
    }
}
