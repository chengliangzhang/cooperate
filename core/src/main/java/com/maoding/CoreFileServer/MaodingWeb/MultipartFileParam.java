package com.maoding.CoreFileServer.MaodingWeb;

import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/22 16:59
 * 描    述 :
 */
public class MultipartFileParam {
    //该请求是否是multipart
    /*private boolean isMultipart;*/
    /**
     * 任务ID
     */
    private String uploadId;

    /**
     * 总分片数量
     */
    private Integer chunks;

    /**
     * 当前为第几块分片
     */
    private Integer chunk;

    /**
     * 每个分片的约定大小
     */
    private Long chunkPerSize = 0L;

    /**
     * 当前分片大小
     */
    private Long size = 0L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String fileExtName;

    /**
     * 分片对象
     */
    private FileItem fileItem;

    /**
     * 仅用于FASTDFS Append
     */
    private String fastdfsGroup;

    /**
     * 仅用于FASTDFS Append
     */
    private String fastdfsPath;

    /**
     * 请求中附带的自定义参数
     */
    private HashMap<String, Object> param = new HashMap<>();

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public Long getChunkPerSize() {
        return chunkPerSize;
    }

    public void setChunkPerSize(Long chunkPerSize) {
        this.chunkPerSize = chunkPerSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public String getFastdfsGroup() {
        return fastdfsGroup;
    }

    public void setFastdfsGroup(String fastdfsGroup) {
        this.fastdfsGroup = fastdfsGroup;
    }

    public String getFastdfsPath() {
        return fastdfsPath;
    }

    public void setFastdfsPath(String fastdfsPath) {
        this.fastdfsPath = fastdfsPath;
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public void setParam(HashMap<String, Object> param) {
        this.param = param;
    }
}
