package web.ygdragon.webstore.shopping.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.Locale;

/**
 * Spring Integration Configuration
 */
@Configuration
public class IntegrationConfig {

    private static final String FILE_PATH = "./shopping/src/main/resources/saved_files";

    /**
     * Primary message processing - input chanel
     *
     * @return DirectChannel instance
     */
    @Bean
    public MessageChannel receivingChannel() {
        return new DirectChannel();
    }

    /**
     * Write a message to a file - output channel
     *
     * @return DirectChannel instance
     */
    @Bean
    public MessageChannel writeToFileChannel() {
        return new DirectChannel();
    }

    /**
     * Convert message text to uppercase - transformer
     *
     * @return GenericTransformer instance
     */
    @Bean
    @Transformer(inputChannel = "receivingChannel", outputChannel = "writeToFileChannel")
    public GenericTransformer<String, String> transformer() {
        return text -> text.toUpperCase(Locale.ROOT);
    }

    /**
     * Message handling - write to file
     *
     * @return FileWritingMessageHandler instance
     */
    @Bean
    @ServiceActivator(inputChannel = "writeToFileChannel")
    public FileWritingMessageHandler messageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
                new File(FILE_PATH)
        );
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }
}
