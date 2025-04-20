package kuke.board.comment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {
    
    @Id
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId; // shard key
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createAt;
    
    
    public static Comment create(Long commentId, String content, Long parentCommentId, Long articleId, Long writerId) {
        Comment comment = new Comment();
        comment.commentId = commentId;
        comment.content = content;
        comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
        comment.articleId = articleId;
        comment.writerId = writerId;
        comment.deleted = Boolean.FALSE;
        comment.createAt = LocalDateTime.now();
        return comment;
    }
    
    public boolean isRoot() {
        return parentCommentId.longValue() == commentId;
    }
    
    public void delete() {
        deleted = true;
    }
}
