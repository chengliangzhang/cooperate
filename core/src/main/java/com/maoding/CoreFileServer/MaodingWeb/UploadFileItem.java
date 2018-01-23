package com.maoding.CoreFileServer.MaodingWeb;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/22 17:40
 * 描    述 : 一个POJO。用于保存上传文件的相关信息
 */
public class UploadFileItem implements Serializable {
    private static final long serialVersionUID = 1L;

    // The form field name in a form used foruploading a file,
    // such as "upload1" in "<inputtype="file" name="upload1"/>"
    private String formFieldName;

    // File name to be uploaded, thefileName contains path,
    // such as "E:\\some_file.jpg"
    private String fileName;

    public UploadFileItem(String formFieldName, String fileName)
    {
        this.formFieldName =formFieldName;
        this.fileName = fileName;
    }

    public String getFormFieldName()
    {
        return formFieldName;
    }

    public void setFormFieldName(String formFieldName)
    {
        this.formFieldName =formFieldName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
