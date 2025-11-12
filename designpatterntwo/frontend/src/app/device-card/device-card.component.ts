import { Component, Input, OnDestroy } from '@angular/core';
import {
  NgIf,
  NgClass,
  NgSwitch,
  NgSwitchCase,
  NgSwitchDefault,
  DecimalPipe,
} from '@angular/common';
import { DeviceType } from '../models/device-type.enum';
import { DeviceService, DeviceResponse } from '../services/device.service';

@Component({
  selector: 'app-device-card',
  standalone: true,
  imports: [NgIf, NgClass, NgSwitch, NgSwitchCase, NgSwitchDefault, DecimalPipe],
  templateUrl: './device-card.component.html',
  styleUrl: './device-card.component.css',
})
export class DeviceCardComponent implements OnDestroy {
  @Input() device!: { name: string; type: DeviceType; state: string };
  loading = false;
  energyLevel = 0;
  decorators: string[] = [];
  private intervalId: any;
  DeviceType = DeviceType; // ✅ enum template'te kullanılabilir

  constructor(private deviceService: DeviceService) {}

  toggleDevice() {
    this.loading = true;
    const action = this.device.state === 'ON' ? 'off' : 'on';
    const apiCall =
      action === 'on'
        ? this.deviceService.turnOn(this.device.type)
        : this.deviceService.turnOff(this.device.type);

    apiCall.subscribe({
      next: (res: DeviceResponse) => {
        this.device.state = action.toUpperCase();
        this.loading = false;
        this.decorators = res.decorators
          ? (res.decorators as string)
              .split('+')
              .map((d: string) => d.trim())
              .filter((d: string) => d)
          : [];
      },
      error: (err) => {
        console.error('Hata:', err);
        this.loading = false;
      },
    });
  }

  startEnergyIncrease() {
    clearInterval(this.intervalId);
    this.intervalId = setInterval(() => {
      if (this.energyLevel < 100) this.energyLevel += 1;
    }, 200);
  }

  resetEnergy() {
    clearInterval(this.intervalId);
    this.energyLevel = 0;
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }
}
