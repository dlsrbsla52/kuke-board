package kuke.board.hotarticle.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleLikeCountRepository {
    
    private final StringRedisTemplate redisTemplate;
    
    // hot-article::article::{articleId}::like-count
    private static final String KEY_FORMAT = "hot-article::article::{articleId}::like-count";
    
    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
    
    public void createOrUpdate(Long articleId, Long likeCount, Duration ttl){
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(likeCount), ttl);
    }
    
    public Long read(Long articleId){  
        String result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.valueOf(result);
    }
}
