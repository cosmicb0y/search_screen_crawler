package com.hackday.crawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screenshot {
    private String area;
    private String type;
    private String filePath;
}
