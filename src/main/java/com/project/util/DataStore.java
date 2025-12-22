package com.project.util;

import java.util.ArrayList;
import java.util.List;

import com.project.model.MoodEntry;
import com.project.model.User;

public class DataStore {
    // These lists are STATIC and FINAL, meaning they are shared across the whole app
    public static final List<User> users = new ArrayList<>();
    public static final List<MoodEntry> moods = new ArrayList<>();

    static {
        // Default Admin
        users.add(new User("100", "Admin", "Admin", "Active"));
        // Default Data so charts aren't empty at start
        moods.add(new MoodEntry("Happy Start", 5, "2025-10-01"));
        moods.add(new MoodEntry("Okay Start", 3, "2025-10-02"));
    }
}