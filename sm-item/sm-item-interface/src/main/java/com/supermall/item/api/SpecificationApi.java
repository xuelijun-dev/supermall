package com.supermall.item.api;

import com.supermall.item.pojo.SpecGroup;
import com.supermall.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi  {
    @GetMapping("spec/groups/{cid}")
    List<SpecGroup> querySpecGroups(@PathVariable("cid") Long cid);
    /**
     * 查询分类规格参数组合
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParam> queryParamByList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching);

    // 查询规格参数组，及组内参数
    @GetMapping("spec/{cid}")
    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);

    // 查询规格参数组，及组内参数
    @GetMapping("spec/group")
    List<SpecGroup> queryListByCid(@RequestParam("cid") Long cid);


}
