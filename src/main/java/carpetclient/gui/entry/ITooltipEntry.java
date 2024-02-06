package carpetclient.gui.entry;

import net.minecraft.client.gui.widget.EntryListWidget;

public interface ITooltipEntry extends EntryListWidget.Entry {
    void drawTooltip(int slotIndex, int x, int y, int mouseX, int mouseY, int listWidth, int listHeight, int slotWidth, int slotHeight,  float partialTicks);
}
