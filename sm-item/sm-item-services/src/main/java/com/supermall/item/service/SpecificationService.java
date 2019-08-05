package com.supermall.item.service;

import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.item.mapper.SpecGroupMapper;
import com.supermall.item.mapper.SpecParamMapper;
import com.supermall.item.pojo.SpecGroup;
import com.supermall.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new SmException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecParam> queryParamByList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new SmException(ExceptionEnum.SPEC_GROUP_PARAM_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup>  queryListByCid(Long cid) {
//         查询规格组
        List<SpecGroup> specGroups = this.queryGroupByCid(cid);
        // 查询组内参数
        List<SpecParam> specParams = queryParamByList(null, cid, null);
        //key是规格组的id，值是组下的所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        for (SpecParam specParam : specParams) {
            if(!map.containsKey(specParam.getGroupId())){
                map.put(specParam.getGroupId(),new ArrayList<>());
            }
            map.get(specParam.getGroupId()).add(specParam);
        }
        //填param到group中
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));

        }
        return specGroups;
    }
//    public List<SpecGroup> querySpecsByCid(Long cid) {
//        // 查询规格组
//        List<SpecGroup> groups = this.querySpecGroups(cid);
//        SpecParam param = new SpecParam();
//        groups.forEach(g -> {
//            // 查询组内参数
//            g.setParams(this.querySpecParams(g.getId(), null, null, null));
//        });
//        return groups;
//    }

}
