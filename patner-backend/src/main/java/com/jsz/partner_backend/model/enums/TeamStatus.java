package com.jsz.partner_backend.model.enums;

import lombok.Getter;

@Getter
public enum TeamStatus {


    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");

    private final int value;
    private final String description;

    public static TeamStatus getTeamStatus(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatus[] values = TeamStatus.values();
        for (TeamStatus teamStatus : values) {
            if (teamStatus.value == value) {
                return teamStatus;
            }
        }
        return null;
    }




    TeamStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
