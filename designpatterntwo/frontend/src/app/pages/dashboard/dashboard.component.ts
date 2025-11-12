import { Component } from '@angular/core';
import { NgFor } from '@angular/common';
import { DeviceType } from '../../models/device-type.enum';
import { DeviceCardComponent } from '../../device-card/device-card.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgFor, DeviceCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  devices = [
    { name: 'Oturma Odası Işığı', type: DeviceType.LIGHT, state: 'OFF' },
    { name: 'Klima', type: DeviceType.AC, state: 'OFF' },
    { name: 'Televizyon', type: DeviceType.TV, state: 'OFF' },
  ];
}
