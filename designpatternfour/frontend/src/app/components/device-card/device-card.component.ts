import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Device } from '../../core/models/device';
import { DeviceType } from '../../core/models/device-type.enum';

@Component({
  selector: 'app-device-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.css'],
})
export class DeviceCardComponent {
  @Input({ required: true }) device!: Device;
  DeviceType = DeviceType;

  get statusText(): string {
    return this.device.isOn ? 'ON' : 'OFF';
  }
}
