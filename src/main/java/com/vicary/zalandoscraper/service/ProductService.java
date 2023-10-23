package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    public void getProductDTO() {

//        repository.fin
    }

    public void updateProductPrice(Long productId, double price) {
        repository.updatePrice(productId, price);
    }

    public void updateProductPrice(Long productId, double price, String priceAlert) {
        repository.updatePrice(productId, price, priceAlert);
    }

    public List<ProductDTO> getAllProductsDto() {
        return mapper.map(repository.findAll());
    }

    public void saveProduct(Product product) {
        repository.save(mapper.map(product));
    }

    public ProductEntity getProductById(Long id) {
        return repository.findById(id).get();
    }
}
