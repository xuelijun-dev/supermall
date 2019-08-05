package com.supermall.upload.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties prop;
   // private static final List<String> ALLOW_TYPES = Arrays.asList("image/gif","image/jpg","image/jpeg","image/png","image/bmp");
    public String uploadImage(MultipartFile file) {

        try {
            String contentType = file.getContentType();
            // 1)校验文件类型
            if(!prop.getAllowTypes().contains(contentType)){
                throw new SmException(ExceptionEnum.INVAID_FILE_TYPE);
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.info("上传失败，文件内容不符合要求");
                throw new SmException(ExceptionEnum.INVAID_FILE_TYPE);
            }
            //上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);


            // 2、保存图片
            //准备目标路径
//            File dest = new File("F:\\video\\6.乐优商城项目\\乐优商城《项目笔记》\\");
//            if (!dest.exists()) {
//                dest.mkdirs();
//            }
//            File dest = new File("F:\\video\\6.乐优商城项目\\乐优商城《项目笔记》\\"+file.getOriginalFilename());
            //保存文件本地
//            file.transferTo(new File(dest, file.getOriginalFilename()));
//            file.transferTo(dest);
            //返回路径
            return prop.getBaseUrl()+storePath.getFullPath();
            //上传失败
        } catch (IOException e) {
            log.error("上传失败！！！");
            throw new SmException(ExceptionEnum.INVAID_FILE_TYPE);
        }

    }
}
