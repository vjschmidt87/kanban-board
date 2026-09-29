package com.portfolio.kanbanboard.controller;

import com.portfolio.kanbanboard.dto.request.CardRequest;
import com.portfolio.kanbanboard.dto.request.MoveCardRequest;
import com.portfolio.kanbanboard.dto.response.CardResponse;
import com.portfolio.kanbanboard.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/columns/{columnId}/cards")
    public ResponseEntity<List<CardResponse>> getCards(@PathVariable Long columnId) {
        return ResponseEntity.ok(cardService.getCards(columnId));
    }

    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<CardResponse> create(@PathVariable Long columnId,
                                                @Valid @RequestBody CardRequest request) {
        return ResponseEntity.ok(cardService.create(columnId, request));
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<CardResponse> update(@PathVariable Long cardId,
                                                @Valid @RequestBody CardRequest request) {
        return ResponseEntity.ok(cardService.update(cardId, request));
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> delete(@PathVariable Long cardId) {
        cardService.delete(cardId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<CardResponse> move(@PathVariable Long cardId,
                                              @Valid @RequestBody MoveCardRequest request) {
        return ResponseEntity.ok(cardService.move(cardId, request));
    }
}
