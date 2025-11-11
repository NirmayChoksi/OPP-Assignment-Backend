package com.oopAssignment.financeTracker.dto.request;

import java.util.List;

import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;

import lombok.Data;

@Data
public class CreateBillRequest {
    private BillType type;  
    private String partyId;
    private String note;
    private List<BillItem> items; 
}
