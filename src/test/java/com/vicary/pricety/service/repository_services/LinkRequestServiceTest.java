package com.vicary.pricety.service.repository_services;

import com.vicary.pricety.entity.LinkRequestEntity;
import com.vicary.pricety.exception.InvalidLinkException;
import com.vicary.pricety.repository.LinkRequestRepository;
import com.vicary.pricety.service.UpdateReceiverService;
import com.vicary.pricety.thread_local.ActiveLanguage;
import com.vicary.pricety.thread_local.ActiveUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LinkRequestServiceTest {
    @Autowired
    private LinkRequestService service;
    @MockBean
    private LinkRequestRepository repository;

    @MockBean
    private UpdateReceiverService updateReceiverService;


    @BeforeAll
    static void beforeAll() {
        ActiveLanguage.get().setResourceBundle(ResourceBundle.getBundle("messages", Locale.of("en")));
        ActiveUser.get().setTelegramId("123");
    }

    @Test
    void findByRequestIdAndDelete_expectValid_ValidLinkRequest() {
        //given
        String givenRequestId = "123";
        Optional<LinkRequestEntity> givenEntity = getValidLinkRequestEntity();
        LinkRequestEntity expectedEntity = getValidLinkRequestEntity().get();

        //when
        when(repository.findByRequestId(givenRequestId)).thenReturn(givenEntity);
        LinkRequestEntity actualEntity = service.getAndDeleteByRequestId(givenRequestId);

        //then
        assertEquals(expectedEntity, actualEntity);
        verify(repository, times(1)).findByRequestId(givenRequestId);
        verify(repository, times(1)).deleteByRequestId(givenRequestId);
    }

    @Test
    void findByRequestIdAndDelete_expectThrow_LinkRequestNotFound() {
        //given
        String givenRequestId = "123";

        //when
        when(repository.findByRequestId(givenRequestId)).thenReturn(Optional.empty());

        //then
        assertThrows(InvalidLinkException.class, () -> service.getAndDeleteByRequestId(givenRequestId));
        verify(repository, times(1)).findByRequestId(givenRequestId);
        verify(repository, times(0)).deleteByRequestId(givenRequestId);
    }

    @Test
    void findByRequestIdAndDelete_expectThrow_LinkRequestExpired() {
        //given
        String givenRequestId = "123";
        Optional<LinkRequestEntity> givenEntity = getExpiredLinkRequestEntity();

        //when
        when(repository.findByRequestId(givenRequestId)).thenReturn(givenEntity);

        //then
        assertThrows(InvalidLinkException.class, () -> service.getAndDeleteByRequestId(givenRequestId));
        verify(repository, times(1)).findByRequestId(givenRequestId);
        verify(repository, times(1)).deleteByRequestId(givenRequestId);
    }


    private Optional<LinkRequestEntity> getValidLinkRequestEntity() {
        return Optional.of(LinkRequestEntity.builder()
                .requestId("123")
                .link("https://www.link.pl/")
                .expiration(System.currentTimeMillis() + 100000)
                .build());
    }

    private Optional<LinkRequestEntity> getExpiredLinkRequestEntity() {
        return Optional.of(LinkRequestEntity.builder()
                .requestId("123")
                .link("https://www.link.pl/")
                .expiration(System.currentTimeMillis() - 1)
                .build());
    }
}
