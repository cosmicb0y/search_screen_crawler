package com.hackday.crawler.job;

import com.hackday.crawler.domain.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class KeywordItemProcessor implements ItemProcessor<Keyword, Keyword> {
    private static final Logger log = LoggerFactory.getLogger(KeywordItemProcessor.class);

    @Override
    public Keyword process(final Keyword keyword) throws Exception {
        return keyword;
    }
}
