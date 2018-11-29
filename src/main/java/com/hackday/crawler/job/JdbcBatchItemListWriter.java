package com.hackday.crawler.job;

import com.hackday.crawler.domain.Screenshot;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class JdbcBatchItemListWriter implements ItemWriter<List<Screenshot>> {


    @Override
    public void write(List<? extends List<Screenshot>> list) throws Exception {
        // need implement
    }
}
