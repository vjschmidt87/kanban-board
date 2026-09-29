package com.portfolio.kanbanboard.repository;

import com.portfolio.kanbanboard.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    Optional<BoardMember> findByBoardIdAndUserId(Long boardId, Long userId);
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
