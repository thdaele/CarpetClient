package carpetclient.gui.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

public class TextEntry extends StandardRowEntry<TextEntry> implements IKeyboardEntry {
    private static final int TEXTFIELD_HEIGHT = 20;

    private TextFieldWidget field;

    public TextEntry(String title, String text, boolean reset) {
        super(title, false, reset, null);

        initEntrys(text);
    }

    public TextEntry(String title, String text, boolean reset, @NotNull String infoStr) {
        super(title, true, reset, infoStr);

        initEntrys(text);
    }

    private void initEntrys(String text) {
        field = new TextFieldWidget(0, Minecraft.getInstance().textRenderer, 0, 0, 100, 20);
        field.setText(text);
    }

    public TextFieldWidget getTextField() { return field; }

    @Override
    public void draw(int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, float partialTicks) {
        field.x = x + listWidth / 2;
        field.y = y;
        field.render();
    }

    @Override
    protected boolean mouseDown(int x, int y, int button) {
        field.mouseClicked(x, y, button);
        return false;
    }

    @Override
    protected void mouseUp(int x, int y, int button) {
    }

    @Override
    public void onUpdate() {
        this.field.tick();
    }

    @Override
    public void keyDown(char typedChar, int keyCode) {
//                if (field.isFocused()) System.out.println("type " + keyCode + " keytypechar " + typedChar);
        if (this.field.keyPressed(typedChar, keyCode)) {
//                    setRule(title, Float.toString(this.field.getValue()));
        } else if (keyCode == Keyboard.KEY_RETURN) {
            if (field.isFocused())
                this.performAction();

            field.setFocused(false);
        }
    }

    @Override
    protected boolean onFocusChanged(int mouseX, int mouseY)
    {
        if (mouseX >= this.field.x && mouseX <= this.field.x + this.field.getInnerWidth())
        {
            if (mouseY >= this.field.y && mouseY <= this.field.y + TEXTFIELD_HEIGHT)
            {
                this.getTextField().setFocused(true);
                return true;
            }
        }

        this.getTextField().setFocused(false);
        return super.onFocusChanged(mouseX, mouseY);
    }

        /*protected void performTextAction() {
//        System.out.println("text clicked " + title + " " + text);
            CarpetRules.textRuleChange(title, field.getText());
        }*/

        /*@Override
        protected void performResetAction() {
//        System.out.println("reset clicked " + title);
            CarpetRules.resetRule(title);
        }*/

        /*@Override
        protected void performInfoAction() {
//        System.out.println("info clicked " + title);
            CarpetRules.ruleTipRequest(title);
        }*/
}

