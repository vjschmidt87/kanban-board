import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';
import { TranslationService } from '../../../../core/services/translation.service';
import { CardResponse, ColumnResponse } from '../../../../core/services/board.service';

@Component({
  selector: 'app-column',
  standalone: true,
  imports: [DragDropModule],
  templateUrl: './column.component.html',
  styleUrl: './column.component.scss',
})
export class ColumnComponent {
  @Input({ required: true }) column!: ColumnResponse;
  @Input() allColumnIds: string[] = [];
  @Output() addCard = new EventEmitter<void>();
  @Output() editCard = new EventEmitter<CardResponse>();
  @Output() removeCard = new EventEmitter<number>();
  @Output() removeColumn = new EventEmitter<void>();
  @Output() cardDropped = new EventEmitter<CdkDragDrop<CardResponse[]>>();

  constructor(public i18n: TranslationService) {}

  get dropListId(): string {
    return `column-${this.column.id}`;
  }

  getPriorityClass(priority: string): string {
    return `priority-${priority.toLowerCase()}`;
  }

  isOverdue(dueDate: string | null): boolean {
    if (!dueDate) return false;
    return new Date(dueDate) < new Date(new Date().toDateString());
  }

  isDueSoon(dueDate: string | null): boolean {
    if (!dueDate) return false;
    const due = new Date(dueDate);
    const today = new Date(new Date().toDateString());
    const diff = (due.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
    return diff >= 0 && diff <= 2;
  }

  formatLabels(labels: string | null): string[] {
    if (!labels) return [];
    return labels.split(',').map(l => l.trim()).filter(l => l.length > 0);
  }

  onDrop(event: CdkDragDrop<CardResponse[]>): void {
    this.cardDropped.emit(event);
  }
}
