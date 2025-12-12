import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app';
import { appConfig } from './app/app.config';
import { provideHttpClient } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    ...appConfig.providers  // APP.CONFIG ROUTER'I KULLAN!
  ]
}).catch(err => console.error(err));
