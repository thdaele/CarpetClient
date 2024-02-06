package carpetclient.mixins;


import carpetclient.Config;
import carpetclient.Hotkeys;
import com.google.common.collect.Maps;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.options.KeyBinding;

/*
A Mixins class to implement key lock when snap aim is turned on.

Multi vanilla keybinding stolen from nessie.
 */
@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

    @Unique
	private static final Map<Integer, List<KeyBindingMixin>> listOfBindings = Maps.newHashMap();
    @Shadow @Final
    private static Map<String, KeyBindingMixin> ALL;
    @Shadow
    private int clickCount;
    @Shadow
    private boolean pressed;
    @Shadow
    private int keyCode;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initKeyBinding(CallbackInfo callbackInfo) {
        List<KeyBindingMixin> bindingsOnKey = listOfBindings.computeIfAbsent(this.keyCode, k -> new ArrayList<>());
        bindingsOnKey.add(this);
    }

    /*
    Inject to create a return out of the funtion that detects key release. This will create the effect of keys being pressed but not get released.
     */
    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private static void setKeyBindStateInject(int keyCode, boolean pressed, CallbackInfo ci) {
        ci.cancel();
        if (keyCode != Hotkeys.toggleSnapAimKeyLocker.getKeyCode() && Hotkeys.toggleSnapAimKeyLocker.isPressed() && !pressed && Config.snapAim) {
            return;
        }

        if (keyCode != 0) {
            List<KeyBindingMixin> bindingsOnKey = listOfBindings.get(keyCode);
            if (bindingsOnKey != null) {
                for (KeyBindingMixin keyBinding : bindingsOnKey) {
                    keyBinding.pressed = pressed;
                }
            }
        }
    }

    @Inject(method = "resetMapping", at = @At("HEAD"), cancellable = true)
    private static void resetKeyBindingArrayAndHash(CallbackInfo ci) {
        ci.cancel();

        listOfBindings.clear();

        for (KeyBindingMixin keyBind : ALL.values()) {
            List<KeyBindingMixin> bindingsOnKey = listOfBindings.computeIfAbsent(keyBind.keyCode, k -> new ArrayList<>());
            bindingsOnKey.add(keyBind);
        }
    }

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void onTick(int keyCode, CallbackInfo ci) {
        ci.cancel();

        if (keyCode != 0) {
            List<KeyBindingMixin> bindingsOnKey = listOfBindings.get(keyCode);

            if (bindingsOnKey != null) {
                for (KeyBindingMixin keyBinding : bindingsOnKey) {
                    keyBinding.clickCount++;
                }
            }
        }
    }
}
