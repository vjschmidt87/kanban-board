package com.portfolio.kanbanboard.config;

import com.portfolio.kanbanboard.entity.*;
import com.portfolio.kanbanboard.enums.BoardRole;
import com.portfolio.kanbanboard.enums.Priority;
import com.portfolio.kanbanboard.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository columnRepository;
    private final CardRepository cardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.demo.username}")
    private String demoUsername;

    @Value("${app.seed.demo.password}")
    private String demoPassword;

    @Value("${app.seed.demo.email}")
    private String demoEmail;

    @Value("${app.seed.alice.username}")
    private String aliceUsername;

    @Value("${app.seed.alice.password}")
    private String alicePassword;

    @Value("${app.seed.alice.email}")
    private String aliceEmail;

    public DataSeeder(UserRepository userRepository, BoardRepository boardRepository,
                      BoardColumnRepository columnRepository, CardRepository cardRepository,
                      BoardMemberRepository boardMemberRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
        this.cardRepository = cardRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        User demo = new User();
        demo.setUsername(demoUsername);
        demo.setEmail(demoEmail);
        demo.setPassword(passwordEncoder.encode(demoPassword));
        demo = userRepository.save(demo);

        User alice = new User();
        alice.setUsername(aliceUsername);
        alice.setEmail(aliceEmail);
        alice.setPassword(passwordEncoder.encode(alicePassword));
        userRepository.save(alice);

        Board board = new Board();
        board.setName("My Project");
        board.setDescription("Portfolio kanban board demo");
        board.setOwner(demo);
        boardRepository.save(board);

        BoardMember ownerMember = new BoardMember();
        ownerMember.setBoard(board);
        ownerMember.setUser(demo);
        ownerMember.setRole(BoardRole.OWNER);
        boardMemberRepository.save(ownerMember);

        BoardMember aliceMember = new BoardMember();
        aliceMember.setBoard(board);
        aliceMember.setUser(alice);
        aliceMember.setRole(BoardRole.MEMBER);
        boardMemberRepository.save(aliceMember);

        BoardColumn backlog = createColumn(board, "Backlog", 0);
        BoardColumn todo = createColumn(board, "To Do", 1);
        BoardColumn inProgress = createColumn(board, "In Progress", 2);
        BoardColumn review = createColumn(board, "Review", 3);
        BoardColumn done = createColumn(board, "Done", 4);

        createCard(backlog, "Research CI/CD options", "Compare GitHub Actions, GitLab CI, and Jenkins",
                Priority.LOW, null, "research,devops", null, 0);
        createCard(backlog, "Write API documentation", "Document all REST endpoints with examples",
                Priority.MEDIUM, LocalDate.now().plusDays(14), "docs", demo, 1);

        createCard(todo, "Set up database migrations", "Configure Flyway for PostgreSQL schema management",
                Priority.HIGH, LocalDate.now().plusDays(5), "backend,database", demo, 0);
        createCard(todo, "Design landing page", "Create wireframes and mockups for the landing page",
                Priority.MEDIUM, LocalDate.now().plusDays(7), "frontend,design", alice, 1);

        createCard(inProgress, "Implement authentication", "JWT-based auth with login and registration",
                Priority.URGENT, LocalDate.now().plusDays(2), "backend,security", demo, 0);
        createCard(inProgress, "Build kanban board UI", "Drag and drop interface with Angular CDK",
                Priority.HIGH, LocalDate.now().plusDays(3), "frontend,angular", alice, 1);

        createCard(review, "Add unit tests for services", "Cover BoardService and CardService with JUnit tests",
                Priority.MEDIUM, LocalDate.now().plusDays(1), "testing,backend", demo, 0);

        createCard(done, "Project setup", "Initialize Spring Boot and Angular projects",
                Priority.HIGH, LocalDate.now().minusDays(3), "setup", demo, 0);
        createCard(done, "Configure Docker Compose", "Multi-service setup with PostgreSQL, backend, and frontend",
                Priority.MEDIUM, LocalDate.now().minusDays(1), "devops,docker", demo, 1);
    }

    private BoardColumn createColumn(Board board, String name, int position) {
        BoardColumn column = new BoardColumn();
        column.setName(name);
        column.setPosition(position);
        column.setBoard(board);
        return columnRepository.save(column);
    }

    private void createCard(BoardColumn column, String title, String description,
                            Priority priority, LocalDate dueDate, String labels,
                            User assignee, int position) {
        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setPriority(priority);
        card.setDueDate(dueDate);
        card.setLabels(labels);
        card.setAssignee(assignee);
        card.setPosition(position);
        card.setColumn(column);
        cardRepository.save(card);
    }
}
