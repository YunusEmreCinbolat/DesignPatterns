import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';           // ⬅️ BUNU EKLE
import { provideRouter, Routes } from '@angular/router';
import { App } from './app/app';

const routes: Routes = [
];

bootstrapApplication(App, {
  providers: [
    provideHttpClient(),                                             // ⬅️ BUNU EKLE
    provideRouter(routes)
  ]
}).catch(err => console.error(err));
