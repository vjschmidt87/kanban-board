package com.portfolio.kanbanboard.dto.response;

import com.portfolio.kanbanboard.entity.BoardColumn;
import java.util.List;

public record ColumnResponse(
    Long id,
    String name,
    Integer position,
    String color,
    List<CardResponse> cards
) {
    public static ColumnResponse from(BoardColumn col) {
        return new ColumnResponse(
            col.getId(),
            col.getName(),
            col.getPosition(),
            col.getColor(),
            col.getCards().stream().map(CardResponse::from).toList()
        );
    }
}
