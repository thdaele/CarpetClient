package carpetclient.coders.Pokechu22;


import carpetclient.gui.entry.BaseEntry;
import carpetclient.gui.entry.IKeyboardEntry;
import carpetclient.gui.entry.ITooltipEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.EntryListWidget;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiConfigList extends EntryListWidget {
    private final List<Entry> entries = new ArrayList<>();

    public GuiConfigList(Minecraft mcIn, int slotHeightIn) {
        super(mcIn, 0, 0, 0, 0, slotHeightIn);
    }

    public int getSelectedElement() { return pos; }

    public abstract void initGui();

    public void onClose()
    {
    }

    public <T> BaseEntry<T> addEntry(BaseEntry<T> entry) {
        this.entries.add(entry);
        return entry;
    }

    @Override
    public int getRowWidth() {
        return 180 * 2;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + getRowWidth() / 2 + 4;
    }

    @Override
    public Entry getEntry(int index) {
        return entries.get(index);
    }

    @Override
    protected int size() {
        return entries.size();
    }

    public void update() {
        // Use a manual for loop to avoid concurrent modification exceptions
        for (int i = 0; i < size(); i++) {
            Entry entry = getEntry(i);
            if (entry instanceof IKeyboardEntry) {
                ((IKeyboardEntry) entry).onUpdate();
            }
        }
    }

    public void keyDown(char typedChar, int keyCode) {
        // Use a manual for loop to avoid concurrent modification exceptions
        for (int i = 0; i < size(); i++) {
            Entry entry = getEntry(i);
            if (entry instanceof IKeyboardEntry) {
                ((IKeyboardEntry) entry).keyDown(typedChar, keyCode);
            }
        }
    }

    public void drawTooltip(int mouseX, int mouseY, float partialTicks) {
        int insideLeft = this.minX + this.width / 2 - this.getRowWidth() / 2 + 2;
        int insideTop = this.minY + 4 - (int)this.scrollAmount;
        int l = this.entryHeight - 4;

        for (int i = 0; i < this.size(); i++) {
            int k = insideTop + i * this.entryHeight + this.headerHeight;

            Entry entry = getEntry(i);
            if (entry instanceof ITooltipEntry) {
                ((ITooltipEntry) entry).drawTooltip(i, insideLeft, k, mouseX, mouseY, this.getRowWidth(), this.height, this.width, l, partialTicks);
            }
        }
    }

    @Override
    protected void entryClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        if (getSelectedElement() != -1 && getSelectedElement() != slotIndex)
        {
            ((BaseEntry) this.getEntry(getSelectedElement())).testFocused(mouseX, mouseY);
        }

        if (slotIndex != -1)
        {
            ((BaseEntry) this.getEntry(slotIndex)).testFocused(mouseX, mouseY);
        }
    }
}
