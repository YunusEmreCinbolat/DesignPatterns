export interface RuleRequest {
  sensor: 'TEMPERATURE' | 'MOTION';   // backend ile uyumlu
  operator: '>' | '<' | '=';
  value: number;
  action: 'TURN_ON' | 'TURN_OFF';
  deviceType: 'LIGHT' | 'AC' | 'DOOR';
}
