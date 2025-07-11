package com.easyapi.minio.configure;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "vmr.minio")
public class MinioProperties {

    private String endpoint;

    /**
     * 处理进群部署方式时，需要返回集群外地址
     */
    private String outEndpoint;

    private String accessKey;

    private String secretKey;

    private String bucket = "default";
    // 应用根目录
    private String basePath = "project";
    // 静态文件根路径
    private String staticPath = "static";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public String getOutEndpoint() {
        return outEndpoint;
    }

    public void setOutEndpoint(String outEndpoint) {
        this.outEndpoint = outEndpoint;
    }
}
