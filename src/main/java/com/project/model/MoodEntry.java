package com.project.model;

public class MoodEntry {
    private String note;
    private int moodLevel;
    private String date;

    public MoodEntry(String note, int moodLevel, String date) {
        this.note = note;
        this.moodLevel = moodLevel;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}