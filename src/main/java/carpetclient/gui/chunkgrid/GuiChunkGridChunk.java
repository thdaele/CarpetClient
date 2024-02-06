package carpetclient.gui.chunkgrid;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

/**
 * Display window for the events.
 */
public class GuiChunkGridChunk extends GuiSubWindow {

    private ButtonWidget doneButton;
    private ButtonWidget showStackTraceButton;

    private List<String> properties;
    private List<String> stackTrace;

    public GuiChunkGridChunk(String header, Screen parentScreen, Screen backgroundScreen, List<String> properties, List<String> stackTrace) {
        super(header, parentScreen, backgroundScreen, stackTrace);
        this.properties = properties;
        this.stackTrace = stackTrace;
    }

    @Override
    public void init() {
        super.init();

        addButton(doneButton = new ButtonWidget(0, 0, 0, I18n.translate("gui.done")));
        addButton(showStackTraceButton = new ButtonWidget(1, 0, 0, "Show Stack Trace"));
        showStackTraceButton.active = stackTrace != null;
        layoutButtons(doneButton, showStackTraceButton);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        switch (button.id) {
            case 0:
                minecraft.openScreen(parentScreen);
                break;
            case 1:
                minecraft.openScreen(new GuiShowStackTrace(this, backgroundScreen, stackTrace));
                break;
            default:
                super.buttonClicked(button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        int x = (getSubWindowLeft() + getSubWindowRight()) / 2;
        int y = getSubWindowTop() + 25;

        for (String prop : properties) {
            textRenderer.draw(prop, x - textRenderer.getWidth(prop) / 2, y, 0);
            y += textRenderer.fontHeight + 2;
        }
    }
}
