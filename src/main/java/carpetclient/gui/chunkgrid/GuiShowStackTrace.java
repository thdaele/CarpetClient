package carpetclient.gui.chunkgrid;

import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Formatting;

/**
 * Window for drawing stack traces.
 */
public class GuiShowStackTrace extends GuiSubWindow {

    private static final Pattern STACK_TRACE_ELEMENT_REGEX = Pattern.compile("(?:.+\\.)?(.+?\\..+?)\\(.+?(?::(\\d+))?\\)");

    private ButtonWidget doneButton;
    private ButtonWidget copyToClipboardButton;

    private List<String> stackTrace;
    int scrollIndex = 0;

    public GuiShowStackTrace(Screen parentScreen, Screen backgroundScreen, List<String> stackTrace) {
        super("Stack Trace", parentScreen, backgroundScreen, stackTrace);
        this.stackTrace = stackTrace;
    }

    @Override
    public void init() {
        super.init();

        addButton(doneButton = new ButtonWidget(0, 0, 0, I18n.translate("gui.done")));
        addButton(copyToClipboardButton = new ButtonWidget(1, 0, 0, "Copy to Clipboard"));
        layoutButtons(doneButton, copyToClipboardButton);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        switch (button.id) {
            case 0:
                minecraft.openScreen(parentScreen);
                break;
            case 1:
                setClipboard(stackTrace.stream().collect(Collectors.joining("\n")));
                break;
            default:
                super.buttonClicked(button);
        }
    }

    @Override
    public void handleMouse() {
        super.handleMouse();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            scroll *= -1;
            scroll /= 40;
            int lineHeight = textRenderer.fontHeight + 1;
            int y = getSubWindowTop() + 17;
            int maxY = getSubWindowBottom() - getFooterHeight() - 2;
            int maxLineCount = (maxY - y) / lineHeight;

            scrollIndex += scroll;
            if ((stackTrace.size() - maxLineCount) < scrollIndex) {
                scrollIndex = stackTrace.size() - maxLineCount;
            }
            if (scrollIndex < 0) {
                scrollIndex = 0;
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        textRenderer.setUnicode(true);

        int lineHeight = textRenderer.fontHeight + 1;
        int y = getSubWindowTop() + 17;
        int maxY = getSubWindowBottom() - getFooterHeight() - 2;
        int maxLineCount = (maxY - y) / lineHeight;

        for (int lineInd = scrollIndex; lineInd < stackTrace.size() && lineInd < scrollIndex + maxLineCount; lineInd++) {
            String line;
            if ((lineInd == scrollIndex + maxLineCount - 1 && lineInd != stackTrace.size() - 1) || (scrollIndex == lineInd && scrollIndex != 0)) {
                line = "...";
            } else {
                line = stackTrace.get(lineInd);
                Matcher matcher = STACK_TRACE_ELEMENT_REGEX.matcher(line);
                if (matcher.matches()) {
                    line = matcher.group(1);
                    String lineNo = matcher.group(2);
                    if (lineNo != null)
                        line += " : L" + lineNo;
                }
            }
            if (textRenderer.getWidth(Formatting.BOLD + line) > getSubWindowRight() - getSubWindowLeft() - 10) {
                while (textRenderer.getWidth(Formatting.BOLD + "..." + line) > getSubWindowRight() - getSubWindowLeft() - 10)
                    line = line.substring(1);
                line = "..." + line;
            }
            line = Formatting.BOLD + line;
            textRenderer.draw(line, getSubWindowLeft() + 5, y, 0);
            y += lineHeight;
        }

        textRenderer.setUnicode(minecraft.getLanguageManager().isUnicode() || minecraft.options.forceUnicodeFont);
    }
}
