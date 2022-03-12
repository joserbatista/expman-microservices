package com.joserbatista.service.user.outbound.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackageClasses = MongoConfig.class)
public class MongoConfig {

    // support for OffsetDateTime
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(new ReadingConverter(), new WritingConverter()));
    }

    private static class ReadingConverter implements Converter<Date, OffsetDateTime> {

        @Override
        public OffsetDateTime convert(Date date) {
            return date.toInstant().atOffset(ZoneOffset.UTC);
        }
    }


    private static class WritingConverter implements Converter<OffsetDateTime, Date> {

        @Override
        public Date convert(OffsetDateTime offsetDateTime) {
            return Date.from(offsetDateTime.toInstant());
        }
    }
}
