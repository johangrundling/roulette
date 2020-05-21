package org.games.dto;

public class PlayerQuickSearchCode {
    private static int quickCode;

    public static int generateQuickCode() {
        return ++quickCode;
    }
}
