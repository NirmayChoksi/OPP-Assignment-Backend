package com.oopAssignment.financeTracker.dto.response;

import com.oopAssignment.financeTracker.model.Party;

public class PartyResponse {

    private String id;
    private String name;
    private String contactNumber;
    private String type;
    private String gstNumber;

    public PartyResponse(Party party) {
        this.id = party.getId();
        this.name = party.getName();
        this.contactNumber = party.getContactNumber();
        this.type = party.getType();
        this.gstNumber = party.getGstNumber();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getType() { return type; }
    public String getGstNumber() { return gstNumber; }
}
