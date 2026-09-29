import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';
import { TranslationService } from '../../../../core/services/translation.service';
import {
  BoardService,
  BoardResponse,
  ColumnResponse,
  CardResponse,
} from '../../../../core/services/board.service';
import { ColumnComponent } from '../column/column.component';
import { CardDialogComponent } from '../card-dialog/card-dialog.component';

@Component({
  selector: 'app-board-view',
  standalone: true,
  imports: [FormsModule, DragDropModule, ColumnComponent, CardDialogComponent],
  templateUrl: './board-view.component.html',
  styleUrl: './board-view.component.scss',
})
export class BoardViewComponent implements OnInit {
  board = signal<BoardResponse | null>(null);
  showAddColumn = signal(false);
  newColumnName = '';
  editingCard = signal<{ card: CardResponse | null; columnId: number } | null>(null);
  boardId = 0;

  constructor(
    public i18n: TranslationService,
    private boardService: BoardService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.boardId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadBoard();
  }

  loadBoard(): void {
    this.boardService.getBoard(this.boardId).subscribe({
      next: board => this.board.set(board),
      error: () => this.router.navigate(['/boards']),
    });
  }

  addColumn(): void {
    if (!this.newColumnName.trim()) return;
    this.boardService.createColumn(this.boardId, { name: this.newColumnName }).subscribe(() => {
      this.newColumnName = '';
      this.showAddColumn.set(false);
      this.loadBoard();
    });
  }

  deleteColumn(columnId: number): void {
    if (confirm(this.i18n.t()('column.delete.confirm'))) {
      this.boardService.deleteColumn(columnId).subscribe(() => this.loadBoard());
    }
  }

  openCardDialog(card: CardResponse | null, columnId: number): void {
    this.editingCard.set({ card, columnId });
  }

  closeCardDialog(): void {
    this.editingCard.set(null);
    this.loadBoard();
  }

  deleteCard(cardId: number): void {
    if (confirm(this.i18n.t()('card.delete.confirm'))) {
      this.boardService.deleteCard(cardId).subscribe(() => this.loadBoard());
    }
  }

  getColumnIds(): string[] {
    return this.board()?.columns.map(c => `column-${c.id}`) || [];
  }

  onCardDrop(event: CdkDragDrop<CardResponse[]>, targetColumn: ColumnResponse): void {
    const card: CardResponse = event.item.data;
    if (event.previousContainer === event.container && event.previousIndex === event.currentIndex) {
      return;
    }

    this.boardService.moveCard(card.id, {
      targetColumnId: targetColumn.id,
      position: event.currentIndex,
    }).subscribe(() => this.loadBoard());
  }
}
