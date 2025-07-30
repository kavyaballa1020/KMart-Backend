package com.kmart.addresservice.service;

import com.kmart.addresservice.client.UserClient;
import com.kmart.addresservice.exception.AddressNotFoundException;
import com.kmart.addresservice.model.Address;
import com.kmart.addresservice.repository.AddressRepository;
import com.kmart.addresservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserClient userClient;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, UserClient userClient) {
        this.addressRepository = addressRepository;
        this.userClient = userClient;
    }

    @Override
    public Address addAddress(Address address, String token) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        Long userId = userClient.getUserId(token); // ✅ now safe
        address.setUserId(userId);
        address.setDefaultAddress(false);
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address updatedAddress, String token) {
        Address address = getVerifiedUserAddress(id, token);
        address.setStreet(updatedAddress.getStreet());
        address.setCity(updatedAddress.getCity());
        address.setState(updatedAddress.getState());
        address.setPostalCode(updatedAddress.getPostalCode());
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id, String token) {
        Address address = getVerifiedUserAddress(id, token);
        addressRepository.delete(address);
    }

    @Override
    public List<Address> getAllAddresses(String token) {
        Long userId = userClient.getUserId(token);
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address setDefaultAddress(Long id, String token) {
        Long userId = userClient.getUserId(token);
        List<Address> addresses = addressRepository.findByUserId(userId);

        for (Address addr : addresses) {
            addr.setDefaultAddress(addr.getId().equals(id));
        }

        addressRepository.saveAll(addresses);
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id " + id));
    }

    private Address getVerifiedUserAddress(Long id, String token) {
        Long userId = userClient.getUserId(token);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id " + id));
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: address does not belong to user");
        }
        return address;
    }
}
