package com.easyapi.minio.service.impl;

import com.easyapi.minio.service.FileDownService;
import io.minio.DownloadObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by zz on 2022/3/10 11:25 上午
 */
@Slf4j
public class FileDownImpl implements FileDownService {
  private static String prexx = "/";

  /**
   * 下载文件
   * @param url
   * @param accessKey
   * @param secretKey
   * @param bucketName
   * @param containName
   */
  @Override
  public void downFile(String url,String accessKey,String secretKey,String bucketName,String containName) {
    try {
      List<String> list = getObjectsList(bucketName,true,url,accessKey,secretKey,containName).get();
      list.forEach(e->{
        fileDownload(url,accessKey,secretKey,bucketName,e,e.split(prexx)[e.split(prexx).length-1]);
      });
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * 下载文件
   * @param url
   * @param accessKey
   * @param secretKey
   * @param bucketName
   */
  @Override
  public void downFile(String url,String accessKey,String secretKey,String bucketName) {
    try {
      List<String> list = getObjectsList(bucketName,true,url,accessKey,secretKey).get();
      list.forEach(e->{
        fileDownload(url,accessKey,secretKey,bucketName,e,e.split(prexx)[e.split(prexx).length-1]);
      });
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * 获取 文件列表
   * @param bucketName
   * @param recursive
   * @param url
   * @param accessKey
   * @param secretKey
   * @return
   */
    @Override
    public Optional<List<String>> getObjectsList (String bucketName,boolean recursive,String url,String accessKey,String secretKey) {
      List<String> list = new ArrayList<>();
      try{
      MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
      Iterable<Result<Item>> results =
          minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(recursive).build());
      Iterator re = results.iterator();
      while (re.hasNext()) {
        Result<Item> result = (Result<Item>) re.next();
        Item item = result.get();
        list.add(item.objectName());
      }
      }catch (Exception e){
        e.printStackTrace();
      }
      return Optional.ofNullable(list);
    }
  @Override
  public Optional<List<String>> getObjectsList (String bucketName,boolean recursive,String url,String accessKey,String secretKey,String containName) {
    List<String> list = new ArrayList<>();
    try{
      MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
      Iterable<Result<Item>> results =
          minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(recursive).build());
      Iterator re = results.iterator();
      while (re.hasNext()) {
        Result<Item> result = (Result<Item>) re.next();
        Item item = result.get();
        list.add(item.objectName());
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    return Optional.ofNullable(list.stream().filter(s -> s.contains(containName)).collect(Collectors.toList()));
  }
  /**
   * 下载文件
   * @param url
   * @param accessKey
   * @param secretKey
   * @param bucketName
   * @param objectName
   * @param fileName
   */
  @Override
  public void fileDownload(
      String url,
      String accessKey,
      String secretKey,
      String bucketName,
      String objectName,
      String fileName) {
    try {
      MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
      minioClient.downloadObject(
          DownloadObjectArgs.builder()
              .bucket(bucketName)
              .object(objectName)
              .filename(fileName)
              .build());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
