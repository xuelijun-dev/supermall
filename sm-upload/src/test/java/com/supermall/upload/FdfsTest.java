package com.supermall.upload;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FdfsTest {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig imageConfig;
    @Test
    public void testUpload() throws FileNotFoundException {
        File file =  new File("C:\\Users\\Administrator\\Desktop\\相册\\大众品.jpg");
        StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());
    }
    @Test
    public void testUploadAndThump() throws FileNotFoundException {
        File file =  new File("C:\\Users\\Administrator\\Desktop\\相册\\大众品.jpg");
        // 上传并且生成缩略图
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), file.length(), "jpg", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String thumbImagePath = imageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(thumbImagePath);

    }
}
