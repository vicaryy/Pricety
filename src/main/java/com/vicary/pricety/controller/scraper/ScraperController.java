package com.vicary.pricety.controller.scraper;

import com.vicary.pricety.entity.*;
import com.vicary.pricety.service.repository_services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/scraper/request")
public class ScraperController {

    private final UpdatedVariantsService updatedVariantsService;
    private final WaitingVariantsService waitingVariantsService;
    private final UpdatedProductService updatedProductService;
    private final WaitingProductService waitingProductService;
    private final WaitingProductPriceService waitingProductPriceService;
    private final UpdatedProductPriceService updatedProductPriceService;


    @GetMapping("variants")
    public ResponseEntity<WaitingVariantsEntity> getWaitingVariants() {
        return ResponseEntity.ok(waitingVariantsService.getFirstAndDelete());
    }

    @PostMapping("variants")
    public ResponseEntity<?> updateVariants(@RequestBody UpdatedVariantsEntity entity) {
        updatedVariantsService.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("product")
    public ResponseEntity<WaitingProductEntity> getWaitingProduct() {
        return ResponseEntity.ok(waitingProductService.getFirstAndDelete());
    }

    @PostMapping("product")
    public ResponseEntity<?> updateProduct(@RequestBody UpdatedProductEntity entity) {
        updatedProductService.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("products")
    public ResponseEntity<List<WaitingProductPriceEntity>> getWaitingProductsPrice() {
        return ResponseEntity.ok(waitingProductPriceService.getAllAndDelete());
    }

    @PostMapping("products")
    public ResponseEntity<?> updateProductsPrice(@RequestBody List<UpdatedProductPriceEntity> u) {
        updatedProductPriceService.saveAll(u);
        return ResponseEntity.ok().build();
    }
}
