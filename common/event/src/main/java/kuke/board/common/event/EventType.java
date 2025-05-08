package kuke.board.common.event;

import kuke.board.common.event.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.KUKE_BOARD_ARTICLE), // 게시글 생성
    ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.KUKE_BOARD_ARTICLE), // 게시글 수정
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.KUKE_BOARD_ARTICLE), // 게시글 삭제
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.KUKE_BOARD_COMMENT), // 댓글 생성
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.KUKE_BOARD_COMMENT), // 댓글 삭제
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.KUKE_BOARD_LIKE), // article 좋아요
    ARTICLE_UNLIKED(ArticleUnLikedEventPayload.class, Topic.KUKE_BOARD_LIKE), // article 좋아요 취소
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.KUKE_BOARD_VIEW); // article 뷰어 수
    
    private final Class<? extends EventPayload> payloadClass;
    private final String topic;
    
    public static EventType from(String type){
        try {
            return EventType.valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from]. type={}", type, e);
            return null;
        }
    }
    
    public static class Topic{
        public static final String KUKE_BOARD_ARTICLE = "kuke-board-article";
        public static final String KUKE_BOARD_COMMENT = "kuke-board-comment";
        public static final String KUKE_BOARD_LIKE = "kuke-board-like";
        public static final String KUKE_BOARD_VIEW = "kuke-board-view";
        
        
    }
}
