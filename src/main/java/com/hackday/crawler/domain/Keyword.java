package com.hackday.crawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {
    private String area;
    private String query;
    private String type;
    private int rank;
}
