import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

interface AuthResponse {
  token: string;
  username: string;
}

interface LoginRequest {
  username: string;
  password: string;
}

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8088/api/auth';

  token = signal<string | null>(localStorage.getItem('kanban-token'));
  username = signal<string | null>(localStorage.getItem('kanban-username'));
  isAuthenticated = computed(() => !!this.token());

  constructor(private http: HttpClient, private router: Router) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(res => this.storeAuth(res))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap(res => this.storeAuth(res))
    );
  }

  logout(): void {
    this.token.set(null);
    this.username.set(null);
    localStorage.removeItem('kanban-token');
    localStorage.removeItem('kanban-username');
    this.router.navigate(['/login']);
  }

  private storeAuth(res: AuthResponse): void {
    this.token.set(res.token);
    this.username.set(res.username);
    localStorage.setItem('kanban-token', res.token);
    localStorage.setItem('kanban-username', res.username);
  }
}
