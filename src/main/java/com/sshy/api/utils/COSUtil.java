package com.sshy.api.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.DeleteObjectsResult;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class COSUtil {

    public static String uploadFile(String key , File localFile){
        COSCredentials cred = new BasicCOSCredentials("AKIDQlzHUhJ14x44sxtlT5dP5aGcpsrLrwxo", "Nhv1wLdkU7btztc8ThjeR7h6JOK5ijfU");
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosclient = new COSClient(cred, clientConfig);
        String bucketName = "maochangan-1256949445";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
        Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
        URL url = cosclient.generatePresignedUrl(bucketName, key, expiration);
        return url.toString();
    }

    public static boolean deleteFile(String key){
        try {
            COSCredentials cred = new BasicCOSCredentials("AKIDQlzHUhJ14x44sxtlT5dP5aGcpsrLrwxo", "Nhv1wLdkU7btztc8ThjeR7h6JOK5ijfU");
            ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
            COSClient cosclient = new COSClient(cred, clientConfig);
            String bucketName = "maochangan-1256949445";
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
            cosclient.deleteObject(deleteObjectRequest);
            return true;
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        }
    }

}
