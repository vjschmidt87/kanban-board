package com.portfolio.kanbanboard.entity;

import com.portfolio.kanbanboard.enums.BoardRole;
import jakarta.persistence.*;

@Entity
@Table(name = "board_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"board_id", "user_id"})
})
public class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BoardRole role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BoardRole getRole() { return role; }
    public void setRole(BoardRole role) { this.role = role; }
}
