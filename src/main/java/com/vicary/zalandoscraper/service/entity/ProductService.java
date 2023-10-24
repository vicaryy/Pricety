package com.vicary.zalandoscraper.service.entity;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    private final UserService userService;

    public ProductDTO getProductDTOById(Long productId) {
        return mapper.map(repository.findById(productId).get());
    }

    public void updateProductPrice(Long productId, double price) {
        repository.updatePrice(productId, price);
    }

    public void updateProductPrice(Long productId, double price, String priceAlert) {
        repository.updatePrice(productId, price, priceAlert);
    }

    public void updateProductPriceAlert(Long productId, String priceAlert) {
        repository.updatePriceAlert(productId, priceAlert);
    }

    public List<ProductDTO> getAllProductsDto() {
        return mapper.map(repository.findAll());
    }

    public List<ProductDTO> getAllProductsDtoByUserId(String userId) {
        List<ProductEntity> productEntities = repository.findAllByUser(userService.findByUserId(userId).get(), Sort.by("id"));
        if (productEntities.isEmpty())
            return Collections.emptyList();
        return mapper.map(productEntities);
    }

    public void saveProduct(Product product) {
        repository.save(mapper.map(product));
    }

    public ProductEntity getProductById(Long id) {
        return repository.findById(id).get();
    }
}
