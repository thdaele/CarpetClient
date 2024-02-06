package carpetclient.gui.entry;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseEntry<T> implements EntryListWidget.Entry {
    @Nullable
    protected String title;

    private List<Consumer<T>> actions;

    public BaseEntry(@Nullable String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    @Override
    public void render(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        if (this.title != null) {
            Minecraft.getInstance().textRenderer.draw(this.title, x, y + 6, 0xFFFFFFFF);
        }
        this.draw(x, y, listWidth, slotHeight, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        return mouseDown(mouseX, mouseY, mouseEvent);
    }

    @Override
    public void mouseReleased(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        mouseUp(mouseX, mouseY, mouseEvent);
    }

    @Override
    public void renderOutOfBounds(int slotIndex, int x, int y, float partialTicks) {
    }

    public void testFocused(int mouseX, int mouseY) {
        this.onFocusChanged(mouseX, mouseY);
    }

    protected boolean onFocusChanged(int mouseX, int mouseY) { return false; }

    public T onAction(Consumer<T> action) {
        if (actions == null)
            actions = new ArrayList<>();

        actions.add(action);
        return (T)this;
    }

    protected abstract void draw(int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, float partialTicks);

    protected boolean mouseDown(int x, int y, int button) { return false; }

    protected void mouseUp(int x, int y, int button) {}

    @SuppressWarnings("unchecked")
    protected void performAction() {
        if (actions != null)
            actions.forEach((r) -> r.accept((T)this));
    }
}
