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

    private int bet;

    private boolean even;

    private boolean result;

    public void process(int number) {
        if (betType == BetType.ODD_EVEN) {
            result = number % 2 == 0;
        } else {
            result = bet == number;
        }

        if (result) {
            amountWon = amountBet.multiply(
                    BigDecimal.valueOf(betType.getWinFactor()));
        } else {
            amountWon = BigDecimal.ZERO;
        }

        player.processBet(amountBet, amountWon);
    }

    public void reciept(){
        System.out.println(" " + player.getName() + " " + amountBet + " " + amountWon);
    }


    public void info(){
        System.out.println(" " + player.getName() + " " + amountBet + " " + amountWon + " " + betType);
    }

}
