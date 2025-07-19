package com.kmart.authservice.service;

import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class VendorDetailsService implements UserDetailsService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Vendor vendor = vendorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Vendor not found"));

        return new User(
                vendor.getEmail(),
                vendor.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_VENDOR"))
        );
    }
}
