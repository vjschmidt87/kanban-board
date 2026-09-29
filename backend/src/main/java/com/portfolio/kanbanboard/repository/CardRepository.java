package com.portfolio.kanbanboard.repository;

import com.portfolio.kanbanboard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByColumnIdOrderByPositionAsc(Long columnId);
    int countByColumnId(Long columnId);
}
