package com.hackday.crawler.job;

import com.hackday.crawler.domain.Screenshot;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ScreenshotWriter implements ItemWriter<List<Screenshot>> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ScreenshotWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void write(List<? extends List<Screenshot>> list) throws Exception {
        for (List<Screenshot> screenshots: list) {
            if (screenshots == null) continue;

            int count = screenshots.size();
            jdbcTemplate.update("REPLACE INTO screenshots (area, searchType, num) VALUES (?, ?, ?)"
                                ,screenshots.get(0).getArea(), screenshots.get(0).getType(), count);

            for (int i = 0; i < screenshots.size(); i++) {
                jdbcTemplate.update("REPLACE INTO paths (area, searchType, path) VALUES (?, ?, ?)"
                                    , screenshots.get(i).getArea(), screenshots.get(i).getType()
                                    , screenshots.get(i).getFilePath());
            }

        }
    }
}
