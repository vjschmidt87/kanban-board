package com.portfolio.kanbanboard.dto.request;

import com.portfolio.kanbanboard.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CardRequest(
    @NotBlank @Size(max = 200) String title,
    String description,
    Priority priority,
    LocalDate dueDate,
    String labels,
    Long assigneeId
) {}
