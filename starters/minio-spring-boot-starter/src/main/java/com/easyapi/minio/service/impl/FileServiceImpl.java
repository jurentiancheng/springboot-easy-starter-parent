package com.easyapi.minio.service.impl;

import com.easyapi.minio.service.FileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * The type File service.
 *
 * @Author: jurentiancheng
 * @Date: 2020 /9/21 8:12 下午
 * @Version 1.0
 */
@Slf4j
public class FileServiceImpl implements FileService {

    private MimetypesFileTypeMap mimetypesFileTypeMap;

    private String staticPath;

    private String basePath;

    private String bucket;

    private String endpoint;
    private String outEndpoint;

    /**
     * The Client.
     */
    private MinioClient minioClient;

    /**
     * Instantiates a new File service.
     */
    public FileServiceImpl(String endPoint, String outEndpoint, String accessKey, String secretKey, String bucket, String basePath, String staticPath) {
        minioClient = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
        this.basePath = basePath;
        this.staticPath = staticPath;
        this.endpoint = endPoint;
        this.outEndpoint = outEndpoint;
        // 自动创建bucket
        makeBucket(bucket);
    }

    /**
     * 创建bucket
     * @param name
     */
    @Override
    public void makeBucket(String name) {
        // Check if the bucket already exists
        log.info("Start checkBucket with bucketName:{}", name);
        try {
            boolean isExist =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if(!isExist) {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
                log.info("Start makeBucket:{} , result:{}", name, "Success");
            }
            // 检查bucket 权限
            String bucketPolicy = minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(name).build());
            log.info("Start checkPolicy bucketName:{}, bucketPolicy:{} ,", name, bucketPolicy);
            if (bucketPolicy.length() == 0) {
                Map<String, Object> defaultBucketPolicy = makeDefaultBucketPolicy(name);
                JSONObject jsonObject = new JSONObject(defaultBucketPolicy);
                String defaultBucketPolicyJson = jsonObject.toString();
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(name).config(defaultBucketPolicyJson).build());
                log.info("Start setBucketPolicy bucketName:{}, with bucketPolicy:{} ,", name, defaultBucketPolicyJson);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("End checkBucket bucketName:{}, with bucketPolicy:{} ,", name);
    }

    /**
     * 构造默认minio bucket 默认读写权限
     * @param bucket
     * @return
     */
    private Map<String, Object> makeDefaultBucketPolicy(String bucket) {
        Map<String, Object> defaultPolicyMap = new HashMap<>();
        defaultPolicyMap.put("Version", "2012-10-17");
        Map<String, Object> statemenOne = new HashMap<>();
        statemenOne.put("Effect", "Allow");
        Map<String, Object> principalOne = new HashMap<>();
        principalOne.put("AWS",Arrays.asList("*"));
        statemenOne.put("Principal", principalOne);
        statemenOne.put("Action", Arrays.asList("s3:GetBucketLocation","s3:ListBucket","s3:ListBucketMultipartUploads"));
        statemenOne.put("Resource", Arrays.asList("arn:aws:s3:::".concat(bucket)));
        Map<String, Object> statemenTwo = new HashMap<>();
        statemenTwo.put("Effect", "Allow");
        statemenTwo.put("Principal", principalOne);
        statemenTwo.put("Action", Arrays.asList("s3:ListMultipartUploadParts","s3:PutObject","s3:AbortMultipartUpload","s3:DeleteObject","s3:GetObject"));
        statemenTwo.put("Resource", Arrays.asList("arn:aws:s3:::".concat(bucket).concat("/*")));
        defaultPolicyMap.put("Statement", Arrays.asList(statemenOne, statemenTwo));
        return defaultPolicyMap;
    }


    @Override
    public Optional<String> base64FileUpload(String base64Content, String prefixName, Boolean isStaticFile, List<String> tags, String bucket) {
        log.info("start base64FileUpload with Base64ContentLenth:【{}】, fileName:【{}】, bucket:【{}】,tags 【{}】", base64Content.length(), prefixName, bucket, tags);
        String newBucket = this.bucket;
        if (bucket != null) {
            newBucket = bucket;
        }
        Optional<String> objUrl = null;
        InputStream inputStream = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            String baseValue = base64Content.replaceAll(" ", "+");
            //去除base64中无用的部分
            byte[] b = decoder.decode(baseValue.substring(baseValue.indexOf(",") + 1));
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            inputStream = new ByteArrayInputStream(b);
            String baseKey;
            if (!isStaticFile) {
                // 目录格式化 日期前缀
                SimpleDateFormat baseKeyFormatter = new SimpleDateFormat("yyyy/MMdd");
                baseKey = baseKeyFormatter.format(new Date());
            } else {
                baseKey = staticPath;
            }
            // 添加basePath应用 前缀
            baseKey = StringUtils.isEmpty(basePath) ? baseKey : basePath.concat("/").concat(baseKey);
            if (!StringUtils.isEmpty(prefixName)) {
                baseKey = baseKey.concat("/").concat(prefixName);
            }

            // 重命名文件名
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_");
            String newFileName = formatter.format(new Date()).concat(String.valueOf(System.currentTimeMillis())).concat(".jpg");
            objUrl = this.upload(inputStream, baseKey.concat("/").concat(newFileName), tags, newBucket);
        }catch (Exception e){
            log.error("base64FileUpload() error:{}", e);
        }finally{
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("inputStream.close() error:{}", e);
            }
        }
        log.info("end base64FileUpload result:【{}】",objUrl);
        return objUrl;
    }


    @Override
    public Optional<String> base64FileUpload(String base64Content, String prefixName) {
        log.info("start base64FileUpload Base64ContentLenth:【{}】, fileName:【{}】", base64Content.length(), prefixName);
        Optional<String> result = this.base64FileUpload(base64Content, prefixName, false, null, null);
        log.info("end base64FileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> base64FileUpload(String base64Content, String prefixName, List<String> tags) {
        log.info("start base64FileUpload Base64ContentLenth:【{}】, fileName:【{}】", base64Content.length(), prefixName);
        Optional<String> result = this.base64FileUpload(base64Content, prefixName, false, tags, null);
        log.info("end base64FileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> base64FileUploadWithStatic(String base64Content, String prefixName) {
        log.info("start base64FileUpload Base64ContentLenth:【{}】, fileName:【{}】", base64Content.length(), prefixName);
        Optional<String> result = this.base64FileUpload(base64Content, prefixName, true,null, null);
        log.info("end base64FileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> base64FileUploadWithStatic(String base64Content, String prefixName, String bucket) {
        log.info("start base64FileUpload Base64ContentLenth:【{}】, fileName:【{}】", base64Content.length(), prefixName);
        Optional<String> result = this.base64FileUpload(base64Content, prefixName, true,null, bucket);
        log.info("end base64FileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> urlFileUpload(String fileUrl, String prefixName, Boolean rename, Boolean isStaticFile, List<String> tags, String bucket) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】, bucket:【{}】", fileUrl, prefixName, bucket);
        String newBucket = this.bucket;
        if (bucket != null) {
            newBucket = bucket;
        }
        Optional<String> objUrl = null;
        InputStream inputStream = null;
        HttpURLConnection httpUrl = null;
        try {
            URL url = new URL(fileUrl);
            httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.connect();
            inputStream = httpUrl.getInputStream();
            String baseKey;
            if (!isStaticFile) {
                // 目录格式化 日期前缀
                SimpleDateFormat baseKeyFormatter = new SimpleDateFormat("yyyy/MMdd");
                baseKey = baseKeyFormatter.format(new Date());
            } else {
                baseKey = staticPath;
            }
            // 添加basePath应用 前缀
            baseKey = StringUtils.isEmpty(basePath) ? baseKey : basePath.concat("/").concat(baseKey);
            if (!StringUtils.isEmpty(prefixName)) {
                baseKey = baseKey.concat("/").concat(prefixName);
            }

            // 重命名文件名
            String newFileName = null;
            if (rename) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_");
                String suffixName = fileUrl.substring(fileUrl.lastIndexOf("."));
                newFileName = formatter.format(new Date()).concat(String.valueOf(System.currentTimeMillis())).concat(suffixName);
            } else {
                newFileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
            }
            objUrl = this.upload(inputStream, baseKey.concat("/").concat(newFileName), tags, newBucket);
        }catch (Exception e){
            log.error("fileUpload() error:{}", e);
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("inputStream.close() error:{}", e);
            }
        }
        log.info("end fileUpload result:【{}】",objUrl);
        return objUrl;
    }

    @Override
    public Optional<String> urlFileUpload(String fileUrl, String prefixName, List<String> tags, Boolean isStaticFile) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", fileUrl, prefixName);
        Optional result = this.urlFileUpload(fileUrl, prefixName, true, isStaticFile, tags, null);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> urlFileUpload(String fileUrl, String prefixName, List<String> tags) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", fileUrl, prefixName);
        Optional result = this.urlFileUpload(fileUrl, prefixName, true, false, tags, null);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> urlFileUpload(String fileUrl, String prefixName) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", fileUrl, prefixName);
        Optional result = this.urlFileUpload(fileUrl, prefixName, true, false, null, null);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> urlFileUpload(String fileUrl, String prefixName, Boolean rename) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】,rename【{}】", fileUrl, prefixName, rename);
        Optional result = this.urlFileUpload(fileUrl, prefixName, false, false, null, null);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }



    @Override
    public Optional<String> fileUpload(String filePath, String prefixName) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, true, false, null, null, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUpload(String filePath, String prefixName, Boolean rename) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】,rename【{}】", filePath, prefixName, rename);
        Optional result = this.fileUpload(filePath, prefixName, rename, false, null, null, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUploadWithNoPurge(String filePath, String prefixName) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, true, false, null, null, false);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUploadWithNoPurge(String filePath, String prefixName, Boolean rename) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, rename, false, null, null, false);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, List<String> tags) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, rename, false, tags, null, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, List<String> tags, String bucket) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】, bucket:【{}】", filePath, prefixName, bucket);
        Optional result = this.fileUpload(filePath, prefixName, rename, false, tags, bucket, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUpload(String filePath, String prefixName, Boolean rename, Boolean isStaticFile, List<String> tags, String bucket, Boolean clearLocalFile) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】, bucket:【{}】", filePath, prefixName, bucket);
        String newBucket = this.bucket;
        if (bucket != null) {
            newBucket = bucket;
        }
        Optional<String> objUrl = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            String baseKey;
            if (!isStaticFile) {
                // 目录格式化 日期前缀
                SimpleDateFormat baseKeyFormatter = new SimpleDateFormat("yyyy/MMdd");
                baseKey = baseKeyFormatter.format(new Date());
            } else {
                baseKey = staticPath;
            }
            // 添加basePath应用 前缀
            baseKey = StringUtils.isEmpty(basePath) ? baseKey : basePath.concat("/").concat(baseKey);
            if (!StringUtils.isEmpty(prefixName)) {
                baseKey = baseKey.concat("/").concat(prefixName);
            }

            // 重命名文件名
            String newFileName = null;
            if (rename) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_");
                String suffixName = filePath.substring(filePath.lastIndexOf("."));
                newFileName = formatter.format(new Date()).concat(String.valueOf(System.currentTimeMillis())).concat(suffixName);
            } else {
                newFileName = filePath.substring(filePath.lastIndexOf("/")+1);
            }
            objUrl = this.upload(inputStream, baseKey.concat("/").concat(newFileName), tags, newBucket);
            // 清理缓存临时文件
            if (clearLocalFile) {
                FileSystemUtils.deleteRecursively(Paths.get(filePath));
            }
        }catch (Exception e){
            log.error("fileUpload() error:{}", e);
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("inputStream.close() error:{}", e);
            }
        }
        log.info("end fileUpload result:【{}】",objUrl);
        return objUrl;
    }

    @Override
    public Optional<String> fileUploadWithStatic(String filePath, String prefixName) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, true, true, null, null, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUploadWithStatic(String filePath, String prefixName, Boolean rename) {
        log.info("start fileUploadWithStatic withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, rename, true, null, null, true);
        log.info("end fileUploadWithStatic result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUploadWithStatic(String filePath, String prefixName, String bucket) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, true, true, null, bucket, true);
        log.info("end fileUpload result:【{}】",result);
        return result;
    }

    @Override
    public Optional<String> fileUploadWithStatic(String filePath, String prefixName, String bucket, Boolean rename) {
        log.info("start fileUploadWithStatic withData:【{}】, fileName:【{}】", filePath, prefixName);
        Optional result = this.fileUpload(filePath, prefixName, rename, true, null, bucket, true);
        log.info("end fileUploadWithStatic result:【{}】",result);
        return result;
    }

    /**
     * 图片上传操作，上传完成后，执行后续操作
     * @param file
     * @param makeParamFunction  构造图像结构话分析参数
     * @param analyzerFunction   执行结构话分析调用
     * @return
     */
    @Override
    public Optional<Object> imageUploadWithStaticBlock(File file, String prefixName, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction) {
        Optional<String> imageUrlOpt = this.fileUpload(file.getPath(), prefixName, true, true, null, null, true);
        // 需要进行图片结构化分析执行如下
        if (makeParamFunction != null && analyzerFunction != null) {
            Object uploadResult = makeParamFunction.apply(imageUrlOpt);
            return analyzerFunction.apply(uploadResult);
        }
        return Optional.ofNullable(imageUrlOpt.get());
    }

    @Override
    public Optional<Object> imageUploadBlock(File file, String prefixName, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction) {
        Optional<String> imageUrlOpt = this.fileUpload(file.getPath(), prefixName, true, false, null, null, true);
        // 需要进行图片结构化分析执行如下
        if (makeParamFunction != null && analyzerFunction != null) {
            Object uploadResult = makeParamFunction.apply(imageUrlOpt);
            return analyzerFunction.apply(uploadResult);
        }
        return Optional.ofNullable(imageUrlOpt.get());
    }

    @Override
    public Optional<Object> imageUploadBlock(File file, String prefixName, List<String> tags, Function<Optional<String>, Object> makeParamFunction, Function<Object, Optional<Object>> analyzerFunction) {
        Optional<String> imageUrlOpt = this.fileUpload(file.getPath(), prefixName, true, false, tags, null, true);
        // 需要进行图片结构化分析执行如下
        if (makeParamFunction != null && analyzerFunction != null) {
            Object uploadResult = makeParamFunction.apply(imageUrlOpt);
            return analyzerFunction.apply(uploadResult);
        }
        return Optional.ofNullable(imageUrlOpt.get());
    }


    /**
     * 文件流方式上传文件
     * @param inputStream  流内容
     * @param fileName   文件名
     * @param bucket     bucket
     * @return      成功后的url地址
     */
    private Optional<String> upload(InputStream inputStream, String fileName, List<String> tags, String bucket){
        log.info("start upload withData:【{}】, fileName:【{}】, bucket:【{}】",inputStream, fileName, bucket);
        String objUrl = null;
        try {
            // 上传object 时添加tags
            Map<String, String> finalTags = new HashMap<>();
            if (!CollectionUtils.isEmpty(tags)) {
                final Map<String, String> finalTags1 = finalTags;
                tags.stream()
                    .filter(tagStr -> tagStr.contains("="))
                    .map(tagStr -> tagStr.split("="))
                    .forEach(tagItem -> {
                        finalTags1.put(tagItem[0], tagItem[1]);
                   });
            }
            String newFileName = fileName;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .tags(finalTags)
                    .object(newFileName)
                    .stream(inputStream,-1, 20971520)
                    .contentType(getContentType(newFileName))
                    .build());
            objUrl = minioClient.getObjectUrl(bucket, newFileName);
            // 替换文件主机头信息
            String finalObjUrl = objUrl;
            objUrl = Optional.ofNullable(outEndpoint)
                    .map(outPoint -> finalObjUrl.replace(endpoint, outPoint))
                    .orElse(objUrl);
        } catch(Exception e) {
            log.error("upload() error:{}", e);
        }
        log.info("end upload result:【{}】",objUrl);
        return Optional.ofNullable(objUrl);
    }


    /**
     * 获取上传文件的文件类型
     * @param fileName
     * @return
     */
    private String getContentType(String fileName) {
        if (mimetypesFileTypeMap == null) {
            mimetypesFileTypeMap = new MimetypesFileTypeMap();
        }
        return mimetypesFileTypeMap.getContentType(fileName);
    }

    public Optional<String> fileExportUpload(String filePath, String prefixName, Boolean isStaticFile, List<String> tags, String bucket) {
        log.info("start fileUpload withData:【{}】, fileName:【{}】, bucket:【{}】", filePath, prefixName, bucket);
        String newBucket = this.bucket;
        if (bucket != null) {
            newBucket = bucket;
        }
        Optional<String> objUrl = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            String baseKey;
            if (!isStaticFile) {
                // 目录格式化 日期前缀
                SimpleDateFormat baseKeyFormatter = new SimpleDateFormat("yyyy/MMdd");
                baseKey = baseKeyFormatter.format(new Date());
            } else {
                baseKey = staticPath;
            }
            // 添加basePath应用 前缀
            baseKey = StringUtils.isEmpty(basePath) ? baseKey : basePath.concat("/").concat(baseKey);
            if (!StringUtils.isEmpty(prefixName)) {
                baseKey = baseKey.concat("/").concat(prefixName);
            }
            // 重命名文件名
            String newFileName = filePath.substring(filePath.lastIndexOf("/")+1);
            objUrl = this.upload(inputStream, baseKey.concat("/").concat(newFileName), tags, newBucket);
            File delfile = new File(filePath);
            this.deleteFile(delfile);
        }catch (Exception e){
            log.error("fileExportUpload() error:{}", e);
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("inputStream.close() error:{}", e);
            }
        }
        log.info("end fileUpload result:【{}】",objUrl);
        return objUrl;
    }
    /**
     * 删除文件
     * @param file
     * @return
     */
    public  boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
