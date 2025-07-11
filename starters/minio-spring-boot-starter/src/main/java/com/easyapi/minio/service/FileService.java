package com.easyapi.minio.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * The interface File service.
 *
 * @Author: jurentiancheng
 * @Date: 2020 /9/21 8:12 下午
 * @Version 1.0
 */
public interface FileService {

    /**
     * 创建bucket
     * @param name
     */
    void makeBucket(String name) ;

    /**
     * base64 图片内容上传
     * @param base64Content content
     * @param prefixName  前缀名称
     * @return
     */
    Optional<String> base64FileUpload(String base64Content, String prefixName);

    /**
     * base64 图片内容上传 打tag
     * @param base64Content content
     * @param prefixName  前缀名称
     * @param tags  tags
     * @return
     */
    Optional<String> base64FileUpload(String base64Content, String prefixName, List<String> tags);

    /**
     * base64 图片内容上传
     * @param base64Content content
     * @param prefixName  前缀名称
     * @param isStaticFile 是否静态文件
     * @param tags  tags
     * @param bucket  minio bucket
     * @return
     */
    Optional<String> base64FileUpload(String base64Content, String prefixName, Boolean isStaticFile, List<String> tags, String bucket);

    /**
     * base64 图片内容上传 直接上传到 static 目录，不需要标记tag
     * @param base64Content content
     * @param prefixName  前缀名称
     * @return
     */
    Optional<String> base64FileUploadWithStatic(String base64Content, String prefixName) ;

    /**
     * base64 图片内容上传 直接上传到 static 目录，不需要标记tag
     * @param base64Content content
     * @param prefixName  前缀名称
     * @param bucket  minio bucket
     * @return
     */
    Optional<String> base64FileUploadWithStatic(String base64Content, String prefixName, String bucket);

    /**
     * 远程文件上传
     * @param fileUrl      远程文件链接
     * @param prefixName    文件名 前缀
     * @return
     */
    Optional<String> urlFileUpload(String fileUrl, String prefixName);

    /**
     * 远程文件上传
     * @param fileUrl      远程文件链接
     * @param prefixName    文件名 前缀
     * @param rename        是否重命名 default:true
     * @return
     */
    Optional<String> urlFileUpload(String fileUrl, String prefixName, Boolean rename);

    /**
     * 远程文件上传
     * @param fileUrl      远程文件链接
     * @param prefixName    文件名 前缀
     * @param tags          tags 值
     * @return
     */
    Optional<String> urlFileUpload(String fileUrl, String prefixName, List<String> tags);

    /**
     * 远程文件上传
     * @param fileUrl      远程文件链接
     * @param prefixName    文件名 前缀
     * @param tags          tags 值
     * @param isStaticFile  是否上传到Static 目录
     * @return
     */
    Optional<String> urlFileUpload(String fileUrl, String prefixName, List<String> tags, Boolean isStaticFile);

    /**
     * 远程文件上传
     * @param fileUrl      远程文件链接
     * @param prefixName    文件名 前缀
     * @param rename        是否重命名 default:true
     * @param isStaticFile  是否上传到Static 目录
     * @param tags          tags  打标签
     * @param bucket        minio bucket
     * @return              上传minio成功后的链接
     */
    Optional<String> urlFileUpload(String fileUrl, String prefixName, Boolean rename, Boolean isStaticFile, List<String> tags, String bucket);

    /**
     * 文件上传
     * @param filePath      文件路径
     * @param prefixName    文件名 前缀
     * @return
     */
    Optional<String> fileUpload(String filePath, String prefixName);

    /**
     * 文件上传  成功后不清理缓存文件，自行清理
     * @param filePath      文件路径
     * @param prefixName    文件名 前缀
     * @return
     */
    Optional<String> fileUploadWithNoPurge(String filePath, String prefixName);

    /**
     * 文件上传 成功后清理缓存文件，自行清理
     * @param filePath
     * @param prefixName
     * @param rename
     */
    Optional<String> fileUploadWithNoPurge(String filePath, String prefixName, Boolean rename);

    /**
     * 文件上传
     * @param filePath      文件路径
     * @param prefixName    文件名 前缀
     * @param rename        是否重命名 default:true
     * @return
     */
    Optional<String> fileUpload(String filePath, String prefixName, Boolean rename);

    /**
     * 文件上传
     * @param filePath      文件路径
     * @param prefixName    文件名 前缀
     * @param tags          tags 值
     * @return
     */
    Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, List<String> tags);

    /**
     * 文件上传
     * @param filePath      文件路径
     * @param prefixName    文件名 前缀
     * @param tags          tags 值
     * @return
     */
    Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, List<String> tags, String bucket) ;


    /**
     * 文件上传
     * @param filePath
     * @param prefixName
     * @param rename
     * @param isStaticFile
     * @param tags
     * @param bucket
     * @param clearLocalFile
     * @return
     */
    Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, Boolean isStaticFile, List<String> tags, String bucket, Boolean clearLocalFile);

    /**
     *
     * @param filePath
     * @param prefixName
     * @return
     */
    Optional<String> fileUploadWithStatic(String filePath, String prefixName);

    /**
     *
     * @param filePath
     * @param prefixName
     * @return
     */
    Optional<String> fileUploadWithStatic(String filePath, String prefixName, Boolean rename);

    /**
     *
     * @param filePath
     * @param prefixName
     * @param bucket
     * @return
     */
    Optional<String> fileUploadWithStatic(String filePath, String prefixName, String bucket);

    /**
     *
     * @param filePath
     * @param prefixName
     * @param bucket
     * @return
     */
    Optional<String> fileUploadWithStatic(String filePath, String prefixName, String bucket, Boolean rename);

    /**
     * 文件上传（带回调方法） 用户上传图片后 执行必要的图像特征入库行为
     * @param file
     * @param prefixName
     * @param makeParamFunction
     * @param analyzerFunction
     * @return
     */
    Optional<Object> imageUploadWithStaticBlock(File file, String prefixName, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction);

    /**
     * 文件上传（带回调方法） 用户上传图片后 执行必要的图像特征入库行为
     * @param file
     * @param prefixName
     * @param makeParamFunction
     * @param analyzerFunction
     * @return
     */
    Optional<Object> imageUploadBlock(File file, String prefixName, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction);

    /**
     * 文件上传（带回调方法） 用户上传图片后 执行必要的图像特征入库行为
     * @param file
     * @param prefixName
     * @param tags
     * @param makeParamFunction
     * @param analyzerFunction
     * @return
     */
    Optional<Object> imageUploadBlock(File file, String prefixName, List<String> tags, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction);

}
