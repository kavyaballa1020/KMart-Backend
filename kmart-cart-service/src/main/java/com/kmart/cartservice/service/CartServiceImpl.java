package com.kmart.cartservice.service;

import com.kmart.cartservice.feign.ProductClient;
import com.kmart.cartservice.model.Cart;
import com.kmart.cartservice.model.CartItem;
import com.kmart.cartservice.payload.ProductDto;
import com.kmart.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final ProductClient productClient;

    @Autowired
    public CartServiceImpl(CartRepository cartRepo, ProductClient productClient) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
    }

    @Override
    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepo.findById(userId).orElse(new Cart());
        cart.setUserId(userId);

        ProductDto product = productClient.getProductById(productId);

        boolean itemExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setCart(cart);
            cart.getItems().add(item);
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart updateItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepo.findById(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }
        return cartRepo.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepo.findById(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepo.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepo.findById(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
