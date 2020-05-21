package org.games.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BetType {

    NUMBER(36),
    ODD_EVEN(2);

    @Getter
    private int winRatio;

}