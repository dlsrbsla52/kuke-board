package kuke.board.like.controller;

import kuke.board.like.service.ArticleLikeService;
import kuke.board.like.service.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {
    
    private final ArticleLikeService articleLikeService;
    
    @GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public ArticleLikeResponse read(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {
        
        return articleLikeService.read(articleId, userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void likePessimisticLock1(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.likePessimisticLock1(articleId, userId);
    }

    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void unlikePessimisticLock1(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.unlikePessimisticLock1(articleId, userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void likePessimisticLock2(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.likePessimisticLock2(articleId, userId);
    }

    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void unlikePessimisticLock2(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.unlikePessimisticLock2(articleId, userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
    public void likeOptimisticLock(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.likeOptimisticLock(articleId, userId);
    }

    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
    public void unlikeOptimisticLock(
            @PathVariable(value = "articleId") Long articleId,
            @PathVariable(value = "userId") Long userId) {

        articleLikeService.unlikeOptimisticLock(articleId, userId);
    }
}
