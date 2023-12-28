package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.model.User;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.repository_services.ProductHistoryService;
import com.vicary.zalandoscraper.service.repository_services.ProductService;
import com.vicary.zalandoscraper.thread_local.ActiveLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class NotificationManagerTest {

    @Autowired
    private NotificationManager notificationManager;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductHistoryService productHistoryService;
    @MockBean
    private ChatNotificationSender chatSender;
    @MockBean
    private EmailNotificationSender emailSender;
    @MockBean
    private UpdateReceiverService updateReceiverService;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @Test
    void isUserNeedsNotify_expectFalse_PriceAlertIsOFF() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("OFF");

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsZero() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setNewPrice(0);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsHigherThanActualPriceAndPriceAlertIsAUTO() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setPrice(100);
        givenProduct.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsEqualToActualPriceAndPriceAlertIsAUTO() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setPrice(200);
        givenProduct.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsHigherThanPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("100.00");
        givenProduct.setPrice(150);
        givenProduct.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsLowerThanActualPriceAndPriceAlertIsAUTO() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setPrice(200);
        givenProduct.setNewPrice(190);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsLowerThanPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("200.00");
        givenProduct.setPrice(300);
        givenProduct.setNewPrice(190);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsEqualToPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("200.00");
        givenProduct.setPrice(300);
        givenProduct.setNewPrice(200);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(0)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectTrue_PriceAlertAutoPriceIsZeroNewPriceIsNotZeroAndServiceReturnsHigherPrice() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setPrice(0);
        givenProduct.setNewPrice(200);
        double givenLastPositivePrice = 300;

        //when
        when(productHistoryService.getLastPositivePrice(givenProduct.getProductId())).thenReturn(givenLastPositivePrice);


        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(1)).getLastPositivePrice(123L);
    }

    @Test
    void isUserNeedsNotify_expectFalse_PriceAlertAutoPriceIsZeroNewPriceIsNotZeroAndServiceReturnsLowerPrice() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setProductId(123L);
        givenProduct.setPriceAlert("AUTO");
        givenProduct.setPrice(0);
        givenProduct.setNewPrice(200);
        double givenLastPositivePrice = 100;

        //when
        when(productHistoryService.getLastPositivePrice(givenProduct.getProductId())).thenReturn(givenLastPositivePrice);


        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenProduct));
        verify(productHistoryService, times(1)).getLastPositivePrice(123L);
    }


    @Test
    void updatePriceAlertInRepository_notUpdated_PriceAlertIsOFF() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setPriceAlert("OFF");

        //when
        notificationManager.updatePriceAlertInRepository(givenProduct);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_notUpdated_PriceAlertIsAUTO() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setPriceAlert("AUTO");

        //when
        notificationManager.updatePriceAlertInRepository(givenProduct);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_notUpdated_NewPriceIsHigherThanPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setPriceAlert("200.00");
        givenProduct.setNewPrice(210);

        //when
        notificationManager.updatePriceAlertInRepository(givenProduct);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_updated_NewPriceIsLowerThanPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setPriceAlert("200.00");
        givenProduct.setNewPrice(190);

        //when
        notificationManager.updatePriceAlertInRepository(givenProduct);

        //then
        verify(productService, times(1)).updateProductPriceAlert(givenProduct.getProductId(), "OFF");
    }

    @Test
    void updatePriceAlertInRepository_updated_NewPriceIsEqualToPriceAlert() {
        //given
        Product givenProduct = getDefaultProduct();
        givenProduct.setPriceAlert("200.00");
        givenProduct.setNewPrice(200);

        //when
        notificationManager.updatePriceAlertInRepository(givenProduct);

        //then
        verify(productService, times(1)).updateProductPriceAlert(givenProduct.getProductId(), "OFF");
    }

    @Test
    void sendPriceNotifications_expectReturn_ProductsDontNeedsToBeSend() {
        //given
        List<Product> givenList = Arrays.asList(getNoNeedToSendProduct(), getNoNeedToSendProduct(), getNoNeedToSendProduct(), getNoNeedToSendProduct());

        //when
        notificationManager.sendPriceNotifications(givenList);

        //then
        verify(chatSender, times(0)).sendAndSave(anyList());
        verify(emailSender, times(0)).sendAndSave(anyList());
    }

    @Test
    void sendPriceNotifications_expectSend_ProductsNeedsToBeSend() {
        //given
        List<Product> givenList = Arrays.asList(getNeedToSendProduct(), getNeedToSendProduct(), getNeedToSendProduct(), getNeedToSendProduct());

        //when
        notificationManager.sendPriceNotifications(givenList);

        //then
        verify(chatSender, times(1)).sendAndSave(anyList());
        verify(emailSender, times(1)).sendAndSave(anyList());
    }

    @Test
    void sendPriceNotifications_expectSend_ProductsNeedsToBeSendButNotOnEmail() {
        //given
        List<Product> givenList = Arrays.asList(getNeedToSendButNotOnEmailProduct(), getNeedToSendButNotOnEmailProduct());

        //when
        notificationManager.sendPriceNotifications(givenList);

        //then
        verify(chatSender, times(1)).sendAndSave(any());
        verify(emailSender, times(1)).sendAndSave(Collections.emptyList());
    }

    @Test
    void sendWaitingUserNotifications_expectReturn_EmptyList() {
        //given
        List<WaitingUserEntity> givenList = Collections.emptyList();

        //when
        notificationManager.sendWaitingUserNotifications(givenList);

        //then
        verify(chatSender, times(0)).send(anyList());
    }

    @Test
    void sendWaitingUserNotifications_expectReturn_NormalList() {
        //given
        List<WaitingUserEntity> givenList = Arrays.asList(getDefaultWaitingUser(), getDefaultWaitingUser(), getDefaultWaitingUser());

        //when
        notificationManager.sendWaitingUserNotifications(givenList);

        //then
        verify(chatSender, times(1)).send(anyList());
    }

    private Product getDefaultProduct() {
        return Product.builder()
                .productId(123L)
                .name("name")
                .user(User.builder().language("en").build())
                .link("https://www.link.pl/")
                .description("desc")
                .variant("variant")
                .build();
    }

    private Product getNoNeedToSendProduct() {
        return Product.builder()
                .productId(123L)
                .priceAlert("OFF")
                .user(User.builder().language("en").build())
                .link("https://www.link.pl/")
                .name("name")
                .description("desc")
                .variant("variant")
                .build();
    }

    private Product getNeedToSendProduct() {
        return Product.builder()
                .productId(123L)
                .priceAlert("100.00")
                .user(User.builder().language("en").build())
                .link("https://www.link.pl/")
                .serviceName("link.pl")
                .currency("zł")
                .price(200)
                .newPrice(50)
                .name("name")
                .description("desc")
                .variant("variant")
                .build();
    }

    private Product getNeedToSendButNotOnEmailProduct() {
        return Product.builder()
                .productId(123L)
                .priceAlert("100.00")
                .user(User.builder().language("en").notifyByEmail(false).build())
                .link("https://www.link.pl/")
                .serviceName("link.pl")
                .currency("zł")
                .price(200)
                .newPrice(50)
                .name("name")
                .description("desc")
                .variant("variant")
                .build();
    }

    private WaitingUserEntity getDefaultWaitingUser() {
        return new WaitingUserEntity(UserEntity.builder()
                .userId("123")
                .nationality("en")
                .build());
    }
}