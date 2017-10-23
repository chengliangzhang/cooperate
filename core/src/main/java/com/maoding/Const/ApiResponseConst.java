package com.maoding.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/14.
 */
public interface ApiResponseConst {
    final static Integer SUCCESS = 0; //操作成功
    final static Integer FAILED = 1; //操作失败
    final static Integer ERROR = -1; //发生异常
    final static Integer DATA_ERROR = -2; //数据异常
    final static Integer NO_PERMISSION = -3; //未授权
    final static Integer URL_NOT_FOUND = 404; //找不到URL

    final static Map<Integer,String> DEFAULT_MESSAGE = new HashMap<Integer,String>(){
        {
            put(SUCCESS,"操作成功");
            put(FAILED, "操作失败");
            put(ERROR,"发生异常");
            put(DATA_ERROR,"数据异常");
            put(NO_PERMISSION,"未授权");
            put(URL_NOT_FOUND,"找不到URL");
        }
    };
}
