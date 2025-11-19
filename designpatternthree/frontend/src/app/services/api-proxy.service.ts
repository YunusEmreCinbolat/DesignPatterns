import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ApiProxyService {
  private cache = new Map<string, Observable<any>>();
  private baseUrl = environment.apiUrl;  // ör: http://localhost:8080

  constructor(private http: HttpClient) {}

  request<T>(
    method: string,
    url: string,
    body?: any,
    useCache: boolean = false
  ): Observable<T> {
    const fullUrl = `${this.baseUrl}${url}`;

    // GET + cache aktifse
    if (method === 'GET' && useCache && this.cache.has(fullUrl)) {
      console.log('[ApiProxyService] Cache hit →', fullUrl);
      return this.cache.get(fullUrl)!;
    }

    console.log('[ApiProxyService] Request →', method, fullUrl);

    const request$ = this.http
      .request<T>(method, fullUrl, {
        body,
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .pipe(shareReplay(1));

    if (useCache) {
      this.cache.set(fullUrl, request$);
    }

    return request$;
  }
}
