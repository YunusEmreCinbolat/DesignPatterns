import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
    <div class="app-shell">
      <h1>🍕 Pizza Ordering System</h1>

      <nav>
        <a routerLink="">Create Order</a> |
        <a routerLink="/orders">All Orders</a>
      </nav>

      <router-outlet></router-outlet>
    </div>
  `,
})
export class AppComponent {}
