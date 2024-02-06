package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
Mixen class to implement relaxed block placement, placement of pumpkins in mid air.
 */
@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin extends HorizontalFacingBlock {

    protected PumpkinBlockMixin(Material materialIn) {
        super(materialIn);
    }

    // Override to place pumpkins mid air
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(World worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((worldIn.getBlockState(pos.down()).getMaterial().isSolid() || Config.relaxedBlockPlacement) && super.canSurvive(worldIn, pos));
    }
}
