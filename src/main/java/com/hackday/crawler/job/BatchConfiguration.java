package com.hackday.crawler.job;

import com.hackday.crawler.domain.Keyword;
import com.hackday.crawler.domain.Screenshot;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Keyword> reader() {
        return new FlatFileItemReaderBuilder<Keyword>()
                .name("keywordItemReader")
                .resource(new ClassPathResource("sample.csv"))
                .linesToSkip(1)
                .delimited()
                .names(new String[]{"area","query","type","rank"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Keyword>() {{
                    setTargetType(Keyword.class);
                }})
                .build();
    }

    @Bean
    public HashMap<String, Boolean> checker() { return new HashMap<>(); }

    @Bean
    public KeywordItemProcessor processor() {
        return new KeywordItemProcessor(checker());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        BasicDataSource source = new BasicDataSource();

        source.setDriverClassName("com.mysql.cj.jdbc.Driver");


        return new JdbcTemplate(source);

    }

    @Bean
    public ScreenshotWriter screenshotWriter(JdbcTemplate jdbcTemplate) {
        return new ScreenshotWriter(jdbcTemplate);

    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(ScreenshotWriter writer) {
        return stepBuilderFactory.get("step1")
                .<Keyword, List<Screenshot>> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]

}
