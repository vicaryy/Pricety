package com.vicary.pricety.controller.scraper;

import com.vicary.pricety.entity.UpdatedVariantsEntity;
import com.vicary.pricety.entity.WaitingVariantsEntity;
import com.vicary.pricety.service.repository_services.UpdatedVariantsService;
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


    @GetMapping("variants")
    public ResponseEntity<WaitingVariantsEntity> getWaitingVariants() {
        return ResponseEntity.ok(waitingVariantsService.getFirst());
    }

    @PostMapping("variants")
    public ResponseEntity<?> updateVariants(@RequestBody UpdatedVariantsEntity entity) {
        System.out.println("no był tu");
        updatedVariantsService.save(entity);
        return ResponseEntity.ok().build();
    }
}
