package com.portfolio.kanbanboard.repository;

import com.portfolio.kanbanboard.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);
    int countByBoardId(Long boardId);
}
