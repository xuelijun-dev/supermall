package com.supermall.search.repository;

import com.supermall.SmSearchApplication;
import com.supermall.common.vo.PageResult;
import com.supermall.item.pojo.Spu;
import com.supermall.search.pojo.Goods;
import com.supermall.search.client.GoodsClient;
import com.supermall.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmSearchApplication.class)
public class GoodsRepsitoryTest {
    @Autowired
    GoodsRepsitory goodsRepsitory;
    @Autowired
    ElasticsearchTemplate template;
    @Autowired
    GoodsClient goodsClient;
    @Autowired
    SearchService searchService;
    @Test
    public void testCreateIndex(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }
    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do{
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> items = result.getItems();
            if(CollectionUtils.isEmpty(items)){
                break;
            }
            List<Goods> goodslist = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
            goodsRepsitory.saveAll(goodslist);
            page++;
            size = items.size();
        }while (size==100);
    }


}