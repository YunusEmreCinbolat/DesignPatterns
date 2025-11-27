import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RuleRequest } from '../models/rule-request';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ApiProxyService {
  // Artık base URL yok, sadece /api... kullanıyoruz → proxy.conf.json backend'e yönlendiriyor.
  private readonly baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  /**
   * RuleController → POST /api/rules/execute
   */
  executeRule(rule: RuleRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/rules/execute`, rule, {
      responseType: 'json',
    });
  }

  /**
   * ReportController → GET /api/reports/energy
   */
  getEnergyReport(): Observable<any> {
    return this.http.get(`${this.baseUrl}/reports/energy`, {
      responseType: 'json',
    });
  }
}
