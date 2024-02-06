package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShulkerBoxItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin class to allow empty shulkerboxes being stackable in the player inventory.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Shadow
    public Item getItem() {
        return null;
    }

    @Shadow
    public boolean hasNbt() {
        return false;
    }

    /*
     * Method to allow empty shulkerboxes stack in the players inventory.
     */
    @Inject(method = "getMaxSize", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(CallbackInfoReturnable<Integer> cir) {
        if (Config.stackableShulkersPlayerInventory && !hasNbt() && this.getItem() instanceof ShulkerBoxItem)
            cir.setReturnValue(64);
    }
}
