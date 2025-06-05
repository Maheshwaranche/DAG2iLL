package com.dag2ill;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.*;

@Component
public class PdfTelegramBot extends TelegramLongPollingBot {

    private final Map<String, String> pdfMap = Map.of(
        "Stress","pdfs/stress.pdf",
        "Social Comparison", "pdfs/social_comparison.pdf",
        "Overthinking", "pdfs/overthinking.pdf",
        "Depression", "pdfs/depression.pdf",
        "Loneliness", "pdfs/Loneliness.pdf",
        "Burnout", "pdfs/burn_out.pdf",
        "Anxiety", "pdfs/anxiety.pdf",
        "Negative Thinking", "pdfs/negative_thinking.pdf"
    );


    
    private final Map<String, String> keywordToPdfKey = new HashMap<>();

    public PdfTelegramBot() {
        // Loneliness
        keywordToPdfKey.put("loneliness", "Loneliness");
        keywordToPdfKey.put("lonely", "Loneliness");
        keywordToPdfKey.put("isolation", "Loneliness");
        keywordToPdfKey.put("emotional disconnection", "Loneliness");
        keywordToPdfKey.put("feeling alone", "Loneliness");
        keywordToPdfKey.put("lack of companionship", "Loneliness");
        keywordToPdfKey.put("social withdrawal", "Loneliness");
        keywordToPdfKey.put("empty feeling", "Loneliness");
        keywordToPdfKey.put("need for connection", "Loneliness");
        keywordToPdfKey.put("solitude", "Loneliness");
        keywordToPdfKey.put("support network", "Loneliness");
        keywordToPdfKey.put("building relationships", "Loneliness");

        // Stress
        keywordToPdfKey.put("stress", "Stress");
        keywordToPdfKey.put("mental pressure", "Stress");
        keywordToPdfKey.put("tension", "Stress");
        keywordToPdfKey.put("overload", "Stress");
        keywordToPdfKey.put("coping strategies", "Stress");
        keywordToPdfKey.put("work-life imbalance", "Stress");
        keywordToPdfKey.put("fight or flight", "Stress");
        keywordToPdfKey.put("burnout risk", "Stress");
        keywordToPdfKey.put("stress management", "Stress");
        keywordToPdfKey.put("relaxation techniques", "Stress");
        keywordToPdfKey.put("mind-body balance", "Stress");

        // Overthinking
        keywordToPdfKey.put("overthinking", "Overthinking");
        keywordToPdfKey.put("racing thoughts", "Overthinking");
        keywordToPdfKey.put("mental clutter", "Overthinking");
        keywordToPdfKey.put("analysis paralysis", "Overthinking");
        keywordToPdfKey.put("rumination", "Overthinking");
        keywordToPdfKey.put("excessive thinking", "Overthinking");
        keywordToPdfKey.put("worry loop", "Overthinking");
        keywordToPdfKey.put("can't switch off", "Overthinking");
        keywordToPdfKey.put("mind overdrive", "Overthinking");
        keywordToPdfKey.put("obsessive thoughts", "Overthinking");
        keywordToPdfKey.put("intrusive thinking", "Overthinking");

        // Anxiety
        keywordToPdfKey.put("anxiety", "Anxiety");
        keywordToPdfKey.put("nervousness", "Anxiety");
        keywordToPdfKey.put("fear of future", "Anxiety");
        keywordToPdfKey.put("panic", "Anxiety");
        keywordToPdfKey.put("uneasiness", "Anxiety");
        keywordToPdfKey.put("restlessness", "Anxiety");
        keywordToPdfKey.put("overwhelm", "Anxiety");
        keywordToPdfKey.put("anxious thoughts", "Anxiety");
        keywordToPdfKey.put("social anxiety", "Anxiety");
        keywordToPdfKey.put("anticipatory worry", "Anxiety");
        keywordToPdfKey.put("heart palpitations", "Anxiety");

        // Negative Thoughts
        keywordToPdfKey.put("negative thinking", "Negative Thinking");
        keywordToPdfKey.put("negative thoughts", "Negative Thinking");
        keywordToPdfKey.put("self-doubt", "Negative Thinking");
        keywordToPdfKey.put("pessimism", "Negative Thinking");
        keywordToPdfKey.put("inner critic", "Negative Thinking");
        keywordToPdfKey.put("toxic thinking", "Negative Thinking");
        keywordToPdfKey.put("cognitive distortions", "Negative Thinking");
        keywordToPdfKey.put("negative self-talk", "Negative Thinking");
        keywordToPdfKey.put("hopelessness", "Negative Thinking"); // Only once
        keywordToPdfKey.put("catastrophizing", "Negative Thinking");
        keywordToPdfKey.put("limiting beliefs", "Negative Thinking");
        keywordToPdfKey.put("emotional traps", "Negative Thinking");

        // Depression
        keywordToPdfKey.put("depression", "Depression");
        keywordToPdfKey.put("depressed", "Depression");
        keywordToPdfKey.put("low mood", "Depression");
        keywordToPdfKey.put("loss of interest", "Depression");
        keywordToPdfKey.put("fatigue", "Depression");
        keywordToPdfKey.put("emotional numbness", "Depression");
        keywordToPdfKey.put("persistent sadness", "Depression");
        keywordToPdfKey.put("self-isolation", "Depression");
        keywordToPdfKey.put("crying spells", "Depression");
        keywordToPdfKey.put("inner emptiness", "Depression");
        keywordToPdfKey.put("mood disorder", "Depression");
        // hopelessness is NOT repeated

        // Burnout
        keywordToPdfKey.put("burnout", "Burnout");
        keywordToPdfKey.put("burn out", "Burnout");
        keywordToPdfKey.put("exhaustion", "Burnout");
        keywordToPdfKey.put("chronic fatigue", "Burnout");
        keywordToPdfKey.put("loss of motivation", "Burnout");
        keywordToPdfKey.put("mental drain", "Burnout");
        keywordToPdfKey.put("work stress", "Burnout");
        keywordToPdfKey.put("emotional depletion", "Burnout");
        keywordToPdfKey.put("overcommitment", "Burnout");
        keywordToPdfKey.put("detachment", "Burnout");
        keywordToPdfKey.put("reduced performance", "Burnout");
        keywordToPdfKey.put("productivity crash", "Burnout");

        // Social Comparison
        keywordToPdfKey.put("social comparison", "Social Comparison");
        keywordToPdfKey.put("envy", "Social Comparison");
        keywordToPdfKey.put("feeling inferior", "Social Comparison");
        keywordToPdfKey.put("online validation", "Social Comparison");
        keywordToPdfKey.put("comparing lifestyles", "Social Comparison");
        keywordToPdfKey.put("self-worth issues", "Social Comparison");
        keywordToPdfKey.put("insecurity", "Social Comparison");
        keywordToPdfKey.put("social media pressure", "Social Comparison");
        keywordToPdfKey.put("unrealistic standards", "Social Comparison");
        keywordToPdfKey.put("peer comparison", "Social Comparison");
        keywordToPdfKey.put("low self-esteem", "Social Comparison");
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = update.getMessage().getChatId().toString();
                String userText = update.getMessage().getText().toLowerCase();

                boolean matched = false;
                for (Map.Entry<String, String> entry : keywordToPdfKey.entrySet()) {
                    if (userText.contains(entry.getKey())) {
                        sendPdfSummary(chatId, entry.getValue());
                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    sendOptions(chatId);
                }

            } else if (update.hasCallbackQuery()) {
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                String data = update.getCallbackQuery().getData();

                if (data.startsWith("FULL_")) {
                    String pdfName = data.substring(5);
                    sendPdfFull(chatId, pdfName);
                } else {
                    sendPdfSummary(chatId, data);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendOptions(String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Please select a Challenge:");

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String key : pdfMap.keySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(key);
            button.setCallbackData(key);
            buttons.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttons);
        message.setReplyMarkup(markup);

        execute(message);
    }

    private void sendPdfSummary(String chatId, String pdfName) throws TelegramApiException {
        String resourcePath = pdfMap.get(pdfName);
        if (resourcePath != null) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    sendMessage(chatId, "❌ Could not find PDF: " + pdfName);
                    return;
                }

                PDDocument document = PDDocument.load(inputStream);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document).trim();
                document.close();

                String summary = text.split("\\n\\s*\\n")[0]; // First paragraph
                if (summary.length() > 500) {
                    summary = summary.substring(0, 497) + "...";
                }

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("📄 *" + escapeMarkdown(pdfName) + "*\n\n" + escapeMarkdown(summary));
                message.setParseMode("MarkdownV2");

                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("Read Full Content");
                button.setCallbackData("FULL_" + pdfName);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(Collections.singletonList(Collections.singletonList(button)));

                message.setReplyMarkup(markup);
                execute(message);

            } catch (Exception e) {
                sendMessage(chatId, "❌ Error reading summary: " + e.getMessage());
            }
        }
    }

    private void sendPdfFull(String chatId, String pdfName) throws TelegramApiException {
        String resourcePath = pdfMap.get(pdfName);
        if (resourcePath != null) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    sendMessage(chatId, "❌ Could not find PDF: " + pdfName);
                    return;
                }

                PDDocument document = PDDocument.load(inputStream);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document).trim();
                document.close();

                String header = "📄 *" + escapeMarkdown(pdfName) + "*\n\n";
                int maxLength = 4096;

                // If whole message fits in one send
                if (header.length() + escapeMarkdown(text).length() <= maxLength) {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText(header + escapeMarkdown(text));
                    message.setParseMode("MarkdownV2");
                    execute(message);
                } else {
                    // Send header first
                    SendMessage headerMsg = new SendMessage();
                    headerMsg.setChatId(chatId);
                    headerMsg.setText(header);
                    headerMsg.setParseMode("MarkdownV2");
                    execute(headerMsg);

                    // Split *before* escaping to avoid broken escape sequences
                    List<String> parts = splitIntoChunksBeforeEscape(text, maxLength - 50);

                    // Send each chunk escaped separately
                    for (String part : parts) {
                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);
                        message.setText(escapeMarkdown(part));
                        message.setParseMode("MarkdownV2");
                        execute(message);
                    }
                }

            } catch (Exception e) {
                sendMessage(chatId, "❌ Error reading full PDF: " + e.getMessage());
            }
        }
    }

    // Split text by newlines or spaces before escaping
    private List<String> splitIntoChunksBeforeEscape(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());

            // Try to break at last newline or space before 'end' for clean split
            int lastNewLine = text.lastIndexOf('\n', end);
            int lastSpace = text.lastIndexOf(' ', end);
            int splitPos = Math.max(lastNewLine, lastSpace);

            if (splitPos > start) {
                end = splitPos + 1; // include the break char
            }

            String chunk = text.substring(start, end);
            chunks.add(chunk);
            start = end;
        }
        return chunks;
    }

    private String escapeMarkdown(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("_", "\\_")
            .replace("*", "\\*")
            .replace("[", "\\[")
            .replace("]", "\\]")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("~", "\\~")
            .replace("`", "\\`")
            .replace(">", "\\>")
            .replace("#", "\\#")
            .replace("+", "\\+")
            .replace("-", "\\-")
            .replace("=", "\\=")
            .replace("|", "\\|")
            .replace("{", "\\{")
            .replace("}", "\\}")
            .replace(".", "\\.")
            .replace("!", "\\!");
    }

    private void sendMessage(String chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(escapeMarkdown(text));
        message.setParseMode("MarkdownV2");
        execute(message);
    }

    @Override
    public String getBotUsername() {
        return "DAG2ILLbot"; // Replace with your BotFather username
    }

    @Override
    public String getBotToken() {
        return "7469872028:AAH2fj6CfUyIW7WGaoLm_XyhUiNRQ9DtV6w"; // Replace with your actual token
    }
}
