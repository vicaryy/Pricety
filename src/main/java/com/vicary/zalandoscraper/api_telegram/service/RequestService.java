package com.vicary.zalandoscraper.api_telegram.service;

import com.vicary.zalandoscraper.api_telegram.api_object.File;
import com.vicary.zalandoscraper.api_telegram.api_object.RequestResponse;
import com.vicary.zalandoscraper.api_telegram.api_object.RequestResponseList;
import com.vicary.zalandoscraper.api_telegram.api_object.message.Message;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequest;
import com.vicary.zalandoscraper.api_telegram.api_request.ApiRequestList;
import com.vicary.zalandoscraper.api_telegram.api_request.GetFile;
import com.vicary.zalandoscraper.api_telegram.api_request.InputFile;
import com.vicary.zalandoscraper.api_telegram.api_request.send.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.NoSuchElementException;

public class RequestService {

    private final ParameterizedTypeReferences typeReferences = new ParameterizedTypeReferences();
    private String requestURL;

    public <Request extends ApiRequest<? extends ReturnObject>, ReturnObject> ReturnObject send(Request request) throws WebClientRequestException, WebClientResponseException {
        request.checkValidation();
        String url = getRequestURL() + request.getEndPoint();

        WebClient webClient = WebClient.create();
        RequestResponse response = (RequestResponse) webClient
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeReferences.get(request.getReturnObject().getClass()))
                .block();
        return (ReturnObject) response.getResult();
    }

    public synchronized <Request extends ApiRequest> void sendWithoutResponse(Request request) throws RestClientException {
        request.checkValidation();
        String url = getRequestURL() + request.getEndPoint();

        WebClient.builder()
                .build()
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public <Request extends ApiRequest> void sendWithoutResponseAsync(Request request) throws RestClientException {
        request.checkValidation();
        String url = getRequestURL() + request.getEndPoint();

        WebClient.builder()
                .build()
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    public Message send(SendPhoto sendPhoto) {
        sendPhoto.checkValidation();
        String url = getRequestURL() + sendPhoto.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendPhoto.getChatId());

        inputFile(sendPhoto.getPhoto(), bodyBuilder, sendPhoto.getMethodName());

        if (sendPhoto.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendPhoto.getMessageThreadId());

        if (sendPhoto.getCaption() != null)
            bodyBuilder.part("caption", sendPhoto.getCaption());

        bodyBuilder.part("parse_mode", sendPhoto.getParseMode());

        if (sendPhoto.getParseMode().isEmpty() && sendPhoto.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendPhoto.getCaptionEntities());

        if (sendPhoto.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendPhoto.getHasSpoiler());

        if (sendPhoto.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendPhoto.getDisableNotification());

        if (sendPhoto.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendPhoto.getProtectContent());

        if (sendPhoto.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendPhoto.getReplyToMessageId());

        if (sendPhoto.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendPhoto.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendAudio sendAudio) {
        sendAudio.checkValidation();
        String url = getRequestURL() + sendAudio.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendAudio.getChatId());

        inputFile(sendAudio.getAudio(), bodyBuilder, sendAudio.getMethodName());

        if (sendAudio.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendAudio.getMessageThreadId());

        if (sendAudio.getCaption() != null)
            bodyBuilder.part("caption", sendAudio.getCaption());

        bodyBuilder.part("parse_mode", sendAudio.getParseMode());

        if (sendAudio.getParseMode().isEmpty() && sendAudio.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendAudio.getCaptionEntities());

        if (sendAudio.getDuration() != null)
            bodyBuilder.part("duration", sendAudio.getDuration());

        if (sendAudio.getPerformer() != null)
            bodyBuilder.part("performer", sendAudio.getPerformer());

        if (sendAudio.getTitle() != null)
            bodyBuilder.part("title", sendAudio.getTitle());

        if (sendAudio.getThumbnail() != null)
            inputFile(sendAudio.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendAudio.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendAudio.getDisableNotification());

        if (sendAudio.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendAudio.getProtectContent());

        if (sendAudio.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendAudio.getReplyToMessageId());

        if (sendAudio.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendAudio.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendDocument sendDocument) {
        sendDocument.checkValidation();
        String url = getRequestURL() + sendDocument.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendDocument.getChatId());

        if (sendDocument.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendDocument.getMessageThreadId());

        inputFile(sendDocument.getDocument(), bodyBuilder, sendDocument.getMethodName());

        if (sendDocument.getThumbnail() != null)
            inputFile(sendDocument.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendDocument.getCaption() != null)
            bodyBuilder.part("caption", sendDocument.getCaption());

        bodyBuilder.part("parse_mode", sendDocument.getParseMode());

        if (sendDocument.getParseMode().isEmpty() && sendDocument.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendDocument.getCaptionEntities());

        if (sendDocument.getDisableContentTypeDetection() != null)
            bodyBuilder.part("disable_content_type_detection", sendDocument.getDisableContentTypeDetection());

        if (sendDocument.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendDocument.getDisableNotification());

        if (sendDocument.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendDocument.getProtectContent());

        if (sendDocument.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendDocument.getReplyToMessageId());

        if (sendDocument.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendDocument.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendVideo sendVideo) {
        sendVideo.checkValidation();
        String url = getRequestURL() + sendVideo.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVideo.getChatId());

        if (sendVideo.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVideo.getMessageThreadId());

        inputFile(sendVideo.getVideo(), bodyBuilder, sendVideo.getMethodName());

        if (sendVideo.getDuration() != null)
            bodyBuilder.part("duration", sendVideo.getDuration());

        if (sendVideo.getWidth() != null)
            bodyBuilder.part("width", sendVideo.getWidth());

        if (sendVideo.getHeight() != null)
            bodyBuilder.part("height", sendVideo.getHeight());

        if (sendVideo.getThumbnail() != null)
            inputFile(sendVideo.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendVideo.getCaption() != null)
            bodyBuilder.part("caption", sendVideo.getCaption());

        bodyBuilder.part("parse_mode", sendVideo.getParseMode());

        if (sendVideo.getParseMode().isEmpty() && sendVideo.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendVideo.getCaptionEntities());

        if (sendVideo.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendVideo.getHasSpoiler());

        if (sendVideo.getSupportsStreaming() != null)
            bodyBuilder.part("supports_streaming", sendVideo.getSupportsStreaming());

        if (sendVideo.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVideo.getDisableNotification());

        if (sendVideo.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVideo.getProtectContent());

        if (sendVideo.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVideo.getReplyToMessageId());

        if (sendVideo.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVideo.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendAnimation sendAnimation) {
        sendAnimation.checkValidation();
        String url = getRequestURL() + sendAnimation.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendAnimation.getChatId());

        if (sendAnimation.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendAnimation.getMessageThreadId());

        inputFile(sendAnimation.getAnimation(), bodyBuilder, sendAnimation.getMethodName());

        if (sendAnimation.getDuration() != null)
            bodyBuilder.part("duration", sendAnimation.getDuration());

        if (sendAnimation.getWidth() != null)
            bodyBuilder.part("width", sendAnimation.getWidth());

        if (sendAnimation.getHeight() != null)
            bodyBuilder.part("height", sendAnimation.getHeight());

        if (sendAnimation.getThumbnail() != null)
            inputFile(sendAnimation.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendAnimation.getCaption() != null)
            bodyBuilder.part("caption", sendAnimation.getCaption());

        bodyBuilder.part("parse_mode", sendAnimation.getParseMode());

        if (sendAnimation.getParseMode().isEmpty() && sendAnimation.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendAnimation.getCaptionEntities());

        if (sendAnimation.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendAnimation.getHasSpoiler());

        if (sendAnimation.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendAnimation.getDisableNotification());

        if (sendAnimation.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendAnimation.getProtectContent());

        if (sendAnimation.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendAnimation.getReplyToMessageId());

        if (sendAnimation.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendAnimation.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendVoice sendVoice) {
        sendVoice.checkValidation();
        String url = getRequestURL() + sendVoice.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVoice.getChatId());

        if (sendVoice.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVoice.getMessageThreadId());

        inputFile(sendVoice.getVoice(), bodyBuilder, sendVoice.getMethodName());

        if (sendVoice.getCaption() != null)
            bodyBuilder.part("caption", sendVoice.getCaption());

        bodyBuilder.part("parse_mode", sendVoice.getParseMode());

        if (sendVoice.getParseMode().isEmpty() && sendVoice.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendVoice.getCaptionEntities());

        if (sendVoice.getDuration() != null)
            bodyBuilder.part("duration", sendVoice.getDuration());

        if (sendVoice.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVoice.getDisableNotification());

        if (sendVoice.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVoice.getProtectContent());

        if (sendVoice.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVoice.getReplyToMessageId());

        if (sendVoice.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVoice.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message send(SendVideoNote sendVideoNote) {
        sendVideoNote.checkValidation();
        String url = getRequestURL() + sendVideoNote.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVideoNote.getChatId());

        if (sendVideoNote.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVideoNote.getMessageThreadId());

        inputFile(sendVideoNote.getVideoNote(), bodyBuilder, sendVideoNote.getMethodName());

        if (sendVideoNote.getDuration() != null)
            bodyBuilder.part("duration", sendVideoNote.getDuration());

        if (sendVideoNote.getLength() != null)
            bodyBuilder.part("length", sendVideoNote.getLength());

        if (sendVideoNote.getThumbnail() != null)
            inputFile(sendVideoNote.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendVideoNote.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVideoNote.getDisableNotification());

        if (sendVideoNote.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVideoNote.getProtectContent());

        if (sendVideoNote.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVideoNote.getReplyToMessageId());

        if (sendVideoNote.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVideoNote.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }

    public Message sendSticker(SendSticker sendSticker) {
        sendSticker.checkValidation();
        String url = getRequestURL() + sendSticker.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendSticker.getChatId());

        if (sendSticker.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendSticker.getMessageThreadId());

        inputFile(sendSticker.getSticker(), bodyBuilder, sendSticker.getMethodName());

        if (sendSticker.getEmoji() != null)
            bodyBuilder.part("duration", sendSticker.getEmoji());

        if (sendSticker.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendSticker.getDisableNotification());

        if (sendSticker.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendSticker.getProtectContent());

        if (sendSticker.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendSticker.getReplyToMessageId());

        if (sendSticker.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendSticker.getAllowSendingWithoutReply());

        return send(url, bodyBuilder);
    }


    public Message send(String url, MultipartBodyBuilder bodyBuilder) throws WebClientRequestException, WebClientResponseException {
        WebClient webClient = WebClient.create();
        RequestResponse response = (RequestResponse) webClient
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(typeReferences.get(Message.class))
                .block();
        return (Message) response.getResult();
    }

    public <RequestList extends ApiRequestList<? extends ReturnObject>, ReturnObject> List<ReturnObject> sendRequestList(RequestList request) throws WebClientRequestException, WebClientResponseException {
        request.checkValidation();
        String url = getRequestURL() + request.getEndPoint();

        WebClient webClient = WebClient.create();
        RequestResponseList response = (RequestResponseList) webClient
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeReferences.get(request.getReturnObject().getClass()))
                .block();
        return (List<ReturnObject>) response.getResult();
    }


    private void inputFile(InputFile inputFile, MultipartBodyBuilder bodyBuilder, String methodName) {
        inputFile.checkValidation(methodName);
        String fileId = inputFile.getFileId();

        if (fileId != null) {
            GetFile getFile = new GetFile(fileId);
            File file = send(getFile);
            if (file != null) {
                bodyBuilder.part(methodName, fileId);
                return;
            } else
                throw new NoSuchElementException("fileId: " + fileId + " does not exists on Telegram server.");
        }

        java.io.File file = inputFile.getFile();
        if (file != null) {
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            bodyBuilder.part(methodName, fileSystemResource);
        }
    }

    private String getRequestURL() {
        if (requestURL == null) {
            String botToken = UpdateFetcher.getBotToken();
            if (botToken == null) {
                throw new NoSuchElementException("You have to initialize UpdateFetcher first to use RequestService.");
            }
            requestURL = "https://api.telegram.org/bot" + botToken;
        }
        return requestURL;
    }
}
