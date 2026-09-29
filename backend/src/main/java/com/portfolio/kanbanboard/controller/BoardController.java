package com.portfolio.kanbanboard.controller;

import com.portfolio.kanbanboard.dto.request.BoardRequest;
import com.portfolio.kanbanboard.dto.response.BoardResponse;
import com.portfolio.kanbanboard.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getUserBoards(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(boardService.getUserBoards(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(boardService.getBoardById(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<BoardResponse> create(@Valid @RequestBody BoardRequest request,
                                                 @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(boardService.create(request, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody BoardRequest request,
                                                 @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(boardService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        boardService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
