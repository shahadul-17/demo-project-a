package com.demoproject.message;

import java.util.List;

public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository = new MessageRepository();

    private MessageServiceImpl() { }

    private void validateStatus(MessageStatus status) throws Exception {
        if (status != MessageStatus.NONE) { return; }

        throw new Exception("Status can either be UNPROCESSED, SUCCESSFUL or FAILED.");
    }

    @Override
    public List<Message> getMessagesByStatus(MessageStatus status, int limit, long lastMessageId) throws Exception {
        validateStatus(status);

        return repository.findByStatus(status, limit, lastMessageId);
    }

    @Override
    public void setMessageStatus(long messageId, MessageStatus status) throws Exception {
        validateStatus(status);

        repository.updateStatusById(messageId, status);
    }

    @Override
    public void commitBatchedChanges(boolean shallDisposeResources) {
        repository.commitBatchedCommands(shallDisposeResources);
    }

    @Override
    public void setStatusForAllMessages(MessageStatus status) throws Exception {
        validateStatus(status);

        repository.updateStatusForAll(status);
    }
}
