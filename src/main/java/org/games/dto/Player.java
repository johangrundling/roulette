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

    public void processBet(BigDecimal amountBet, BigDecimal amountWon) {
        totalAmountBet = totalAmountBet.add(amountBet);
        totalAmountWon = totalAmountWon.add(amountWon);
    }

    public void info() {
        System.out.println(name + " " + totalAmountBet + " " + totalAmountWon);
    }

}
