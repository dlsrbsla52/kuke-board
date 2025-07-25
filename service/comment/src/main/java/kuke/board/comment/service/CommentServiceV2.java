package kuke.board.comment.service;

import jakarta.transaction.Transactional;
import kuke.board.comment.entity.ArticleCommentCount;
import kuke.board.comment.entity.CommentPath;
import kuke.board.comment.entity.CommentV2;
import kuke.board.comment.repository.ArticleCommentCountRepository;
import kuke.board.comment.repository.CommentRepositoryV2;
import kuke.board.comment.service.Request.CommentCreateRequestV2;
import kuke.board.comment.service.Response.CommentPageResponse;
import kuke.board.comment.service.Response.CommentResponse;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.*;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepositoryV2 commentRepositoryV2;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequestV2 request) {
        CommentV2 parent = findParent(request);
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();
        CommentV2 comment = commentRepositoryV2.save(
                CommentV2.create(
                        snowflake.nextId(),
                        request.getContent(),
                        request.getArticleId(),
                        request.getWriterId(),
                        parentCommentPath.createChildCommentPath(
                                commentRepositoryV2.findDescendantsTopPath(request.getArticleId(), parentCommentPath.getPath())
                                        .orElse(null)
                        )
                )
        );

        
        if(articleCommentCountRepository.increase(request.getArticleId()) == 0) {
            articleCommentCountRepository.save(
                    ArticleCommentCount.init(request.getArticleId(), 1L)
                    );
        }
        
        return CommentResponse.from(comment);
    }

    private CommentV2 findParent(CommentCreateRequestV2 request) {
        String parentPath = request.getParentPath();
        if(parentPath == null) {
            return null;
        }
        
        return commentRepositoryV2.findByPath(parentPath)
                .filter(not(CommentV2::getDeleted))
                .orElseThrow();

    }
    
    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepositoryV2.findById(commentId).orElseThrow()
        );
    }
    
    @Transactional
    public void delete(Long commentId) {
        commentRepositoryV2 .findById(commentId)
                .filter(not(CommentV2::getDeleted))
                .ifPresent(comment -> {
                    if(hasChildren(comment)) {
                        comment.delete();
                    }else {
                        delete(comment);
                    }
                });
    }

    private boolean hasChildren(CommentV2 comment) {
        
        return commentRepositoryV2.findDescendantsTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }
    
    private void delete(CommentV2 comment){
        commentRepositoryV2.delete(comment);
        articleCommentCountRepository.decrease(comment.getArticleId());
        if (!comment.isRoot()) {
            commentRepositoryV2.findByPath(comment.getCommentPath().getParentPath())
                    .filter(CommentV2::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }
    
    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize){
        return CommentPageResponse.of(
                commentRepositoryV2.findAll(articleId, (page -1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from)
                        .toList(),
                commentRepositoryV2.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }
    
    public List<CommentResponse> readAllInfiniteScroll(Long articleId, String lastPath, Long pageSize){
        List<CommentV2> comments = lastPath == null ?
                commentRepositoryV2.findAllInfiniteScroll(articleId, pageSize) :
                commentRepositoryV2.findAllInfiniteScroll(articleId, lastPath, pageSize);
        
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }
    
    public Long count(Long articleId){
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }
}
