package com.kmart.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String companyName;

    private String phoneNumber;
    
    

    @Enumerated(EnumType.STRING)
    private VendorStatus status = VendorStatus.PENDING;
    
    @Column(nullable = false)
    private boolean approved = false; // ✅ Add this line

    // Add getter and setter for approved


    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    // ==== Getters and Setters ====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VendorStatus getStatus() {
        return status;
    }

    public void setStatus(VendorStatus status) {
        this.status = status;
    }

    // ✅ Custom helper method
    public boolean isApproved() {
        return this.status == VendorStatus.APPROVED;
    }
}
