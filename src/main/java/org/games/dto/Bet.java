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

    private BigDecimal amount;

    private BigDecimal amountWon;

    private BetType betType;

}
