package com.demoproject.message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByStatus(MessageStatus status, int limit, long lastMessageId) throws Exception;
    void setMessageStatus(long messageId, MessageStatus status) throws Exception;
    void setStatusForAllMessages(MessageStatus status) throws Exception;
    void commitBatchedChanges(boolean shallDisposeResources);
}
