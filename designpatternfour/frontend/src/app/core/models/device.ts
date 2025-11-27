import { DeviceType } from './device-type.enum';

export interface Device {
  name: string;
  type: DeviceType;
  isOn: boolean;
}
