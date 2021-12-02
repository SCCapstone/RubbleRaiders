package com.badlogic.game.screens;

import java.util.ArrayList;

public class EventGrabber {
    public enum eventCategory {Peaceful, Combat, SkillChallenge};
    public String[] options;

    public EventGrabber() {
        options = new String[4];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Option " + (i + 1) + ":";
        }

    }
}
