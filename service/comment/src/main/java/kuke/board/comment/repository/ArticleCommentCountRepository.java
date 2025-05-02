package kuke.board.comment.repository;

import kuke.board.comment.entity.ArticleCommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount,Long> {

    @Query(
            value = "update article_comment_count set comment_count = comment_count + 1 where articleId = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param(value = "articleId") Long articleId);

    @Query(
            value = "update article_comment_count set comment_count = comment_count - 1 where articleId = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param(value = "articleId") Long articleId);
}
