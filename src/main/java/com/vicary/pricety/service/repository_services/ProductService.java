package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.ProductEntity;
import com.vicary.pricety.model.Product;
import com.vicary.pricety.model.ProductTemplate;
import com.vicary.pricety.repository.ProductRepository;
import com.vicary.pricety.service.map.ProductMapper;
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

    public List<Product> getAllProductsByUserId(long userId) {
        List<ProductEntity> productEntities = repository.findAllByUser(userService.findByUserId(userId), Sort.by("id"));

        if (productEntities.isEmpty())
            return Collections.emptyList();

        return mapper.map(productEntities);
    }

    public void updateProductAfterUpdate(List<Product> products) {
        for (Product p : products) {
            if (p.isNotifyWhenAvailable() && p.getNewPrice() != 0)
                updateProductNotifyWhenAvailable(p.getProductId(), false);

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

    public int countByUserId(long userId) {
        return repository.countByUserId(userId);
    }

    public boolean existsByUserIdAndLinkAndVariant(long userId, String link, String variant) {
        return repository.existByUserIdLinkAndVariant(userId, link, variant) == 1;
    }

    public void deleteProductById(long id) {
        repository.deleteByProductId(id);
        logger.info("Deleted product id: '{}'", id);
    }

    public void deleteAllProductsByUserId(long userId) {
        repository.deleteAllByUserId(userId);
    }

    public void saveProduct(Product product, long userId) {
        repository.save(mapper.map(product, userService.findByUserId(userId)));
        logger.info("[Product Service] Added new product to database link: {}", product.getLink());
    }

    public void saveProduct(Product product, String userEmail) {
        repository.save(mapper.map(product, userService.findWebUserByEmail(userEmail)));
        logger.info("[Product Service] Added new product to database link: {}", product.getLink());
    }


    public boolean existsById(long productId) {
        return repository.existsById(productId);
    }

    public void updateProduct(long productId, String title, String description, String alert) {
        if (!isAlertValid(productId, alert))
            return;
        repository.updateTitleDescriptionAlert(productId, title, description, alert);
    }

    private boolean isAlertValid(long productId, String alert) {
        if (alert.equalsIgnoreCase("AUTO") || alert.equalsIgnoreCase("OFF"))
            return true;

        if (alert.contains(","))
            alert = alert.replaceFirst(",", ".");

        double a;
        try {
            a = Double.parseDouble(alert);
        } catch (Exception ex) {
            return false;
        }

        double productPrice = getProduct(productId).getPrice();
        return productPrice == 0 || productPrice > a;
    }

    public void updateProductNotifyWhenAvailable(long productId, boolean notify) {
        repository.updateNotifyWhenAvailable(productId, notify);
    }

    public List<ProductTemplate> getTemplatesByUserEmail(String email) {
        return mapper.mapToTemplate(getAllProductsByUserId(userService.findWebUserByEmail(email).getUserId()));
    }
}































