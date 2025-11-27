package com.example.backend.config;

import com.example.backend.device.AC;
import com.example.backend.device.Door;
import com.example.backend.device.Light;
import com.example.backend.mediator.DefaultSmartHomeMediator;
import com.example.backend.mediator.SmartHomeMediator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartHomeConfig {

    @Bean
    public SmartHomeMediator smartHomeMediator() {
        DefaultSmartHomeMediator mediator = new DefaultSmartHomeMediator();

        mediator.registerDevice(new Light("LivingRoomLight"));
        mediator.registerDevice(new AC("MainAC"));
        mediator.registerDevice(new Door("FrontDoor"));

        return mediator;
    }
}
