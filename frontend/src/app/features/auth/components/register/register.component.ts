import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslationService } from '../../../../core/services/translation.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  confirmPassword = '';
  error = signal<string | null>(null);
  loading = signal(false);

  constructor(
    public i18n: TranslationService,
    private auth: AuthService,
    private router: Router,
  ) {}

  onSubmit(): void {
    this.error.set(null);

    if (this.password !== this.confirmPassword) {
      this.error.set(this.i18n.t()('auth.error.passwordMismatch'));
      return;
    }

    this.loading.set(true);

    this.auth.register({
      username: this.username,
      email: this.email,
      password: this.password,
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/boards']);
      },
      error: () => {
        this.loading.set(false);
        this.error.set(this.i18n.t()('auth.error.exists'));
      },
    });
  }
}
