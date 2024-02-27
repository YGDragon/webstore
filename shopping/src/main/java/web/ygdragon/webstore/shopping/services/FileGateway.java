package web.ygdragon.webstore.shopping.services;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Message gateway interface
 */
@MessagingGateway(defaultRequestChannel = "receivingChannel")
public interface FileGateway {
    /**
     * Writing message to file
     *
     * @param titleFile File title
     * @param message   Message text
     */
    void writeToFile(@Header(FileHeaders.FILENAME) String titleFile, String message);
}
