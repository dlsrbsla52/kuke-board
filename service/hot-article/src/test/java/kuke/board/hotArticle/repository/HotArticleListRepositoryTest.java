package kuke.board.hotArticle.repository;

import kuke.board.hotarticle.repository.HotArticleListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HotArticleListRepositoryTest {

    @Autowired
    HotArticleListRepository hotArticleListRepository;

    @Test
    public void addTest() {
        try {
            // given
            LocalDateTime time = LocalDateTime.of(2025, 6, 9, 0, 0);
            long limit = 3L;

            // when
            hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(3));
            hotArticleListRepository.add(2L, time, 3L, limit, Duration.ofSeconds(3));
            hotArticleListRepository.add(3L, time, 1L, limit, Duration.ofSeconds(3));
            hotArticleListRepository.add(4L, time, 5L, limit, Duration.ofSeconds(3));
            hotArticleListRepository.add(5L, time, 4L, limit, Duration.ofSeconds(3));

            // then
            List<Long> articleIds = hotArticleListRepository.readAll("20250609");

            assertThat(articleIds).hasSize(Long.valueOf(limit).intValue());
            assertThat(articleIds.get(0)).isEqualTo(4);
            assertThat(articleIds.get(1)).isEqualTo(5);
            assertThat(articleIds.get(2)).isEqualTo(2);

            TimeUnit.SECONDS.sleep(5);

            assertThat(hotArticleListRepository.readAll("20250609")).isEmpty();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}