package com.portfolio.kanbanboard.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoveCardRequest(
    @NotNull Long targetColumnId,
    @NotNull Integer position
) {}
