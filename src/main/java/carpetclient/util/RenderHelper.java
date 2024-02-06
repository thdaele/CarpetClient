package carpetclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.TextRenderer;
import java.util.List;

public class RenderHelper {
    public static void drawGuiInfoBox(TextRenderer fontRenderer, String text, int startY, int infoBoxWidth, int guiWidth, int guiHeight, int bottomPadding) {
        if (text == null) {
            return;
        }

        int infoX = guiWidth / 2 - infoBoxWidth / 2;

        List<String> lines = wordWrap(text, infoBoxWidth - 10);
        int infoBoxHeight = lines.size() * fontRenderer.fontHeight + 10;

        int infoY = startY + infoBoxHeight < guiHeight - bottomPadding
                ? startY : guiHeight - infoBoxHeight - bottomPadding;

        int y = infoY + 5;

        GuiElement.fill(infoX, infoY, infoX + infoBoxWidth, infoY + infoBoxHeight, 0xCF000000);

        for (String s : lines) {
            fontRenderer.draw(s, infoX + 5, y, 0xFFFFFF);
            y += fontRenderer.fontHeight;
        }
    }

    public static List<String> wordWrap(String s, int width) {
        s = s.replace("\r", ""); // If we got a \r\n in the text somehow, remove it.

        List<String> lines = Minecraft.getInstance().textRenderer.split(s, width);

        return lines;
    }
}
