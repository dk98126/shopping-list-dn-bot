import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoppingListBot extends TelegramLongPollingBot {
    public String getBotToken() {
        return "847013426:AAHE2j-7qTDTz2MYuVsDlHFChnVlVaNrhyI";
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/udword")) {
                SendMessage message = new SendMessage().setChatId(chat_id).setText("<b>Term:<b>\n" +
                        "alternative\n" +
                        "\n" +
                        "<b>Meaning:</b>\n" +
                        "A <a href=\"https://www.urbandictionary.com/define.php?term=label\">label</a> for the label-less\n" +
                        "\n" +
                        "<b>Examples:</b>\n" +
                        "<i>*Some dude*: <a href=\"https://www.urbandictionary.com/define.php?term=Why%20don%27t%20you\">Why don't you</a> dress like all your \"punk\" friends? \n" +
                        " \n" +
                        "*<a href=\"https://www.urbandictionary.com/define.php?term=Alternative\">Alternative</a>*: Because <a href=\"https://www.urbandictionary.com/define.php?term=I%20don%27t%20want%20to\">I don't want to</a>...</i>\n").enableHtml(true);
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

    public String getRandomWordFromUrbanDictionary() {
        StringBuilder builder = new StringBuilder();
        Document doc = null;
        Random random = new Random();
        String url = "https://www.urbandictionary.com/random.php?page=" + (random.nextInt(999) + 1);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        if (doc != null) {
            Elements defPanels = doc.getElementsByClass("def-panel");
            Iterator<Element> defPanelsIterator = defPanels.iterator();
            if (defPanelsIterator.hasNext()) {
                Element defPanel = defPanels.get(random.nextInt(defPanels.size() - 1));
                Elements words = defPanel.getElementsByClass("word");
                Iterator<Element> wordsIterator = words.iterator();
                if (wordsIterator.hasNext()) {
                    Element word = wordsIterator.next();
                    builder.append("<b>Term:<b>\n").append(word.text()).append("\n\n");
                }
                Elements meanings = defPanel.getElementsByClass("meaning");
                Iterator<Element> meaningsIterator = meanings.iterator();
                if (meaningsIterator.hasNext()) {
                    Element meaning = meaningsIterator.next();
                    String meaningString = getHrefsAndText(meaning);
                    builder.append("<b>Meaning:</b>\n").append(meaningString).append("\n\n");
                }
                Elements examples = defPanel.getElementsByClass("example");
                Iterator<Element> examplesIterator = examples.iterator();
                if (examplesIterator.hasNext()) {
                    Element example = examplesIterator.next();
                    String exampleString = getHrefsAndText(example);
                    builder.append("<b>Examples:</b>\n").append("<i>").append(exampleString).append("</i>");
                }
            }
        }
        System.out.println(builder.toString());
        return builder.toString();
    }

    public static String getHrefsAndText(Element source) {
        Pattern hrefPatter = Pattern.compile("href=\"[^\"]*\"");
        String[] stringArray = source.html().split("</a>");
        StringBuilder builder = new StringBuilder();
        for (String string : stringArray) {
            Matcher matcher = hrefPatter.matcher(string);
            if (matcher.find()) {
                String href = matcher.group(0).replace("href=", "").replaceAll("\"", "");
                String url = "href=\"https://www.urbandictionary.com" + href + "\"";
                string = string.replaceAll("<a.*>", "<a " + url + ">");
                builder.append(string).append("</a>");
            } else builder.append(string);
        }
        return builder.toString().replaceAll("\n", "").replaceAll("<br>", "\n");
    }
}
