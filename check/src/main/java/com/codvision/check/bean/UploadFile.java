package com.codvision.check.bean;

/**
 * Des:
 * 上传文件类
 *
 * @author xujichang
 * <p>
 * created by 2018/9/25-6:48 PM
 */
public class UploadFile {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件状态，是否上传
     */
    private int status;
    /**
     * 文件在数据库生成时间
     */
    private long storeMs;
    /**
     * 文件上传时间
     */
    private long uploadMs;
    /**
     * 递增值
     */
    private int id;
    /**
     * 封面
     */
    private String thumbnail;
    /**
     * 文件存储文件夹
     */
    private String dir;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStoreMs() {
        return storeMs;
    }

    public void setStoreMs(long storeMs) {
        this.storeMs = storeMs;
    }

    public long getUploadMs() {
        return uploadMs;
    }

    public void setUploadMs(long uploadMs) {
        this.uploadMs = uploadMs;
    }

    public void setThumbnail(String screenshot) {
        this.thumbnail = screenshot;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
