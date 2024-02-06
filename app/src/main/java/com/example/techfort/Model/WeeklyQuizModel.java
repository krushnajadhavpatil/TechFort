package com.example.techfort.Model;

public class WeeklyQuizModel {

    String weeklyQuizCategoryId, weeklyQuizCategoryName;

    public WeeklyQuizModel() {
    }

    public WeeklyQuizModel(String weeklyQuizCategoryId, String weeklyQuizCategoryName) {
        this.weeklyQuizCategoryId = weeklyQuizCategoryId;
        this.weeklyQuizCategoryName = weeklyQuizCategoryName;
    }

    public String getWeeklyQuizCategoryId() {
        return weeklyQuizCategoryId;
    }

    public void setWeeklyQuizCategoryId(String weeklyQuizCategoryId) {
        this.weeklyQuizCategoryId = weeklyQuizCategoryId;
    }

    public String getWeeklyQuizCategoryName() {
        return weeklyQuizCategoryName;
    }

    public void setWeeklyQuizCategoryName(String weeklyQuizCategoryName) {
        this.weeklyQuizCategoryName = weeklyQuizCategoryName;
    }

}
