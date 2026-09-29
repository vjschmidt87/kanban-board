package com.portfolio.kanbanboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardRequest(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 500) String description
) {}
