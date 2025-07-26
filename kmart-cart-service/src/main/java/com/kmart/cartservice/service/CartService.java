package com.kmart.cartservice.service;

import com.kmart.cartservice.model.Cart;
import com.kmart.cartservice.model.CartItem;

public interface CartService {
    Cart addItemToCart(Long userId, Long productId, int quantity);
    Cart updateItemQuantity(Long userId, Long productId, int quantity);
    Cart removeItemFromCart(Long userId, Long productId);
    Cart getCartByUserId(Long userId);
}
