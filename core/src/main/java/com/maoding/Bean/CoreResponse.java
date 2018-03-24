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
public class CoreResponse<T> implements Serializable {
    /** 日志 */
    @JsonIgnore
    private final static Logger log = LoggerFactory.getLogger(CoreResponse.class);

    /** 返回状态，等于0-正常，小于0-发生异常，大于0-存在警告 */
    private Integer status;
    /** 返回状态文字说明 */
    private String msg;
    /** 返回对象 */
    private T data;

    /** 用于维持兼容性的字段 */
    private String code; //同AjaxMessage.code
    private Object info; //同AjaxMessage.info

    private Object extendData;

    public Object getExtendData() {
        return extendData;
    }

    public void setExtendData(Object extendData) {
        this.extendData = extendData;
    }

    public CoreResponse(Integer status, String msg, T data) {
        this.status = (status != null) ? status : ApiResponseConst.SUCCESS;
        this.msg = (msg != null) ? msg : ApiResponseConst.DEFAULT_MESSAGE.get(this.status);
        this.data = data;

        //记录日志
        if (isSuccessful()){
            log.debug(this.msg);
        } else if (isError()) {
            log.error(this.msg);
            if ((this.data != null) && (this.data instanceof Exception)) {
                Exception e = (Exception)data;
                e.printStackTrace();
            }
        } else {
            log.info(this.msg);
        }
    }
    public CoreResponse(String msg, T data) {this(ApiResponseConst.SUCCESS,msg,data);}
    public CoreResponse(String msg) {this(msg,null);}
    public CoreResponse(Integer code, String msg) {this(code,msg,null);}
    public CoreResponse(Integer code) {this(code,null);}
    public CoreResponse() {this(ApiResponseConst.SUCCESS);}

    public static <E> CoreResponse<E> success(String msg, E data) {
        return new CoreResponse<>(ApiResponseConst.SUCCESS, msg, data);
    }
    public static <E> CoreResponse<E> success(E data) {
        return success(null,data);
    }
    public static <E> CoreResponse<E> success() {
        return success(null);
    }

    public static <E> CoreResponse<E> failed(String msg, E data) {
        return new CoreResponse<>(ApiResponseConst.FAILED, msg, data);
    }
    public static <E> CoreResponse<E> failed(String msg) {
        return failed(msg,null);
    }
    public static <E> CoreResponse<E> failed() {
        return failed(null);
    }

    public static <E> CoreResponse<E> error(Integer code, String msg, E data) {
        return new CoreResponse<>(code, msg, data);
    }
    public static <E> CoreResponse<E> error(Integer code, String msg) {
        return error(code,msg,null);
    }
    public static <E> CoreResponse<E> error(Integer code) {
        return error(code,null);
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return ApiResponseConst.SUCCESS.equals(status);
    }

    @JsonIgnore
    public boolean isError() {
        return ((status == null) || (status < 0));
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    /** 维持兼容性 */
    public String getCode() {
        String s = code;
        if (s == null){
            s =  (status != null) ? status.toString() : ApiResponseConst.SUCCESS.toString();
        }
        return s;
    }

    public CoreResponse<T> setCode(String code) {
        this.code = code;
        status = Integer.parseInt(code);
        return this;
    }

    public Object getInfo() {
        Object o = info;
        if (o == null){
            o = msg;
            if (o == null) o = (status != null) ? ApiResponseConst.DEFAULT_MESSAGE.get(status) : ApiResponseConst.DEFAULT_MESSAGE.get(ApiResponseConst.SUCCESS);
        }
        return o;
    }

    public CoreResponse<T> setInfo(Object info) {
        this.info = info;
        if (info instanceof String){
            msg = (String) info;
        }
        return this;
    }

    public static <E> CoreResponse<E> urlNotFound(String msg, E data){return error(ApiResponseConst.URL_NOT_FOUND,msg,data);}
    public static <E> CoreResponse<E> dataNotFound(String msg, E data){return error(ApiResponseConst.DATA_ERROR,msg,data);}
    public static <E> CoreResponse<E> error(String msg, E data){return error(ApiResponseConst.ERROR,msg,data);}
    public static CoreResponse<Object> error(Object info){return error(ApiResponseConst.ERROR,info.toString(),null);}
}
