package com.portfolio.kanbanboard.dto.response;

import com.portfolio.kanbanboard.entity.Card;
import com.portfolio.kanbanboard.enums.Priority;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CardResponse(
    Long id,
    String title,
    String description,
    Integer position,
    Long columnId,
    String assigneeUsername,
    Priority priority,
    LocalDate dueDate,
    String labels,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CardResponse from(Card card) {
        return new CardResponse(
            card.getId(),
            card.getTitle(),
            card.getDescription(),
            card.getPosition(),
            card.getColumn().getId(),
            card.getAssignee() != null ? card.getAssignee().getUsername() : null,
            card.getPriority(),
            card.getDueDate(),
            card.getLabels(),
            card.getCreatedAt(),
            card.getUpdatedAt()
        );
    }
}
