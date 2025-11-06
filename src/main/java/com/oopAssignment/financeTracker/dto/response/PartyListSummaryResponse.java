package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyListSummaryResponse {

    private BigDecimal totalYouGave;
    private BigDecimal totalYouGot;
    private BigDecimal netBalance;

    private List<PartyWithBalanceResponse> parties;
}
