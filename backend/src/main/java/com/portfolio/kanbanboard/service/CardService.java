package com.portfolio.kanbanboard.service;

import com.portfolio.kanbanboard.dto.request.CardRequest;
import com.portfolio.kanbanboard.dto.request.MoveCardRequest;
import com.portfolio.kanbanboard.dto.response.CardResponse;
import com.portfolio.kanbanboard.entity.BoardColumn;
import com.portfolio.kanbanboard.entity.Card;
import com.portfolio.kanbanboard.entity.User;
import com.portfolio.kanbanboard.enums.Priority;
import com.portfolio.kanbanboard.exception.ResourceNotFoundException;
import com.portfolio.kanbanboard.repository.BoardColumnRepository;
import com.portfolio.kanbanboard.repository.CardRepository;
import com.portfolio.kanbanboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final BoardColumnRepository columnRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, BoardColumnRepository columnRepository,
                       UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.columnRepository = columnRepository;
        this.userRepository = userRepository;
    }

    public List<CardResponse> getCards(Long columnId) {
        return cardRepository.findByColumnIdOrderByPositionAsc(columnId)
                .stream().map(CardResponse::from).toList();
    }

    @Transactional
    public CardResponse create(Long columnId, CardRequest request) {
        BoardColumn col = columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found: " + columnId));
        int nextPosition = cardRepository.countByColumnId(columnId);

        Card card = new Card();
        card.setTitle(request.title());
        card.setDescription(request.description());
        card.setPriority(request.priority() != null ? request.priority() : Priority.MEDIUM);
        card.setDueDate(request.dueDate());
        card.setLabels(request.labels());
        card.setPosition(nextPosition);
        card.setColumn(col);

        if (request.assigneeId() != null) {
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.assigneeId()));
            card.setAssignee(assignee);
        }

        cardRepository.save(card);
        return CardResponse.from(card);
    }

    @Transactional
    public CardResponse update(Long cardId, CardRequest request) {
        Card card = findCard(cardId);
        card.setTitle(request.title());
        card.setDescription(request.description());
        card.setPriority(request.priority() != null ? request.priority() : card.getPriority());
        card.setDueDate(request.dueDate());
        card.setLabels(request.labels());

        if (request.assigneeId() != null) {
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.assigneeId()));
            card.setAssignee(assignee);
        } else {
            card.setAssignee(null);
        }

        cardRepository.save(card);
        return CardResponse.from(card);
    }

    @Transactional
    public void delete(Long cardId) {
        Card card = findCard(cardId);
        Long columnId = card.getColumn().getId();
        int deletedPos = card.getPosition();
        cardRepository.delete(card);

        List<Card> remaining = cardRepository.findByColumnIdOrderByPositionAsc(columnId);
        for (Card c : remaining) {
            if (c.getPosition() > deletedPos) {
                c.setPosition(c.getPosition() - 1);
                cardRepository.save(c);
            }
        }
    }

    @Transactional
    public CardResponse move(Long cardId, MoveCardRequest request) {
        Card card = findCard(cardId);
        Long oldColumnId = card.getColumn().getId();
        int oldPos = card.getPosition();

        BoardColumn targetCol = columnRepository.findById(request.targetColumnId())
                .orElseThrow(() -> new ResourceNotFoundException("Column not found: " + request.targetColumnId()));

        // Remove from old column
        List<Card> oldCards = cardRepository.findByColumnIdOrderByPositionAsc(oldColumnId);
        for (Card c : oldCards) {
            if (c.getPosition() > oldPos && !c.getId().equals(cardId)) {
                c.setPosition(c.getPosition() - 1);
            }
        }
        cardRepository.saveAll(oldCards);

        // Insert into new column
        List<Card> newCards = cardRepository.findByColumnIdOrderByPositionAsc(request.targetColumnId());
        int insertPos = request.position();
        for (Card c : newCards) {
            if (c.getPosition() >= insertPos && !c.getId().equals(cardId)) {
                c.setPosition(c.getPosition() + 1);
            }
        }
        cardRepository.saveAll(newCards);

        card.setColumn(targetCol);
        card.setPosition(insertPos);
        cardRepository.save(card);
        return CardResponse.from(card);
    }

    private Card findCard(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found: " + id));
    }
}
