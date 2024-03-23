package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.ProductEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.ProductTemplate;
import com.vicary.zalandoscraper.repository.ProductRepository;
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

    public List<Product> getAll() {
        return mapper.map(repository.findAll());
    }

    public List<ProductTemplate> getAllTemplates() {
        return mapper.mapToTemplate(getAll());
    }

    public Product getProduct(Long productId) {
        return mapper.map(repository.findById(productId).orElseThrow());
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

    public List<Product> getAllProductsSortById() {
        return mapper.map(repository.findAll(Sort.by("id")));
    }

    public List<Product> getAllProductsSortByLink() {
        return mapper.map(repository.findAll(Sort.by("link")));
    }

    public List<Product> getAllProductsByTelegramId(String telegramId) {
        List<ProductEntity> productEntities = repository.findAllByUser(userService.findByTelegramId(telegramId), Sort.by("id"));

        if (productEntities.isEmpty())
            return Collections.emptyList();

        return mapper.map(productEntities);
    }

    public void updateProductPrices(List<Product> products) {
        for (Product p : products) {
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

    public int countByTelegramId(String telegramId) {
        return repository.countByTelegramId(userService.getUserIdByTelegramId(telegramId));
    }

    public boolean existsByTelegramIdAndLinkAndVariant(String telegramId, String link, String variant) {
        return repository.existByTelegramIdLinkAndVariant(userService.getUserIdByTelegramId(telegramId), link, variant) == 1;
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllProductsByTelegramId(String telegramId) {
        repository.deleteAllByUserId(userService.getUserIdByTelegramId(telegramId));
    }

    public void saveProduct(Product product, String telegramId) {
        repository.save(mapper.map(product, userService.findByTelegramId(telegramId)));
        logger.info("[Product Service] Added new product to database link: {}", product.getLink());
    }

    public boolean existsById(long productId) {
        return repository.existsById(productId);
    }
}
