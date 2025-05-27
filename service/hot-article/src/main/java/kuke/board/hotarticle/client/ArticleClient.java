package kuke.board.hotarticle.client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleClient {
    
    private RestClient restClient;
    
    @Value("${endpoints.kuke-board-article-service.url}")
    private String articleServiceUrl;
    
    @PostConstruct
    void initRestClient(){
        restClient = RestClient.create(articleServiceUrl);
    }
    
    
    @Getter
    public static class ArticleResponse {
        
    }
}
