package com.demoproject.application;

import com.demoproject.appconfig.AppConfig;
import com.demoproject.appconfig.AppConfigService;
import com.demoproject.appconfig.AppConfigServiceImpl;
import com.demoproject.appconfig.AppConfigStatus;
import com.demoproject.chargedetails.ChargeDetails;
import com.demoproject.common.PropertiesProvider;
import com.demoproject.common.ServiceProvider;
import com.demoproject.common.SingletonServiceProvider;
import com.demoproject.external.ExternalServiceImpl;
import com.demoproject.external.UnlockResponse;
import com.demoproject.chargedetails.ChargeDetailsService;
import com.demoproject.chargedetails.ChargeDetailsServiceImpl;
import com.demoproject.chargefailurelog.ChargeFailureLog;
import com.demoproject.chargefailurelog.ChargeFailureLogService;
import com.demoproject.chargefailurelog.ChargeFailureLogServiceImpl;
import com.demoproject.chargesuccesslog.ChargeSuccessLog;
import com.demoproject.chargesuccesslog.ChargeSuccessLogService;
import com.demoproject.chargesuccesslog.ChargeSuccessLogServiceImpl;
import com.demoproject.external.ExternalService;
import com.demoproject.external.ChargeResponse;
import com.demoproject.keyworddetails.KeywordDetails;
import com.demoproject.keyworddetails.KeywordDetailsService;
import com.demoproject.keyworddetails.KeywordDetailsServiceImpl;
import com.demoproject.message.Message;
import com.demoproject.message.MessageService;
import com.demoproject.message.MessageServiceImpl;
import com.demoproject.message.MessageStatus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {

    private final Logger logger = LogManager.getLogger(ApplicationServiceImpl.class);
    private final ServiceProvider serviceProvider = SingletonServiceProvider.getInstance();
    private final AppConfigService appConfigService = serviceProvider.get(AppConfigServiceImpl.class);
    private final MessageService messageService = serviceProvider.get(MessageServiceImpl.class);
    private final KeywordDetailsService keywordDetailsService = serviceProvider.get(KeywordDetailsServiceImpl.class);
    private final ChargeDetailsService chargeDetailsService = serviceProvider.get(ChargeDetailsServiceImpl.class);
    private final ChargeFailureLogService chargeFailureLogService = serviceProvider.get(ChargeFailureLogServiceImpl.class);
    private final ChargeSuccessLogService chargeSuccessLogService = serviceProvider.get(ChargeSuccessLogServiceImpl.class);
    private final ExternalService externalService = serviceProvider.get(ExternalServiceImpl.class);

    private ApplicationServiceImpl() { }

    private void processMessage(Message message) throws Exception {
        KeywordDetails keywordDetails = keywordDetailsService.getKeywordDetails(message.getKeyword());

        if (keywordDetails == null) {
            throw new Exception("No keyword details found for message with ID, '" + message.getMessageId() + "' and content, '" + message.getContent() + "'.");
        }

        UnlockResponse unlockResponse = externalService.unlock(message.getKeyword(),
                message.getContent(), keywordDetails.getUnlockUrl());
        ChargeDetails chargeDetails = chargeDetailsService.getChargeDetails(unlockResponse.getPrice());

        if (chargeDetails == null) {
            throw new Exception("No charge details found for message with ID, '" + message.getMessageId()
                    + "' content, '" + message.getContent()
                    + "' unlock code, '" + unlockResponse.getUnlockCode()
                    + "' and price, '" + unlockResponse.getPrice() + "'.");
        }

        ChargeResponse chargeResponse = externalService.charge(
                chargeDetails.getChargeCode(),
                message.getMobileNumber(),
                keywordDetails.getChargeUrl());

        // if the transaction is not successful...
        if (chargeResponse.getChargeStatusCode() != 100) {
            ChargeFailureLog log = new ChargeFailureLog(0L, message.getMessageId(), message.getMobileNumber(),
                    keywordDetails.getKeywordDetailsId(), chargeDetails.getPrice(), chargeDetails.getChargeDetailsId(),
                    chargeResponse.getStatusCode(), chargeResponse.getTransactionId(),
                    chargeResponse.getDescription());

            // we shall write failure log...
            chargeFailureLogService.addLogEntry(log);
            // updates the message status to FAILED ('F')...
            messageService.setMessageStatus(message.getMessageId(), MessageStatus.FAILED);

            return;
        }

        // otherwise, we shall write success log...
        ChargeSuccessLog log = new ChargeSuccessLog(0L, message.getMessageId(), message.getMobileNumber(),
                keywordDetails.getKeywordDetailsId(), chargeDetails.getPrice(), chargeDetails.getPriceWithVat(),
                chargeDetails.getValidity(), chargeDetails.getChargeDetailsId(), chargeResponse.getTransactionId(),
                chargeResponse.getDescription());

        // we shall write failure log...
        chargeSuccessLogService.addLogEntry(log);
        // updates the message status to SUCCEEDED ('S')...
        messageService.setMessageStatus(message.getMessageId(), MessageStatus.SUCCEEDED);
    }

    private void processMessages(List<Message> messages) throws Exception {
        for (Message message : messages) {
            processMessage(message);
        }
    }

    @Override
    public void reset() throws Exception {
        logger.log(Level.INFO, "Performing reset operations.");

        // in order to execute this service, status must be set to IN_PROGRESS (1)...
        appConfigService.setStatus(AppConfigStatus.IN_PROGRESS);
        // resets status of all messages to UNPROCESSED ('N')...
        messageService.setStatusForAllMessages(MessageStatus.UNPROCESSED);
        // we shall also clear both failure and success logs...
        chargeFailureLogService.clearAllLogs();
        chargeSuccessLogService.clearAllLogs();

        logger.log(Level.INFO, "Reset operations completed successfully.");
    }

    @Override
    public void execute() throws Exception {
        logger.log(Level.INFO, "Executing application service.");

        // assuming that the keyword details remain unchanged,
        // we shall load all the keyword details from the database...
        keywordDetailsService.loadAllKeywordDetails();
        // similarly, assuming that the charge details remain unchanged,
        // we shall fetch all the charge details from the database...
        chargeDetailsService.loadAllChargeDetails();

        int batchSize = 0;
        int maximumBatchSize = Integer.parseInt(PropertiesProvider.getProperties()
                .getProperty("APPLICATION_MAXIMUM_BATCH_SIZE"));
        long lastMessageId = 0L;
        AppConfig appConfig;

        while (true) {
            appConfig = appConfigService.getAppConfig();

            // if app config is null, we shall skip this iteration...
            if (appConfig == null) { throw new Exception("Application configuration was not found in the database."); }

            // if status is STOPPED (0), we shall break out of the loop and thus,
            // the application exits...
            if (appConfig.getStatus() == AppConfigStatus.STOPPED) {
                logger.log(Level.INFO, "Application closed because the status is set to STOPPED (0).");

                break;
            }

            // we shall retrieve the messages with status UNPROCESSED ('N')...
            List<Message> messages = messageService.getMessagesByStatus(
                    MessageStatus.UNPROCESSED, appConfig.getNumberOfRows(), lastMessageId);

            // if we don't have any message, we shall break out of the loop and thus,
            // the application exits...
            if (messages.size() == 0) {
                logger.log(Level.INFO, "Application closed because no message left to be processed.");

                break;
            }

            // processes all the messages...
            processMessages(messages);

            // we shall store the ID of the last message...
            lastMessageId = messages.get(messages.size() - 1).getMessageId();
            batchSize += messages.size();

            // we shall commit the changes only if the batch size
            // has reached the maximum batch size...
            if (batchSize < maximumBatchSize) { continue; }

            // we shall reset batchSize to zero (0)...
            batchSize = 0;

            // after maximum batch size is reached, we shall commit all
            // the message status updates to the database...
            messageService.commitBatchedChanges(false);
            // we shall also commit all the logs to the database...
            // NOTE: WE COULD'VE OMITTED THESE TWO METHOD CALLS AND COMMIT ALL THE
            // LOGS AT ONCE. BUT TOO LARGE BATCH SIZE WILL CAUSE INCREASED MEMORY
            // FOOTPRINTS WITH NO INCREASE IN PERFORMANCE...
            chargeFailureLogService.commitBatchedChanges(false);
            chargeSuccessLogService.commitBatchedChanges(false);
        }

        logger.log(Level.INFO, "Application service execution completed successfully.");
    }

    @Override
    public void dispose() {
        // finally commits the remaining message status updates
        // to the database and disposes associated resources.
        // e.g. underlying database connections, statements etc...
        messageService.commitBatchedChanges(true);
        // also commits the remaining logs to the database...
        chargeFailureLogService.commitBatchedChanges(true);
        chargeSuccessLogService.commitBatchedChanges(true);
    }
}
