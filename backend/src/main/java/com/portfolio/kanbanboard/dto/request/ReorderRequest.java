package com.portfolio.kanbanboard.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReorderRequest(
    @NotNull Integer position
) {}
