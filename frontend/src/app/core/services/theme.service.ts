import { Injectable, signal } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  currentTheme = signal<Theme>(this.loadTheme());

  private loadTheme(): Theme {
    const saved = localStorage.getItem('kanban-theme');
    if (saved === 'dark' || saved === 'light') return saved;
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  toggleTheme(): void {
    const next = this.currentTheme() === 'light' ? 'dark' : 'light';
    this.currentTheme.set(next);
    localStorage.setItem('kanban-theme', next);
    this.applyTheme(next);
  }

  applyTheme(theme: Theme): void {
    document.documentElement.setAttribute('data-theme', theme);
  }

  init(): void {
    this.applyTheme(this.currentTheme());
  }
}
