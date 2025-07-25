package kuke.board.view.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kuke.board.view.entity.ArticleViewCount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {
    
    @Autowired
    ArticleViewCountBackUpRepository articleViewCountBackUpRepository;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Test
    @Transactional
    void updateViewCountTest(){
        // given
        articleViewCountBackUpRepository.save(
                ArticleViewCount.init(1L, 0L)
        );
        entityManager.flush();
        entityManager.clear();

        // 100, 300, 200 등 순차적으로 들어오지 않을 때를 대비
        int result1 = articleViewCountBackUpRepository.updateViewCount(1L, 100L);
        int result2 = articleViewCountBackUpRepository.updateViewCount(1L, 300L);
        int result3 = articleViewCountBackUpRepository.updateViewCount(1L, 200L);

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);
        System.out.println("result3 = " + result3);
        
        ArticleViewCount articleViewCount = articleViewCountBackUpRepository.findById(1L).get();
        Assertions.assertThat(articleViewCount.getViewCount()).isEqualTo(300L);
    }

}