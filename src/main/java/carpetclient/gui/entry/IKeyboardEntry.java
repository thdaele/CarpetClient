package carpetclient.gui.entry;

import net.minecraft.client.gui.widget.EntryListWidget;

public interface IKeyboardEntry extends EntryListWidget.Entry {
    void keyDown(char typedChar, int keyCode);

    void onUpdate();
}