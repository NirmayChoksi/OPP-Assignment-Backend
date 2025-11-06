package com.oopAssignment.financeTracker.dto.response;

import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.model.PartyType;

import lombok.Data;

@Data
public class PartyResponse {

    private String id;
    private String name;
    private String contactNumber;
    private PartyType type;
    private String gstNumber;

    public PartyResponse(Party party) {
        this.id = party.getId();
        this.name = party.getName();
        this.contactNumber = party.getContactNumber();
        this.type = party.getType();
        this.gstNumber = party.getGstNumber();
    }
}
