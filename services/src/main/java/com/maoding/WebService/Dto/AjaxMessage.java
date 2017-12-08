package com.maoding.WebService.Dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/4 18:50
 * 描    述 :
 */
public class AjaxMessage {
    /**
     * 信息code
     */
    private String code;
    /**
     * 信息
     */
    private Object info;

    private Object data;

    public String getCode() {
        return code;
    }

    public AjaxMessage setCode(String code) {
        this.code = code;
        return this;
    }

    public Object getInfo() {
        return info;
    }

    public AjaxMessage setInfo(Object info) {
        this.info = info;
        return this;
    }

    public Object getData() {
        return data;
    }

    public AjaxMessage setData(Object data) {
        this.data = data;
        return this;
    }


    public static AjaxMessage failed(Object data) {
        AjaxMessage m = new AjaxMessage();
        m.setCode("1");
        m.setInfo(((data != null) && (data instanceof String)) ? data : "操作失败");
        if (data != null)
            m.setData(data);
        return m;
    }

    public static AjaxMessage error(Object info) {
        AjaxMessage m = new AjaxMessage();
        m.setCode("1");
        if (info != null)
            m.setInfo(info);
        return m;
    }

    public static AjaxMessage succeed(Object data) {
        AjaxMessage m = new AjaxMessage();
        m.setCode("0");
        m.setInfo(((data != null) && (data instanceof String)) ? data : "操作成功");
        if (data != null)
            m.setData(data);
        return m;
    }
}
