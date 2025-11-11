package com.oopAssignment.financeTracker.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "parties")
@CompoundIndexes({
        @CompoundIndex(name = "user_contact_unique", def = "{'userId': 1, 'contactNumber': 1}", unique = true)
})
public class Party {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid contact number format")
    private String contactNumber;

    @NotBlank(message = "User ID is required")
    private String userId; // owner of this party

    private PartyType type; // ✅ Switched to Enum
    private BigDecimal YOU_GOT;
    private BigDecimal YOU_GAVE;

    private String gstNumber; // optional for supplier
}
