import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';   // ✅ DOĞRU IMPORT
import { NgIf } from '@angular/common';
import { ApiProxyService } from '../../core/services/api-proxy.service';
import { RuleRequest } from '../../core/models/rule-request';

@Component({
  selector: 'app-rule-builder',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './rule-builder.component.html',
  styleUrls: ['./rule-builder.component.css'],
})
export class RuleBuilderComponent {
  @Output() ruleExecuted = new EventEmitter<string>(); // mesaj string

  sensor: 'TEMPERATURE' | 'MOTION' = 'TEMPERATURE';
  operator: '>' | '<' | '=' = '>';
  value = 26;
  action: 'TURN_ON' | 'TURN_OFF' = 'TURN_ON';
  deviceType: 'LIGHT' | 'AC' | 'DOOR' = 'AC';

  loading = false;
  lastResponse = '';

  constructor(private api: ApiProxyService) {}

  buildRule(): RuleRequest {
    return {
      sensor: this.sensor,
      operator: this.operator,
      value: this.value,
      action: this.action,
      deviceType: this.deviceType,
    };
  }

  onSubmit() {
    this.loading = true;

    this.api.executeRule(this.buildRule()).subscribe({
      next: (res: any) => {
        this.lastResponse = res.message;
        this.ruleExecuted.emit(res.message); // 🔥 Dashboard’a gönder
        this.loading = false;
      },
      error: () => {
        this.lastResponse = 'Error executing rule';
        this.loading = false;
      },
    });
  }

  getEnergyReport() {
    this.loading = true;

    this.api.getEnergyReport().subscribe({
      next: (res: any) => {
        this.lastResponse = JSON.stringify(res);
        this.ruleExecuted.emit('ENERGY_REPORT');
        this.loading = false;
      },
      error: () => {
        this.lastResponse = 'Error getting report';
        this.loading = false;
      },
    });
  }
}
