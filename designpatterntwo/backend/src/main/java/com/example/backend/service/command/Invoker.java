package com.example.backend.service.command;

import java.util.ArrayList;
import java.util.List;

public class Invoker {
    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }

    public List<Command> getHistory() {
        return history;
    }
}

