package kuke.board.article.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class PageLimitCalculatorTest {

    @Test
    void calculatePgaeLimitTest(){
        calculatePgaeLimitTest(1L, 30L, 10L, 301L);
        calculatePgaeLimitTest(7L, 30L, 10L, 301L);
        calculatePgaeLimitTest(10L, 30L, 10L, 301L);
        calculatePgaeLimitTest(11L, 30L, 10L, 601L);
        calculatePgaeLimitTest(12L, 30L, 10L, 601L);
    }


    void calculatePgaeLimitTest(Long page, Long pageSize, Long movablePageCount, Long expected){
        Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount);
        assertThat(result).isEqualTo(expected);
    }

}