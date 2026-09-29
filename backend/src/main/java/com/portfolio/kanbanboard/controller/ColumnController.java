package com.portfolio.kanbanboard.controller;

import com.portfolio.kanbanboard.dto.request.ColumnRequest;
import com.portfolio.kanbanboard.dto.request.ReorderRequest;
import com.portfolio.kanbanboard.dto.response.ColumnResponse;
import com.portfolio.kanbanboard.service.ColumnService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ColumnController {

    private final ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<List<ColumnResponse>> getColumns(@PathVariable Long boardId) {
        return ResponseEntity.ok(columnService.getColumns(boardId));
    }

    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<ColumnResponse> create(@PathVariable Long boardId,
                                                  @Valid @RequestBody ColumnRequest request) {
        return ResponseEntity.ok(columnService.create(boardId, request));
    }

    @PutMapping("/columns/{columnId}")
    public ResponseEntity<ColumnResponse> update(@PathVariable Long columnId,
                                                  @Valid @RequestBody ColumnRequest request) {
        return ResponseEntity.ok(columnService.update(columnId, request));
    }

    @DeleteMapping("/columns/{columnId}")
    public ResponseEntity<Void> delete(@PathVariable Long columnId) {
        columnService.delete(columnId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/columns/{columnId}/reorder")
    public ResponseEntity<ColumnResponse> reorder(@PathVariable Long columnId,
                                                   @Valid @RequestBody ReorderRequest request) {
        return ResponseEntity.ok(columnService.reorder(columnId, request));
    }
}
