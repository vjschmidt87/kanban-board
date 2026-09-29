package com.portfolio.kanbanboard.repository;

import com.portfolio.kanbanboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN b.members m WHERE b.owner.id = :userId OR m.user.id = :userId")
    List<Board> findBoardsByUserId(@Param("userId") Long userId);
}
