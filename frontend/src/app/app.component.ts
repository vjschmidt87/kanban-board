import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/components/header/header.component';
import { ThemeService } from './core/services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  template: `
    <app-header />
    <main>
      <router-outlet />
    </main>
  `,
  styles: [`
    main {
      min-height: calc(100vh - 60px);
    }
  `],
})
export class AppComponent implements OnInit {
  constructor(private theme: ThemeService) {}

  ngOnInit(): void {
    this.theme.init();
  }
}
