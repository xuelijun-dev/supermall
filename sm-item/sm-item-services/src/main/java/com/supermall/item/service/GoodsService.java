package com.supermall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import com.supermall.common.dto.CartDTO;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.vo.PageResult;
import com.supermall.item.mapper.SkuMapper;
import com.supermall.item.mapper.SpuDetailMapper;
import com.supermall.item.mapper.SpuMapper;
import com.supermall.item.mapper.StockMapper;
import com.supermall.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("title","%"+key+"%");
        }
        //排序
        if(saleable != null){
            example.setOrderByClause("last_update_time DESC");
        }
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(spus)){
            throw new SmException(ExceptionEnum.GOOD_NOT_FOUND);
        }
        loadCategoryAndBrandName(spus);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        return new PageResult<>(spuPageInfo.getTotal(),spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for(Spu spu : spus){
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));
            //处理品牌名称
            spu.setBname(brandService.queryBrandById(spu.getBrandId()).getName());
        }
    }
    @Transactional
    public void saveGoods(Spu spu) {

        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);

        // 保存spu详情
        spu.getSpuDetail().setSpuId(spu.getId());
        this.spuDetailMapper.insert(spu.getSpuDetail());
        // 保存sku和库存信息
        saveSkuAndStock(spu.getSkus(), spu.getId());
        //发送mq消息
        amqpTemplate.convertAndSend("item.insert",spu.getId());
    }
    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                continue;
            }
            // 保存sku
            sku.setSpuId(spuId);
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }
    @Transactional
    public void update(Spu spu) {
        // 查询以前sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            skuMapper.delete(record);
            //发送mq消息
            amqpTemplate.convertAndSend("item.update",spu.getId());
        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
    }
    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        if(spu  == null){
            throw new SmException(ExceptionEnum.SPUDETAIL_NOT_FOUNT);
        }
        spu.setSkus(skuService.querySkuBySpuId(id));
        spu.setSpuDetail(spuDetailService.queryDetailById(id));
        return spu;
    }

    @Transactional
    public void decreaseStock(List<CartDTO> carts) {
        for (CartDTO cart : carts) {
            int count = stockMapper.descreaseStock(cart.getSkuId(), cart.getNum());
            if(count != 1){
                throw new SmException(ExceptionEnum.STOCK_IS_NOT_ENGOUGH);
            }
        }
    }
}
