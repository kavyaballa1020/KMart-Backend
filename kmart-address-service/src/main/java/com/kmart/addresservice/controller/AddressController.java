package com.kmart.addresservice.controller;

import com.kmart.addresservice.model.Address;
import com.kmart.addresservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public Address addAddress(@RequestBody Address address, @RequestHeader("Authorization") String token) {
        return addressService.addAddress(address, token);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address address, @RequestHeader("Authorization") String token) {
        return addressService.updateAddress(id, address, token);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        addressService.deleteAddress(id, token);
    }

    @GetMapping
    public List<Address> getAllAddresses(@RequestHeader("Authorization") String token) {
        return addressService.getAllAddresses(token);
    }

    @PutMapping("/default/{id}")
    public Address setDefaultAddress(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return addressService.setDefaultAddress(id, token);
    }
}
