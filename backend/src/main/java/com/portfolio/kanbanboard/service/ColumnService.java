package com.portfolio.kanbanboard.service;

import com.portfolio.kanbanboard.dto.request.ColumnRequest;
import com.portfolio.kanbanboard.dto.request.ReorderRequest;
import com.portfolio.kanbanboard.dto.response.ColumnResponse;
import com.portfolio.kanbanboard.entity.Board;
import com.portfolio.kanbanboard.entity.BoardColumn;
import com.portfolio.kanbanboard.exception.ResourceNotFoundException;
import com.portfolio.kanbanboard.repository.BoardColumnRepository;
import com.portfolio.kanbanboard.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ColumnService {

    private final BoardColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    public ColumnService(BoardColumnRepository columnRepository, BoardRepository boardRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
    }

    public List<ColumnResponse> getColumns(Long boardId) {
        return columnRepository.findByBoardIdOrderByPositionAsc(boardId)
                .stream().map(ColumnResponse::from).toList();
    }

    @Transactional
    public ColumnResponse create(Long boardId, ColumnRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        int nextPosition = columnRepository.countByBoardId(boardId);

        BoardColumn col = new BoardColumn();
        col.setName(request.name());
        col.setColor(request.color());
        col.setPosition(nextPosition);
        col.setBoard(board);
        columnRepository.save(col);
        return ColumnResponse.from(col);
    }

    @Transactional
    public ColumnResponse update(Long columnId, ColumnRequest request) {
        BoardColumn col = findColumn(columnId);
        col.setName(request.name());
        col.setColor(request.color());
        columnRepository.save(col);
        return ColumnResponse.from(col);
    }

    @Transactional
    public void delete(Long columnId) {
        BoardColumn col = findColumn(columnId);
        Long boardId = col.getBoard().getId();
        int deletedPosition = col.getPosition();
        columnRepository.delete(col);

        List<BoardColumn> remaining = columnRepository.findByBoardIdOrderByPositionAsc(boardId);
        for (BoardColumn c : remaining) {
            if (c.getPosition() > deletedPosition) {
                c.setPosition(c.getPosition() - 1);
                columnRepository.save(c);
            }
        }
    }

    @Transactional
    public ColumnResponse reorder(Long columnId, ReorderRequest request) {
        BoardColumn col = findColumn(columnId);
        int oldPos = col.getPosition();
        int newPos = request.position();
        Long boardId = col.getBoard().getId();

        List<BoardColumn> columns = columnRepository.findByBoardIdOrderByPositionAsc(boardId);
        if (oldPos < newPos) {
            for (BoardColumn c : columns) {
                if (c.getPosition() > oldPos && c.getPosition() <= newPos) {
                    c.setPosition(c.getPosition() - 1);
                }
            }
        } else if (oldPos > newPos) {
            for (BoardColumn c : columns) {
                if (c.getPosition() >= newPos && c.getPosition() < oldPos) {
                    c.setPosition(c.getPosition() + 1);
                }
            }
        }
        col.setPosition(newPos);
        columnRepository.saveAll(columns);
        return ColumnResponse.from(col);
    }

    private BoardColumn findColumn(Long id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found: " + id));
    }
}
