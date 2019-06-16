import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ShoppingListBot extends TelegramLongPollingBot {
    public String getBotToken() {
        return "847013426:AAHE2j-7qTDTz2MYuVsDlHFChnVlVaNrhyI";
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/udword")) {
                SendMessage message = new SendMessage().setChatId(chat_id).setText(lastWordFromUrbanDictionaryDescription()).enableHtml(true);
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

    public String lastWordFromUrbanDictionaryDescription() {
        StringBuilder builder = new StringBuilder();
        Document doc = null;
        String dateString;
        String url = "https://www.urbandictionary.com/?page=1";
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            Elements defPanels = doc.getElementsByClass("def-panel");
            Iterator<Element> defPanelsIterator = defPanels.iterator();
            if (defPanelsIterator.hasNext()) {
                Element defPanel = defPanelsIterator.next();
                Elements dates = defPanel.getElementsByClass("ribbon");
                Iterator<Element> datesIterator = dates.iterator();
                if (datesIterator.hasNext()) {
                    dateString = datesIterator.next().text();
                    builder.append("<i>").append(dateString).append("</i>\n");
                }
                Elements words = defPanel.getElementsByClass("word");
                Iterator<Element> wordsIterator = words.iterator();
                if (wordsIterator.hasNext()) {
                    Element word = wordsIterator.next();
                    builder.append("<b>").append(word.text()).append("</b>\n");
                }
                Elements meanings = defPanel.getElementsByClass("meaning");
                Iterator<Element> meaningsIterator = meanings.iterator();
                if (meaningsIterator.hasNext()) {
                    Element meaning = meaningsIterator.next();
                    String meaningString = meaning.html().replaceAll("<br>", "\n");
                    meaningString = meaningString.replaceAll("\n\n", "\n");
                    builder.append(meaningString);
                }
            }
        }
        return builder.toString();
    }
}
