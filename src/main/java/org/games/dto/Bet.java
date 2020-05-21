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


    private String baseInfo() {
        StringBuilder info = new StringBuilder(" - ");
        info.append(player.getName())
                .append(", amount bet:")
                .append(amountBet)
                .append(", bet on: ");

        if (betType == BetType.NUMBER) {
            info.append(numberBetOn);
        } else {
            info.append(even ? "even" : "odd");
        }
        return info.toString();
    }

    public void info() {
        System.out.println(baseInfo());
    }

    public void receipt() {
        StringBuilder info = new StringBuilder(baseInfo());
        info.append(", result ")
                .append(result ? " Won" : "Lost");
        if (result) {
            info.append(", amount won: ")
                    .append(amountWon);
        }
        System.out.println(info.toString());
    }

}
