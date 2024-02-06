package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item {

    @Inject(method = "getMiningSpeed", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        Block block = state.getBlock();

        if (block != Blocks.WEB && state.getMaterial() != Material.LEAVES && (!Config.missingTools || state.getMaterial() != Material.SPONGE)) {
            cir.setReturnValue(block == Blocks.WOOL ? 5.0F : super.getMiningSpeed(stack, state));
        } else {
            cir.setReturnValue(15.0F);
        }
    }
}
