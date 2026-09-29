package com.portfolio.kanbanboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ColumnRequest(
    @NotBlank @Size(max = 50) String name,
    String color
) {}
