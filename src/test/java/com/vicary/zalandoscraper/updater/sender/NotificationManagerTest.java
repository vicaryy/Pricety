package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.entity.UserEntity;
import com.vicary.zalandoscraper.entity.WaitingUserEntity;
import com.vicary.zalandoscraper.service.dto.ProductDTO;
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
    private ChatNotificationSender chatSender;
    @MockBean
    private EmailNotificationSender emailSender;

    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
    }

    @Test
    void isUserNeedsNotify_expectFalse_PriceAlertIsOFF() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("OFF");

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsZero() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("AUTO");
        givenDto.setNewPrice(0);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsHigherThanActualPriceAndPriceAlertIsAUTO() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("AUTO");
        givenDto.setPrice(100);
        givenDto.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsEqualToActualPriceAndPriceAlertIsAUTO() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("AUTO");
        givenDto.setPrice(200);
        givenDto.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectFalse_NewPriceIsHigherThanPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("100.00");
        givenDto.setPrice(150);
        givenDto.setNewPrice(200);

        //when
        //then
        assertFalse(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsLowerThanActualPriceAndPriceAlertIsAUTO() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("AUTO");
        givenDto.setPrice(200);
        givenDto.setNewPrice(190);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsLowerThanPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("200.00");
        givenDto.setPrice(300);
        givenDto.setNewPrice(190);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenDto));
    }

    @Test
    void isUserNeedsNotify_expectTrue_NewPriceIsEqualToPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("200.00");
        givenDto.setPrice(300);
        givenDto.setNewPrice(200);

        //when
        //then
        assertTrue(notificationManager.isUserNeedsNotify(givenDto));
    }


    @Test
    void updatePriceAlertInRepository_notUpdated_PriceAlertIsOFF() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("OFF");

        //when
        notificationManager.updatePriceAlertInRepository(givenDto);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_notUpdated_PriceAlertIsAUTO() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("AUTO");

        //when
        notificationManager.updatePriceAlertInRepository(givenDto);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_notUpdated_NewPriceIsHigherThanPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("200.00");
        givenDto.setNewPrice(210);

        //when
        notificationManager.updatePriceAlertInRepository(givenDto);

        //then
        verify(productService, times(0)).updateProductPriceAlert(anyLong(), anyString());
    }

    @Test
    void updatePriceAlertInRepository_updated_NewPriceIsLowerThanPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("200.00");
        givenDto.setNewPrice(190);

        //when
        notificationManager.updatePriceAlertInRepository(givenDto);

        //then
        verify(productService, times(1)).updateProductPriceAlert(givenDto.getProductId(), "OFF");
    }

    @Test
    void updatePriceAlertInRepository_updated_NewPriceIsEqualToPriceAlert() {
        //given
        ProductDTO givenDto = getDefaultProductDTO();
        givenDto.setPriceAlert("200.00");
        givenDto.setNewPrice(200);

        //when
        notificationManager.updatePriceAlertInRepository(givenDto);

        //then
        verify(productService, times(1)).updateProductPriceAlert(givenDto.getProductId(), "OFF");
    }

    @Test
    void sendPriceNotifications_expectReturn_DTOsDontNeedsToBeSend() {
        //given
        List<ProductDTO> givenList = Arrays.asList(getNoNeedToSendProductDTO(), getNoNeedToSendProductDTO(), getNoNeedToSendProductDTO(), getNoNeedToSendProductDTO());

        //when
        notificationManager.sendPriceNotifications(givenList);

        //then
        verify(chatSender, times(0)).sendAndSave(anyList());
        verify(emailSender, times(0)).sendAndSave(anyList());
    }

    @Test
    void sendPriceNotifications_expectSend_DTOsNeedsToBeSend() {
        //given
        List<ProductDTO> givenList = Arrays.asList(getNeedToSendProductDTO(), getNeedToSendProductDTO(), getNeedToSendProductDTO(), getNeedToSendProductDTO());

        //when
        notificationManager.sendPriceNotifications(givenList);

        //then
        verify(chatSender, times(1)).sendAndSave(anyList());
        verify(emailSender, times(1)).sendAndSave(anyList());
    }

    @Test
    void sendPriceNotifications_expectSend_DTOsNeedsToBeSendButNotOnEmail() {
        //given
        List<ProductDTO> givenList = Arrays.asList(getNeedToSendButNotOnEmailProductDTO(), getNeedToSendButNotOnEmailProductDTO());

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

    private ProductDTO getDefaultProductDTO() {
        return ProductDTO.builder()
                .productId(123L)
                .name("name")
                .language("en")
                .link("https://www.link.pl/")
                .description("desc")
                .variant("variant")
                .build();
    }

    private ProductDTO getNoNeedToSendProductDTO() {
        return ProductDTO.builder()
                .productId(123L)
                .priceAlert("OFF")
                .language("en")
                .link("https://www.link.pl/")
                .name("name")
                .description("desc")
                .variant("variant")
                .build();
    }

    private ProductDTO getNeedToSendProductDTO() {
        return ProductDTO.builder()
                .productId(123L)
                .priceAlert("100.00")
                .language("en")
                .link("https://www.link.pl/")
                .price(200)
                .newPrice(50)
                .name("name")
                .description("desc")
                .variant("variant")
                .build();
    }

    private ProductDTO getNeedToSendButNotOnEmailProductDTO() {
        return ProductDTO.builder()
                .productId(123L)
                .priceAlert("100.00")
                .language("en")
                .link("https://www.link.pl/")
                .price(200)
                .newPrice(50)
                .name("name")
                .description("desc")
                .variant("variant")
                .notifyByEmail(false)
                .build();
    }

    private WaitingUserEntity getDefaultWaitingUser() {
        return new WaitingUserEntity(UserEntity.builder()
                .userId("123")
                .nationality("en")
                .build());
    }
}