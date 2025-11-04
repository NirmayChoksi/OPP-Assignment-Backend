package com.oopAssignment.financeTracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parties")
@CompoundIndexes({
    @CompoundIndex(
        name = "user_contact_unique",
        def = "{'userId': 1, 'contactNumber': 1}",
        unique = true
    )
})
public class Party {

    @Id
    private String id;
    private String name;
    private String contactNumber;
    private String userId; // reference to User (owner)
    private String type;   // CUSTOMER or SUPPLIER
    private String gstNumber; // optional for supplier

    public Party() {}

    public Party(String name, String contactNumber, String userId, String type, String gstNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.userId = userId;
        this.type = type;
        this.gstNumber = gstNumber;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
}
