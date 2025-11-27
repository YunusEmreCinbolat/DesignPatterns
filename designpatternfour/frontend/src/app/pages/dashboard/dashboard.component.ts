import { Component } from '@angular/core';
import { NgFor } from '@angular/common';

import { Device } from '../../core/models/device';
import { DeviceType } from '../../core/models/device-type.enum';
import { DeviceCardComponent } from '../../components/device-card/device-card.component';
import { RuleBuilderComponent } from '../../components/rule-builder/rule-builder.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgFor, DeviceCardComponent, RuleBuilderComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  devices: Device[] = [
    { name: 'Living Room Light', type: DeviceType.LIGHT, isOn: false },
    { name: 'Main AC', type: DeviceType.AC, isOn: false },
    { name: 'Front Door', type: DeviceType.DOOR, isOn: false },
  ];

  onRuleExecuted(message: string) {
    console.log('Rule Result:', message);

    this.updateDeviceState(message);
  }

  updateDeviceState(msg: string) {
    if (msg.includes('AC') && msg.includes('ON'))
      this.devices.find(d => d.type === DeviceType.AC)!.isOn = true;

    if (msg.includes('AC') && msg.includes('OFF'))
      this.devices.find(d => d.type === DeviceType.AC)!.isOn = false;

    if (msg.includes('Light') && msg.includes('ON'))
      this.devices.find(d => d.type === DeviceType.LIGHT)!.isOn = true;

    if (msg.includes('Light') && msg.includes('OFF'))
      this.devices.find(d => d.type === DeviceType.LIGHT)!.isOn = false;

    if (msg.includes('FrontDoor') && msg.includes('opened'))
      this.devices.find(d => d.type === DeviceType.DOOR)!.isOn = true;

    if (msg.includes('FrontDoor') && msg.includes('closed'))
      this.devices.find(d => d.type === DeviceType.DOOR)!.isOn = false;
  }
}
