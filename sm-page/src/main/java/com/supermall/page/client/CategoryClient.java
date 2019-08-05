package com.supermall.page.client;

import com.supermall.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;
@FeignClient("item-services")
public interface CategoryClient  extends CategoryApi {
}
