package com.maoding.Common;

import com.maoding.Common.zeroc.CustomException;
import com.maoding.Common.zeroc.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/12 10:22
 * 描    述 :
 */
public class CheckService {
    private final static Logger log = LoggerFactory.getLogger(CheckService.class);

    public static void check(boolean condition, ErrorCode code, String message) throws CustomException {
        if (!(condition)) throw new CustomException(code,message);
    }
    public static void check(boolean condition, ErrorCode code) throws CustomException{
        check(condition,code,"系统异常");
    }
    public static void check(boolean condition, String message) throws CustomException{
        check(condition,ErrorCode.Assert,message);
    }
    public static void check(boolean condition) throws CustomException{
        check(condition,ErrorCode.Assert);
    }

}
