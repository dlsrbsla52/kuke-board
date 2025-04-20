package kuke.board.comment.api;

import kuke.board.comment.service.Response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create(){
        CommentResponse comment1 = createComment(new CommentCreateRequest(1L, "My comment1", null, 1L));
        CommentResponse comment2 = createComment(new CommentCreateRequest(1L, "My comment2", comment1.getCommentId(), 1L));
        CommentResponse comment3 = createComment(new CommentCreateRequest(1L, "My comment3", comment1.getCommentId(), 1L));


        System.out.printf("comment1=%s", comment1.getCommentId());
        System.out.printf("\tcomment2=%s", comment2.getCommentId());
        System.out.printf("\tcomment3=%s", comment3.getCommentId());

//        154229919798927360
//        154229937121402880
//        154230043790942208
    }
    
    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
                
    }
    
    @Test
    void read(){
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 154229919798927360L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }
    
    @Test
    void delete(){
//        154230914562007040
//        154230920480169984
//        154230934778552320

        restClient.delete()
                .uri("/v1/comments/{commentId}", 154230914562007040L)
                .retrieve();
    }
    
     
    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {

        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;

    }
}
