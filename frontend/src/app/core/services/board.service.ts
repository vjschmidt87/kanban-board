import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CardResponse {
  id: number;
  title: string;
  description: string;
  position: number;
  columnId: number;
  assigneeUsername: string | null;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  dueDate: string | null;
  labels: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ColumnResponse {
  id: number;
  name: string;
  position: number;
  cards: CardResponse[];
}

export interface BoardSummary {
  id: number;
  name: string;
  description: string;
  ownerUsername: string;
  columnCount: number;
  cardCount: number;
}

export interface BoardResponse {
  id: number;
  name: string;
  description: string;
  ownerUsername: string;
  columns: ColumnResponse[];
  members: string[];
}

export interface BoardRequest {
  name: string;
  description: string;
}

export interface CardRequest {
  title: string;
  description: string;
  priority: string;
  dueDate: string | null;
  labels: string;
  assigneeId: number | null;
}

export interface MoveCardRequest {
  targetColumnId: number;
  position: number;
}

export interface ColumnRequest {
  name: string;
}

@Injectable({ providedIn: 'root' })
export class BoardService {
  private apiUrl = 'http://localhost:8088/api';

  constructor(private http: HttpClient) {}

  getBoards(): Observable<BoardSummary[]> {
    return this.http.get<BoardSummary[]>(`${this.apiUrl}/boards`);
  }

  getBoard(id: number): Observable<BoardResponse> {
    return this.http.get<BoardResponse>(`${this.apiUrl}/boards/${id}`);
  }

  createBoard(request: BoardRequest): Observable<BoardResponse> {
    return this.http.post<BoardResponse>(`${this.apiUrl}/boards`, request);
  }

  updateBoard(id: number, request: BoardRequest): Observable<BoardResponse> {
    return this.http.put<BoardResponse>(`${this.apiUrl}/boards/${id}`, request);
  }

  deleteBoard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/boards/${id}`);
  }

  createColumn(boardId: number, request: ColumnRequest): Observable<ColumnResponse> {
    return this.http.post<ColumnResponse>(`${this.apiUrl}/boards/${boardId}/columns`, request);
  }

  updateColumn(columnId: number, request: ColumnRequest): Observable<ColumnResponse> {
    return this.http.put<ColumnResponse>(`${this.apiUrl}/columns/${columnId}`, request);
  }

  deleteColumn(columnId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/columns/${columnId}`);
  }

  createCard(columnId: number, request: CardRequest): Observable<CardResponse> {
    return this.http.post<CardResponse>(`${this.apiUrl}/columns/${columnId}/cards`, request);
  }

  updateCard(cardId: number, request: CardRequest): Observable<CardResponse> {
    return this.http.put<CardResponse>(`${this.apiUrl}/cards/${cardId}`, request);
  }

  deleteCard(cardId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cards/${cardId}`);
  }

  moveCard(cardId: number, request: MoveCardRequest): Observable<CardResponse> {
    return this.http.put<CardResponse>(`${this.apiUrl}/cards/${cardId}/move`, request);
  }
}
