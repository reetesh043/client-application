package com.example.client.kyc.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private BigInteger clientId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "TAX_ID")
    private String taxId;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "KYC_STATUS")
    private String kycStatus;
}

