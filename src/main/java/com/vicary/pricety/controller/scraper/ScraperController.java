package com.vicary.pricety.controller.scraper;

import com.vicary.pricety.entity.UpdatedProductEntity;
import com.vicary.pricety.entity.UpdatedVariantsEntity;
import com.vicary.pricety.entity.WaitingProductEntity;
import com.vicary.pricety.entity.WaitingVariantsEntity;
import com.vicary.pricety.service.repository_services.UpdatedProductService;
import com.vicary.pricety.service.repository_services.UpdatedVariantsService;
import com.vicary.pricety.service.repository_services.WaitingProductService;
import com.vicary.pricety.service.repository_services.WaitingVariantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/scraper/request")
public class ScraperController {

    private final UpdatedVariantsService updatedVariantsService;
    private final WaitingVariantsService waitingVariantsService;
    private final UpdatedProductService updatedProductService;
    private final WaitingProductService waitingProductService;


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
}
