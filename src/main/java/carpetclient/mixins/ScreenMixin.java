package carpetclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.URI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

/**
 * Class to help open screenshots
 */
@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow
	private void openLink(URI url) { }

    /*
    Method made to help open screenshot folders instead of the image itself when clicking on it in the text area.
     */
    @Inject(method = "handleClickEvent", at = @At("HEAD"), cancellable = true)
    public void onItemRightClick(Text component, CallbackInfoReturnable<Boolean> cir) {
        if(component != null && Screen.isShiftDown())
        {
            ClickEvent clickevent = component.getStyle().getClickEvent();
            if(clickevent != null && clickevent.getAction() == ClickEvent.Action.OPEN_FILE){
                this.openLink((new File(clickevent.getValue())).getParentFile().toURI());
                cir.setReturnValue(true);
            }
        }
    }
}
