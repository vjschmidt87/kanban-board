import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslationService } from '../../../../core/services/translation.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  username = '';
  password = '';
  error = signal<string | null>(null);
  loading = signal(false);

  constructor(
    public i18n: TranslationService,
    private auth: AuthService,
    private router: Router,
  ) {}

  onSubmit(): void {
    this.error.set(null);
    this.loading.set(true);

    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/boards']);
      },
      error: () => {
        this.loading.set(false);
        this.error.set(this.i18n.t()('auth.error.invalid'));
      },
    });
  }
}
