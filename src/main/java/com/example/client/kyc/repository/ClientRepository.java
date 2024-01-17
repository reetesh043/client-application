package com.example.client.kyc.repository;

import com.example.client.kyc.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ClientRepository extends JpaRepository<Client, BigInteger> {
}
