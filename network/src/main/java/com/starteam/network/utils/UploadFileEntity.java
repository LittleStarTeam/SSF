package com.starteam.network.utils;

import java.util.List;

/**
 * <p>Created by gizthon on 2018/3/8. email:2013mzhou@gmail.com</p>
 * <p>
 * des: 上传文件实体类
 */
public class UploadFileEntity {

    private List<UploadResultListEntity> uploadResultList;

    public List<UploadResultListEntity> getUploadResultList() {
        return uploadResultList;
    }

    public void setUploadResultList(List<UploadResultListEntity> uploadResultList) {
        this.uploadResultList = uploadResultList;
    }

    public static class UploadResultListEntity {
        /**
         * materialId : 1/180308/0/1520487807535136020.jpg
         * origialFileName : 273-2018-03-08-13-45-57-082.jpg
         * prefixDomain : http://183.62.215.52/upload/
         * fullFilePath : http://183.62.215.52/upload/1/180308/0/1520487807535136020.jpg
         * isSuccess : 0
         */

        private String materialId;      // 文件上传后所在路径【保存到后台的路径】，如:0/160412/0/1460443735903671150.png
        private String origialFileName; // 原文件名称如：xxx.jpg
        private String prefixDomain;    // 文件所在主机，如:http://172.16.180.76/
        private String fullFilePath;    // 文件上传后全路径【只是在上传后用户展示用途,不能提交此字段数据保存到后台】，如：http://172.16.180.76/0/160412/0/1460443741192358917.jpg
        private String isSuccess;       // 上传是否成功;0=成功，1=失败,2=文件为非白名单，禁止上传
        private String fileType	;       //	文件类型 1:图片，2：音频，3：视频

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

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
}
