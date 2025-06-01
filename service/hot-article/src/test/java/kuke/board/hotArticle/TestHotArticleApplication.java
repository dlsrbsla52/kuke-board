package kuke.board.hotArticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "kuke.board.hotarticle")
public class TestHotArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestHotArticleApplication.class, args);
    }
}