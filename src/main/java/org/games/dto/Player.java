package org.games.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Player {

    private String name;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private BigDecimal totalAmountBet = BigDecimal.ZERO;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private BigDecimal totalAmountWon = BigDecimal.ZERO;

    @EqualsAndHashCode.Exclude
    private int quickCode;

    public void processBet(BigDecimal amountBet, BigDecimal amountWon) {
        totalAmountBet = totalAmountBet.add(amountBet);
        totalAmountWon = totalAmountWon.add(amountWon);
    }

    public void info() {
        System.out.format("%-20s %-7d %10s %10s %n"
                , name
                ,quickCode
                ,totalAmountBet
                ,totalAmountWon);
    }

}
