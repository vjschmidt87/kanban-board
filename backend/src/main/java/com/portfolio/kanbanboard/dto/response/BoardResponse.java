package com.portfolio.kanbanboard.dto.response;

import com.portfolio.kanbanboard.entity.Board;
import java.time.LocalDateTime;
import java.util.List;

public record BoardResponse(
    Long id,
    String name,
    String description,
    String ownerUsername,
    List<ColumnResponse> columns,
    int memberCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
            board.getId(),
            board.getName(),
            board.getDescription(),
            board.getOwner().getUsername(),
            board.getColumns().stream().map(ColumnResponse::from).toList(),
            board.getMembers().size(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }

    public static BoardResponse summary(Board board) {
        return new BoardResponse(
            board.getId(),
            board.getName(),
            board.getDescription(),
            board.getOwner().getUsername(),
            List.of(),
            board.getMembers().size(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }
}
