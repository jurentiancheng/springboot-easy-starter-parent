package com.easyapi.minio.service;

import java.util.List;
import java.util.Optional;

/**
 * Created by zz on 2022/3/10 11:25 上午
 * 处理minio文件下载
 */
public interface FileDownService {
  void downFile(String url,String accessKey,String secretKey,String bucketName);
  void downFile(String url,String accessKey,String secretKey,String bucketName,String containName);
  Optional<List<String>> getObjectsList(String bucketName, boolean recursive,String url,String accessKey,String secretKey);
  Optional<List<String>> getObjectsList(String bucketName, boolean recursive,String url,String accessKey,String secretKey,String containName);
  void fileDownload(String url, String accessKey, String secretKey, String bucketName, String objectName, String fileName);
 }
