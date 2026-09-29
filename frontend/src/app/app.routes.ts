import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/boards', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/components/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/components/register/register.component').then(m => m.RegisterComponent),
  },
  {
    path: 'boards',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/board/components/board-list/board-list.component').then(m => m.BoardListComponent),
  },
  {
    path: 'boards/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/board/components/board-view/board-view.component').then(m => m.BoardViewComponent),
  },
  { path: '**', redirectTo: '/boards' },
];
