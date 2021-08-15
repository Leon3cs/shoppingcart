package com.dio.experts.shoppingcart.controller;

import com.dio.experts.shoppingcart.model.Cart;
import com.dio.experts.shoppingcart.model.Item;
import com.dio.experts.shoppingcart.repository.CartRepository;
import com.dio.experts.shoppingcart.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/{idCart}/{idItem}")
    public ResponseEntity<?> addItem(@PathVariable("idCart") String id, @PathVariable("idItem") String idItem){
        Optional<Cart> optionalCart = cartRepository.findById(id);
        Cart cart;

        if(optionalCart.isEmpty()){
            cart = new Cart();
        }else{
            cart = optionalCart.get();
        }

        try{
            Item item = itemRepository.findById(idItem).orElseThrow(() -> new NoSuchElementException("Item inválido"));
            List<Item> items = cart.getItems();
            items.add(item);
            cart.setItems(items);
            cart = cartRepository.save(cart);
            return ResponseEntity.ok(cart);
        }catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCarts(){
        List<Cart> carts = new ArrayList<>();

        cartRepository.findAll().forEach((cart) -> carts.add(cart));

        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable("id") String id){
        try{
            Cart cart = cartRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Carrinho não encontrado"));
            return ResponseEntity.ok(cart);
        }catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> clearCart(@PathVariable("id") String id){
        try{
            cartRepository.deleteById(id);
            return ResponseEntity.ok(1);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
