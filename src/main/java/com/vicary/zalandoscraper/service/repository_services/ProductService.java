package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.repository.ProductRepository;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
import com.vicary.zalandoscraper.service.map.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    private final ProductMapper mapper;

    private final UserService userService;

    public ProductDTO getProductDTOById(Long productId) {
        return mapper.map(repository.findById(productId).get());
    }

    public void updatePriceById(Long productId, double price) {
        repository.updatePrice(productId, price);
    }

    public void updatePriceAndPriceAlertById(Long productId, double price, String priceAlert) {
        repository.updatePriceAndPriceAlert(productId, price, priceAlert);
    }

    public void updateProductPriceAlert(Long productId, String priceAlert) {
        repository.updatePriceAlert(productId, priceAlert);
    }

    public List<ProductDTO> getAllProductsDtoSortById() {
        return mapper.map(repository.findAll(Sort.by("id")));
    }

    public List<ProductDTO> getAllProductsDtoSortByLink() {
        return mapper.map(repository.findAll(Sort.by("link")));
    }

    public List<ProductDTO> getAllProductsDtoByUserId(String userId) {
        List<ProductEntity> productEntities = repository.findAllByUser(userService.findByUserId(userId).get(), Sort.by("id"));

        if (productEntities.isEmpty())
            return Collections.emptyList();

        return mapper.map(productEntities);
    }

    public void updateProductPrices(List<ProductDTO> productDTOs) {
        for (ProductDTO p : productDTOs) {
            if (p.getPriceAlert().equals("AUTO") || p.getPriceAlert().equals("OFF"))
                updatePriceById(p.getProductId(), p.getNewPrice());

            else {
                double priceAlert = Double.parseDouble(p.getPriceAlert());
                if (p.getNewPrice() <= priceAlert && p.getNewPrice() != 0)
                    updatePriceAndPriceAlertById(p.getProductId(), p.getNewPrice(), "OFF");
                else
                    updatePriceById(p.getProductId(), p.getNewPrice());
            }
        }
    }

    public int countByUserId(String userId) {
        return repository.countByUserId(userId);
    }

    public boolean existsByUserIdAndLinkAndVariant(String userId, String link, String variant) {
        return repository.existByUserIdLinkAndVariant(userId, link, variant) == 1;
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllProductsByUserId(String userId) {
        repository.deleteAllByUserId(userId);
    }

    public void saveProduct(Product product) {
        logger.debug(product.toString());
//        repository.save(mapper.map(product));
//        logger.info("[Product Service] Added new product to database link: {}", product.getLink());
    }

    public ProductEntity getProductById(Long id) {
        return repository.findById(id).get();
    }
}
