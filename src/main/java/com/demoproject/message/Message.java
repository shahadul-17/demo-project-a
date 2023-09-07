package com.demoproject.message;

import com.demoproject.utils.JsonSerializer;
import com.demoproject.utils.StringUtils;

public class Message {

    private long messageId;                                     // id
    private String mobileNumber;                                // msisdn
    private String keyword = "";
    private String content = "";                                // sms_text
    private MessageStatus status = MessageStatus.NONE;          // status

    public Message() { }

    public Message(long messageId, String mobileNumber, String content, MessageStatus status) {
        this.setMessageId(messageId);
        this.setMobileNumber(mobileNumber);
        this.setContent(content);
        this.setStatus(status);
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (StringUtils.isNullOrWhiteSpace(content)) { return; }

        this.content = content.trim();
        this.keyword = extractKeyword(this.content);
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }

    private static String extractKeyword(String message) {
        int indexOfSpace = message.indexOf(' ');

        if (indexOfSpace == -1) { return ""; }

        return message.substring(0, indexOfSpace);
    }
}
