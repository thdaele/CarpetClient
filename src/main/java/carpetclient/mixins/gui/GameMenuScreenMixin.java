package carpetclient.mixins.gui;

import carpetclient.gui.ConfigGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/*
Mixins override to add the main access button for Carpet Options menu.
 */
@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    @Unique
	private static final int carpetClientID = 6000;
    @Unique
	ButtonWidget carpetButton;

    @Inject(method = "init", at = @At("RETURN"))
    private void onInitGui(CallbackInfo ci) {
        injectButtons((GameMenuScreen) (Object) this, buttons);
    }

    /*
     * Action handler for the button click.
     */
    @Inject(method = "buttonClicked", at = @At("HEAD"))
    private void onActionPerformed(ButtonWidget guibutton, CallbackInfo ci) {
        handleButtonClick((GameMenuScreen) (Object) this, guibutton);
    }

    /*
     * Inserting the button handler when Carpet Button is clicked.
     */
    @Unique
	private void handleButtonClick(GameMenuScreen guiIngameMenu, ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }

        if (guibutton.id == carpetClientID) {
            if (Minecraft.getInstance().isIntegratedServerRunning()) {
                return; // not available if in singleplayer or LAN server mode
            }

            Minecraft.getInstance().openScreen(new ConfigGUI(guiIngameMenu));
        }
    }

    /*
     * Inserting the Button to access carpet menu.
     */
    @Unique
	private void injectButtons(GameMenuScreen gui, List buttonList) {
        int insertAtYPos = 0;

        for (Object obj : buttonList) {
            ButtonWidget btn = (ButtonWidget) obj;

            if (btn.id == 5) { // Button "Achievements"
                insertAtYPos = btn.y + 24;
                break;
            }
        }

        // Move other buttons down one slot (= 24 height units)
        for (Object obj : buttonList) {
            ButtonWidget btn = (ButtonWidget) obj;

            if (btn.y >= insertAtYPos) {
                btn.y += 24;
            }
        }

        // Insert carpet button in main window of escape menu.
        carpetButton = new ButtonWidget(carpetClientID, gui.width / 2 - 100, insertAtYPos, 200, 20, "Carpet Client");
        carpetButton.active = !Minecraft.getInstance().isIntegratedServerRunning();
        buttonList.add(carpetButton);
    }
}
