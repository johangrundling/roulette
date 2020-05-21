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
public class Player {

    private String name;

    private BigDecimal totalAmountBet = BigDecimal.ZERO;

    private BigDecimal totalAmountWon = BigDecimal.ZERO;

}
