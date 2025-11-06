package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyWithBalanceResponse {

    private String partyId;
    private String name;
    private String contactNumber;

    private BigDecimal youGave;
    private BigDecimal youGot;
    private BigDecimal balance;
}
