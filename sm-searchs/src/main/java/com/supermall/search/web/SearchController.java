package com.supermall.search.web;

import com.supermall.common.vo.PageResult;
import com.supermall.item.pojo.Category;
import com.supermall.search.pojo.Goods;
import com.supermall.search.pojo.SearchRequest;
import com.supermall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    SearchService searchService;

    /**
     * 搜索商品
     *
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request));
    }

    @GetMapping("page/test")
    public ResponseEntity<String> search(){
        return ResponseEntity.ok("hello");
    }

}
