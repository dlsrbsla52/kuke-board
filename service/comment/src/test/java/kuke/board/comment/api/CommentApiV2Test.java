package kuke.board.comment.api;

import kuke.board.comment.service.Request.CommentCreateRequestV2;
import kuke.board.comment.service.Response.CommentPageResponse;
import kuke.board.comment.service.Response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {
    
    RestClient restClient = RestClient.create("http://localhost:9001");
    
    
    @Test
    void create(){
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment3", response2.getPath(), 1L));

        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());

        System.out.println("response1.getPath()() = " + response1.getPath());
        System.out.println("\tresponse2.getPath()() = " + response2.getPath());
        System.out.println("\t\tresponse3.getPath()() = " + response3.getPath());
    }
    
    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }
    
    @Test
    void read(){
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentId}", 176948909897134080L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println("response = " + response);
    }
    
    @Test
    void delete(){
        restClient.delete()
                .uri("/v2/comments/{commentId}", 176948909897134080L)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {

        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;

    }
    
    @Test
    void readAll(){
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=50000")
                .retrieve()
                .body(CommentPageResponse.class);
        
        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("commentId = " + comment.getCommentId());
        }
        
        /*
            commentId = 176951365499404293
            commentId = 176951365591678978
            commentId = 176951365591678987
            commentId = 176951365591678996
            commentId = 176951365591679006
            commentId = 176951365591679015
            commentId = 176951365591679024
            commentId = 176951365595873284
            commentId = 176951365595873293
            commentId = 176951365595873302
        
            commentId = 176951365499404293
            commentId = 176951365591678978
            commentId = 176951365591678987
            commentId = 176951365591678996
            commentId = 176951365591679006
            commentId = 176951365591679015
            commentId = 176951365591679024
            commentId = 176951365595873284
            commentId = 176951365595873293
            commentId = 176951365595873302
        */
    }
    
    @Test
    void readAllInfiniteScroll(){
        List<CommentResponse> response1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        System.out.println("response1 = " + response1);

        for (CommentResponse comment : response1) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }


        String lastPath = response1.getLast().getPath();
        List<CommentResponse> response2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=%s".formatted(lastPath))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        System.out.println("response2 = " + response2);
        
        for (CommentResponse comment : response2) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }
}
