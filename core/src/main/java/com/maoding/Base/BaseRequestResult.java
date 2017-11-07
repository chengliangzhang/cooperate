package com.maoding.Base;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 16:56
 * 描    述 :
 */
public class BaseRequestResult {
    /** 申请的唯一编号 */
    private Integer requestId;
    /** 执行结果编码，0-正常，小于0-出现异常（异常编码），大于0-出现警告（警告编码） */
    private Integer status;
    /** 执行结果文字描述 */
    private String msg;

}
