package tutorial;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;


class JokeFetcher {

    private static final String DAD_JOKE_URL = "https://icanhazdadjoke.com/";

    public static String getDadJoke() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(DAD_JOKE_URL)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject.getString("joke");
        }
    }
}



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
//            long chatId = update.getMessage().getChatId();
            String chatId = update.getMessage().getChatId().toString();

            logger.info("Haha we Received message: " + messageText + " from chat ID: " + chatId);

//            SendMessage message = new SendMessage();
//            message.setChatId(chatId);
//            message.setText("You said: " + messageText);

            if (messageText != null && !messageText.isEmpty()) {
                try {
                    String joke = JokeFetcher.getDadJoke();
                    String fullMessage = "Wow, \"" + messageText + "\" is such a good point, here is your financial advice:\n\""+ joke + "\"";
                    SendMessage message = new SendMessage(chatId, fullMessage);
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    SendMessage message = new SendMessage(chatId, "Sorry, I couldn't fetch a joke at this time.");
                    try {
                        execute(message);
                    } catch (TelegramApiException telegramApiException) {
                        telegramApiException.printStackTrace();
                    }
                }

//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                logger.error("Failed to send message", e);
//            }

            }
        }
    }
}


