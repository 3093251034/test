package com.czd;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest
@Data
@ContextConfiguration(classes = BlogApplication.class)
public class OSSTest {

//    @Value(value = "${oss.AK}")
//    private String AK;
    @Value(value = "oss.SK")
    private String SK;


    @Test
    public void test(){
//        System.out.println(AK);
        System.out.println(SK);
    }
}
