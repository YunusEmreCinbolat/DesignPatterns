import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  styleUrls: ['./app.css'],
  template: `
    <div class="app-shell">
      <header class="header">
        <h1 class="title">🛒 E-Commerce Discount Engine</h1>
        <p class="subtitle">Flyweight · Bridge · Facade Pattern Demo</p>
      </header>

      <nav class="nav">
        <a routerLink="">Cart</a>
      </nav>

      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {}
