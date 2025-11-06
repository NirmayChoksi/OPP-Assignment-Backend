package com.oopAssignment.financeTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePartyRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Type is required (CUSTOMER or SUPPLIER)")
    private String type;

    private String gstNumber;
}
