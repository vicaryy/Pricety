package com.vicary.zalandoscraper.controller;

import com.vicary.zalandoscraper.model.UserDTO;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.repository_services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ApiController {

    private final UserService userService;

    @GetMapping("/api/test")
    public Object test() {
        return userService.findAllUsersDTO();
    }

    @PostMapping(value = "/api/set/users")
    public ResponseEntity<String> setUsersInDatabase(@RequestBody List<UserDTO> users) {
        for (UserDTO userDTO : users)
            System.out.println(userDTO);
        return ResponseEntity.ok("Git xdd");
    }

    @PostMapping(value = "/api/set/products")
    public ResponseEntity<String> setProductsInDatabase(@RequestBody List<ProductDTO> products) {
        for (ProductDTO productDTO : products)
            System.out.println(productDTO);
        return ResponseEntity.ok("Git xdd");
    }
}
