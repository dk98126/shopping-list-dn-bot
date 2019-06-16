import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;

public class ShoppingListBot extends TelegramLongPollingBot {
    public String getBotToken() {
        return "847013426:AAHE2j-7qTDTz2MYuVsDlHFChnVlVaNrhyI";
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/start")) {
                SendMessage message = new SendMessage().setChatId(chat_id).setText(message_text);
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("/pic")) {
                SendPhoto message = new SendPhoto().setChatId(chat_id)
                        .setPhoto("AgADAgAD36oxG52vMUheQSgTB0JSf5RrUw8ABOLV4pVOGRXy64gEAAEC")
                        .setCaption("Photo");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage()
                        .setChatId(chat_id).setText("Unknown command");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBotUsername() {
        return "ShoppingListBot";
    }
}
