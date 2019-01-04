package com.hellomrpc.mybatis.generator.plugins;

public class Utils {
    public static String join(String join,String[] strAry){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
            if(i==(strAry.length-1)){
                sb.append(strAry[i]);
            }else{
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }
}
