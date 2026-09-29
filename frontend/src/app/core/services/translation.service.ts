import { Injectable, signal, computed } from '@angular/core';
import { en } from '../i18n/en';
import { ptBr } from '../i18n/pt-br';

export type Language = 'en' | 'pt-br';

@Injectable({ providedIn: 'root' })
export class TranslationService {
  private translations: Record<Language, Record<string, string>> = {
    'en': en,
    'pt-br': ptBr,
  };

  currentLang = signal<Language>(this.loadLang());

  private loadLang(): Language {
    const saved = localStorage.getItem('kanban-lang');
    return (saved === 'pt-br' ? 'pt-br' : 'en');
  }

  setLanguage(lang: Language): void {
    this.currentLang.set(lang);
    localStorage.setItem('kanban-lang', lang);
  }

  toggleLanguage(): void {
    this.setLanguage(this.currentLang() === 'en' ? 'pt-br' : 'en');
  }

  t = computed(() => {
    const lang = this.currentLang();
    const dict = this.translations[lang];
    return (key: string, params?: Record<string, string | number>): string => {
      let value = dict[key] || key;
      if (params) {
        Object.entries(params).forEach(([k, v]) => {
          value = value.replace(`{${k}}`, String(v));
        });
      }
      return value;
    };
  });
}
