package org.games.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    private Player player;

    private BigDecimal amountBet;

    private BigDecimal amountWon;

    private BetType betType;

    private int numberBetOn;

    private boolean even;

    private boolean result;

    public void process(int number) {
        if (betType == BetType.ODD_EVEN) {
            result = number % 2 == 0;
        } else {
            result = numberBetOn == number;
        }

        if (result) {
            amountWon = amountBet.multiply(
                    BigDecimal.valueOf(betType.getWinFactor()));
        } else {
            amountWon = BigDecimal.ZERO;
        }

        player.processBet(amountBet, amountWon);
    }

    public void info() {
        String beton = (betType == BetType.NUMBER) ? "" + numberBetOn : even ? "EVEN" : "ODD";
        System.out.format("%-20s %7s %10s%n"
                , player.getName()
                , beton
                , amountBet);
    }

    public void receipt() {
        String beton = (betType == BetType.NUMBER) ? "" + numberBetOn : even ? "EVEN" : "ODD";
        String resultDisplay = (result)?"WON":"LOSE";
        System.out.format("%-20s %7s %10s %10s%n"
                , player.getName()
                , beton
                , resultDisplay
                , amountWon);
    }

}
