import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DeviceType } from '../models/device-type.enum';
import { environment } from '../environment/environment';

export interface DeviceResponse {
  message: string;
  status: string;
  decorators?: string;
}

@Injectable({ providedIn: 'root' })
export class DeviceService {
private apiUrl = `${environment.apiUrl}/devices`;
  
  constructor(private http: HttpClient) {}

  turnOn(type: DeviceType) {
    return this.http.post<DeviceResponse>(`${this.apiUrl}/${type}/on`, {});
  }

  turnOff(type: DeviceType) {
    return this.http.post<DeviceResponse>(`${this.apiUrl}/${type}/off`, {});
  }
}


