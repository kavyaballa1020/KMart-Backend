package com.kmart.authservice.repository;

import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.model.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByEmail(String email);

    List<Vendor> findByStatus(VendorStatus status);
}
