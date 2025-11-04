package com.oopAssignment.financeTracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreatePartyRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Type is required (CUSTOMER or SUPPLIER)")
    private String type;

    private String gstNumber; // optional

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
}
