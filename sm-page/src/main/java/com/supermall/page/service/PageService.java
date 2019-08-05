package com.supermall.page.service;

import com.supermall.item.pojo.*;
import com.supermall.page.client.BrandClient;
import com.supermall.page.client.CategoryClient;
import com.supermall.page.client.GoodsClient;
import com.supermall.page.client.SpecificationClient;
import com.supermall.page.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model =new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        //查询详情
        SpuDetail detail = spu.getSpuDetail();
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specificationClient.queryListByCid(spu.getCid3());
        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("brand",brand);
        model.put("detail",detail);
        model.put("categories",categories);
        model.put("specs",specs);
        return model;
    }

    public void createHtml(Long spuId) {
        PrintWriter writer = null;
        try{
            // 获取页面数据
            Map<String, Object> spuMap = loadModel(spuId);
            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariables(spuMap);
            // 创建输出流
            File dest = new File("F:\\video\\6.乐优商城项目\\"+spuId+".html");
            if(dest.exists()){
                dest.delete();
            }
            writer = new PrintWriter(dest,"UTF-8");
            // 执行页面静态化方法
            templateEngine.process("item",context,writer);
        } catch (Exception e) {
            log.error("页面静态化出错：{}，" + e, spuId);
        }finally {
            if (writer != null) {
                writer.close();
            }
        }

    }
    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    public void deleteHtml(Long spuId) {
        File dest = new File("F:\\video\\6.乐优商城项目\\"+spuId+".html");
        if(dest.exists()){
            dest.delete();
        }
    }
}
