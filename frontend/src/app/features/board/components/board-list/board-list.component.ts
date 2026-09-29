import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslationService } from '../../../../core/services/translation.service';
import { BoardService, BoardSummary } from '../../../../core/services/board.service';

@Component({
  selector: 'app-board-list',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './board-list.component.html',
  styleUrl: './board-list.component.scss',
})
export class BoardListComponent implements OnInit {
  boards = signal<BoardSummary[]>([]);
  showCreate = signal(false);
  newName = '';
  newDesc = '';

  constructor(
    public i18n: TranslationService,
    private boardService: BoardService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  loadBoards(): void {
    this.boardService.getBoards().subscribe(boards => this.boards.set(boards));
  }

  openBoard(id: number): void {
    this.router.navigate(['/boards', id]);
  }

  createBoard(): void {
    if (!this.newName.trim()) return;
    this.boardService.createBoard({ name: this.newName, description: this.newDesc }).subscribe(board => {
      this.showCreate.set(false);
      this.newName = '';
      this.newDesc = '';
      this.router.navigate(['/boards', board.id]);
    });
  }

  deleteBoard(event: Event, id: number): void {
    event.stopPropagation();
    if (confirm(this.i18n.t()('boards.delete.confirm'))) {
      this.boardService.deleteBoard(id).subscribe(() => this.loadBoards());
    }
  }
}
