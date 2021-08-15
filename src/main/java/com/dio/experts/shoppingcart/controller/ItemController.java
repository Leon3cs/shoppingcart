package com.dio.experts.shoppingcart.controller;

import com.dio.experts.shoppingcart.model.Item;
import com.dio.experts.shoppingcart.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item item){
        return ResponseEntity.ok(itemRepository.save(item));
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems(){
        List<Item> items = new ArrayList<>();

        itemRepository.findAll().forEach((item) -> items.add(item));

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable("id") String id){
        try{
            Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Item não encontrado"));
            return ResponseEntity.ok(item);
        }catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable("id") String id){
        try{
            itemRepository.deleteById(id);
            return ResponseEntity.ok(1);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
