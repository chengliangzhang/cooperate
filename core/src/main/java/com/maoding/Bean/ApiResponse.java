package com.maoding.Bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maoding.Const.ApiResponseConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 请求操作时的返回值类型
 */
public final class ApiResponse<T> implements Serializable {
    /** 日志 */
    @JsonIgnore
    private final static Logger log = LoggerFactory.getLogger(ApiResponse.class);

    /** 返回值，用于表明正确还是错误 */
    @JsonIgnore
    private Integer codeValue;
    /** 返回状态文字说明 */
    private String msg;
    /** 返回对象 */
    private T dataValue;

    /** 用于维持兼容性的字段 */
    private String code; //同AjaxMessage.code
    private Object info; //同AjaxMessage.info
    private Object data; //同AjaxMessage.data

    public ApiResponse(Integer code, String msg, T dataValue) {
        this.codeValue = code;
        this.code = (this.codeValue != null) ? this.codeValue.toString() : ApiResponseConst.SUCCESS.toString();
        this.msg = (msg != null) ? msg : ApiResponseConst.DEFAULT_MESSAGE.get(this.codeValue);
        this.dataValue = dataValue;

        //记录日志
        if (isSuccessful()){
            log.debug(this.msg);
        } else if (isError()) {
            log.error(this.msg);
            if ((this.dataValue != null) && (this.dataValue instanceof Exception)) {
                Exception e = (Exception)dataValue;
                e.printStackTrace();
            }
        } else {
            log.info(this.msg);
        }
    }
    public ApiResponse(String msg, T dataValue) {this(ApiResponseConst.SUCCESS,msg,dataValue);}
    public ApiResponse(String msg) {this(msg,null);}
    public ApiResponse(Integer code, String msg) {this(code,msg,null);}
    public ApiResponse(Integer code) {this(code,null);}
    public ApiResponse() {this(ApiResponseConst.SUCCESS);}

    public static <E> ApiResponse<E> success(String msg, E dataValue) {
        return new ApiResponse<>(ApiResponseConst.SUCCESS, msg, dataValue);
    }
    public static <E> ApiResponse<E> success(E dataValue) {
        return success(null,dataValue);
    }
    public static <E> ApiResponse<E> success() {
        return success(null);
    }

    public static <E> ApiResponse<E> failed(String msg, E dataValue) {
        return new ApiResponse<>(ApiResponseConst.FAILED, msg, dataValue);
    }
    public static <E> ApiResponse<E> failed(String msg) {
        return failed(msg,null);
    }
    public static <E> ApiResponse<E> failed() {
        return failed(null);
    }

    public static <E> ApiResponse<E> error(Integer code, String msg, E dataValue) {
        return new ApiResponse<>(code, msg, dataValue);
    }
    public static <E> ApiResponse<E> error(Integer code, String msg) {
        return error(code,msg,null);
    }
    public static <E> ApiResponse<E> error(Integer code) {
        return error(code,null);
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return ApiResponseConst.SUCCESS.equals(codeValue);
    }

    @JsonIgnore
    public boolean isError() {
        return ((codeValue == null) || (codeValue < 0));
    }

    public Integer getCodeValue() {
        Integer i = codeValue;
        if ((i == null) && (code != null)){
            i = Integer.parseInt(code);
        }
        return i;
    }

    public void setCodeValue(Integer codeValue) {
        this.codeValue = codeValue;
    }

    public String getMsg() {
        String s = msg;
        if (s == null){
            s = info.toString();
        }
        return s;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDataValue() {
        T d = dataValue;
        if (d == null){
            try {
                d = (T) data;
            } catch (Exception e) {
                log.error("返回值中数据转换失败:{}",e.getMessage());
                e.printStackTrace();
            }
        }
        return d;
    }

    public void setDataValue(T dataValue) {
        this.dataValue = dataValue;
    }


    /** 维持兼容性 */
    public String getCode() {
        String s = code;
        if ((code == null) && (codeValue != null)){
            s = codeValue.toString();
        }
        return s;
    }

    public ApiResponse<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public Object getInfo() {
        Object o = info;
        if (o == null){
            o = msg;
        }
        return o;
    }

    public ApiResponse<T> setInfo(Object info) {
        this.info = info;
        return this;
    }

    public Object getData() {
        Object o = data;
        if (o == null){
            o = dataValue;
        }
        return o;
    }

    public ApiResponse<T> setData(Object data){
        this.data = data;
        return this;
    }


    public static <E> ApiResponse<E> urlNotFound(String msg,E dataValue){return error(ApiResponseConst.URL_NOT_FOUND,msg,dataValue);}
    public static <E> ApiResponse<E> dataNotFound(String msg,E dataValue){return error(ApiResponseConst.DATA_ERROR,msg,dataValue);}
    public static <E> ApiResponse<E> error(String msg,E dataValue){return error(ApiResponseConst.ERROR,msg,dataValue);}
    public static ApiResponse<Object> error(Object info){return error(ApiResponseConst.ERROR,info.toString(),null);}
    public static ApiResponse<Object> succeed(Object data){return success(data);}
}
