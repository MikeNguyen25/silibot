package tutorial;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    @Override
    public String getBotUsername() {
        return "FinFill_Bot";
    }

    @Override
    public String getBotToken() {
        return "7155377512:AAHufWFSOITJc0CDEYj-WCrzyGwZ23ubrSc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            logger.info("Received message: " + messageText + " from chat ID: " + chatId);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("You said: " + messageText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                logger.error("Failed to send message", e);
            }
        }
    }
}


