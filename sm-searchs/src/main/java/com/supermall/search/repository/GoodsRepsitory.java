package com.supermall.search.repository;

import com.supermall.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepsitory extends ElasticsearchRepository<Goods,Long> {
}
