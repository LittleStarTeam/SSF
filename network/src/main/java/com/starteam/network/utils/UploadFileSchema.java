package com.starteam.network.utils;

/**
 * @version V1.0
 * @function 图片上传返回类
 */

public class UploadFileSchema {

    private String materialId;      // 文件上传后所在路径【保存到后台的路径】，如:0/160412/0/1460443735903671150.png
    private String origialFileName; // 原文件名称如：xxx.jpg
    private String prefixDomain;    // 文件所在主机，如:http://172.16.180.76/
    private String fullFilePath;    // 文件上传后全路径【只是在上传后用户展示用途,不能提交此字段数据保存到后台】，如：http://172.16.180.76/0/160412/0/1460443741192358917.jpg
    private String isSuccess;       // 上传是否成功;0=成功，1=失败,2=文件为非白名单，禁止上传

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOrigialFileName() {
        return origialFileName;
    }

    public void setOrigialFileName(String origialFileName) {
        this.origialFileName = origialFileName;
    }

    public String getPrefixDomain() {
        return prefixDomain;
    }

    public void setPrefixDomain(String prefixDomain) {
        this.prefixDomain = prefixDomain;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }

    public void setFullFilePath(String fullFilePath) {
        this.fullFilePath = fullFilePath;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

}
