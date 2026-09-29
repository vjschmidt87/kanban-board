import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslationService } from '../../../../core/services/translation.service';
import { BoardService, CardResponse } from '../../../../core/services/board.service';

@Component({
  selector: 'app-card-dialog',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './card-dialog.component.html',
  styleUrl: './card-dialog.component.scss',
})
export class CardDialogComponent implements OnInit {
  @Input() card: CardResponse | null = null;
  @Input() columnId = 0;
  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  title = '';
  description = '';
  priority = 'MEDIUM';
  dueDate = '';
  labels = '';
  loading = signal(false);

  priorities = ['LOW', 'MEDIUM', 'HIGH', 'URGENT'];

  constructor(
    public i18n: TranslationService,
    private boardService: BoardService,
  ) {}

  ngOnInit(): void {
    if (this.card) {
      this.title = this.card.title;
      this.description = this.card.description || '';
      this.priority = this.card.priority;
      this.dueDate = this.card.dueDate || '';
      this.labels = this.card.labels || '';
    }
  }

  get isEdit(): boolean {
    return !!this.card;
  }

  onSubmit(): void {
    if (!this.title.trim()) return;
    this.loading.set(true);

    const request = {
      title: this.title,
      description: this.description,
      priority: this.priority,
      dueDate: this.dueDate || null,
      labels: this.labels,
      assigneeId: null,
    };

    const obs = this.isEdit
      ? this.boardService.updateCard(this.card!.id, request)
      : this.boardService.createCard(this.columnId, request);

    obs.subscribe({
      next: () => {
        this.loading.set(false);
        this.saved.emit();
      },
      error: () => this.loading.set(false),
    });
  }

  onDelete(): void {
    if (!this.card) return;
    if (confirm(this.i18n.t()('card.delete.confirm'))) {
      this.boardService.deleteCard(this.card.id).subscribe(() => this.saved.emit());
    }
  }
}
