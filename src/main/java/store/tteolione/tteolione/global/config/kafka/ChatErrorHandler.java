package store.tteolione.tteolione.global.config.kafka;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex)
    {
        Throwable exception = new MessageDeliveryException("abc");
        if (exception instanceof MessageDeliveryException)
        {

            return handleUnauthorizedException(clientMessage, exception);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex)
    {

        ApiError apiError = new ApiError(
                ex.getMessage());

        return prepareErrorMessage(clientMessage, apiError, String.valueOf(ErrorCodeConstants.UNAUTHORIZED_STRING));

    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ApiError apiError, String errorCode)
    {

        String message = apiError.getErrorMessage();

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(errorCode);
        accessor.setDestination("/pub/message");
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
