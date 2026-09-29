package com.portfolio.kanbanboard.service;

import com.portfolio.kanbanboard.dto.request.BoardRequest;
import com.portfolio.kanbanboard.dto.response.BoardResponse;
import com.portfolio.kanbanboard.entity.Board;
import com.portfolio.kanbanboard.entity.BoardMember;
import com.portfolio.kanbanboard.entity.User;
import com.portfolio.kanbanboard.enums.BoardRole;
import com.portfolio.kanbanboard.exception.ResourceNotFoundException;
import com.portfolio.kanbanboard.exception.UnauthorizedException;
import com.portfolio.kanbanboard.repository.BoardRepository;
import com.portfolio.kanbanboard.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public List<BoardResponse> getUserBoards(String username) {
        User user = findUser(username);
        return boardRepository.findBoardsByUserId(user.getId())
                .stream().map(BoardResponse::summary).toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long id, String username) {
        Board board = findBoard(id);
        checkAccess(board, username);
        return BoardResponse.from(board);
    }

    @Transactional
    public BoardResponse create(BoardRequest request, String username) {
        User owner = findUser(username);
        Board board = new Board();
        board.setName(request.name());
        board.setDescription(request.description());
        board.setOwner(owner);
        boardRepository.save(board);

        BoardMember member = new BoardMember();
        member.setBoard(board);
        member.setUser(owner);
        member.setRole(BoardRole.OWNER);
        board.getMembers().add(member);
        boardRepository.save(board);

        return BoardResponse.from(board);
    }

    @Transactional
    public BoardResponse update(Long id, BoardRequest request, String username) {
        Board board = findBoard(id);
        checkOwner(board, username);
        board.setName(request.name());
        board.setDescription(request.description());
        boardRepository.save(board);
        return BoardResponse.from(board);
    }

    @Transactional
    public void delete(Long id, String username) {
        Board board = findBoard(id);
        checkOwner(board, username);
        boardRepository.delete(board);
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + id));
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private void checkAccess(Board board, String username) {
        boolean hasAccess = board.getOwner().getUsername().equals(username) ||
                board.getMembers().stream().anyMatch(m -> m.getUser().getUsername().equals(username));
        if (!hasAccess) throw new UnauthorizedException("No access to this board");
    }

    private void checkOwner(Board board, String username) {
        if (!board.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedException("Only the owner can modify this board");
        }
    }
}
