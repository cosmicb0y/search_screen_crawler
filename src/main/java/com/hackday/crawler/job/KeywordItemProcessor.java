package com.hackday.crawler.job;

import com.hackday.crawler.domain.Keyword;
import com.hackday.crawler.domain.Screenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeywordItemProcessor implements ItemProcessor<Keyword, List<Screenshot>> {
    private static final Logger log = LoggerFactory.getLogger(KeywordItemProcessor.class);

    private final HashMap checker;

    @Autowired
    public KeywordItemProcessor(HashMap checker) {
        this.checker = checker;
    }

    @Override
    public List<Screenshot> process(final Keyword keyword) throws Exception {

        String chromeDriverPath = "/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

        String url = "https://search.naver.com/search.naver?query=" + URLEncoder.encode(keyword.getQuery(), "UTF-8");
        driver.get(url);

        List<WebElement> elements = driver.findElements(By.xpath("//div[@data-dss-logarea='" + keyword.getArea() + "']"));

        Thread.sleep(200);

        if (elements.size() == 0) {
            if (!checker.containsKey(keyword.getArea() + keyword.getType())) {
                checker.put(keyword.getArea() + keyword.getArea(), false);
            }
            return null;
        }

        List<Screenshot> screenshots = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            String filePath = "/Users/cosmicboy/IdeaProjects/screenshot/" + keyword.getQuery() + (i + 1) +".png";
            FileUtils.copyFile(elements.get(i).getScreenshotAs(OutputType.FILE), new File(filePath));
            screenshots.add(new Screenshot(keyword.getArea(), keyword.getType(), filePath));
        }


        return screenshots;
    }
}
