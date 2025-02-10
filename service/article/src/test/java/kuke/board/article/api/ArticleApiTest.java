package kuke.board.article.api;

import kuke.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleApiTest {

    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest(){
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));

        System.out.println("response = " + response);
    }


    @Test
    void readTest(){
        ArticleResponse response = read(147365744120795136L);
        System.out.println("response = " + response);
    }

    @Test
    void updateTest(){
        update(147365744120795136L);
        ArticleResponse response = read(147365744120795136L);

        System.out.println("response = " + response);
    }


    @Test
    void deleteTest(){
        restClient.delete()
                .uri("/v1/articles/{articleId}", 147365744120795136L)
                .retrieve();
    }


    void update(Long articleId){
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi2", "my content 22"))
                .retrieve()
                .body(ArticleResponse.class);

    }


    ArticleResponse read(Long articleId){
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }


    ArticleResponse create(ArticleCreateRequest request){
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

}
