package com.kmart.addresservice.service;

import com.kmart.addresservice.model.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(Address address, String token);

    Address updateAddress(Long id, Address address, String token);

    void deleteAddress(Long id, String token);

    List<Address> getAllAddresses(String token);

    Address setDefaultAddress(Long id, String token);
}
