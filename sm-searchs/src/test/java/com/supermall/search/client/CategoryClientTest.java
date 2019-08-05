package com.supermall.search.client;

import com.supermall.SmSearchApplication;
import com.supermall.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmSearchApplication.class)
public class CategoryClientTest {
    @Autowired
    CategoryClient categoryClient;
    @Test
    public void queryCategoryByIds(){
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(1l, 2l, 3l));
        Assert.assertEquals(3,categories.size());
        for (Category category : categories) {
            System.out.println(category);
        }
    }
}
