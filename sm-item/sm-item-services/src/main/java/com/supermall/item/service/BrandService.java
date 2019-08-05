package com.supermall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.vo.PageResult;
import com.supermall.item.mapper.BrandMapper;
import com.supermall.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if(StringUtils.isNoneBlank(sortBy)){
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new SmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        return new PageResult<>(pageInfo.getTotal(),brands);

    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if(count !=1){
            throw new SmException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增品牌分类中间表
        for(Long cid :cids){
            brandMapper.insertCategoryBrand(cid,brand.getId());
            if(count!=1){
                throw new SmException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    public Brand queryBrandById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand == null){
            throw new SmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return  brand;
    }


    public List<Brand> queryBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.queryBrandsByCategoryId(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new SmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return  brands;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(brands)){
            throw new SmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
