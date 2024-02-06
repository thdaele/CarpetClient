package carpetclient.mixins;

import carpetclient.Config;
import carpetclient.Hotkeys;
import net.minecraft.block.DiodeBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
Mixen class to make comperator properly rotate without visual glitches when doing "accurateBlockPlacement".
 */
@Mixin(DiodeBlock.class)
public abstract class DiodeBlockMixin extends HorizontalFacingBlock {

    protected DiodeBlockMixin(Material materialIn) {
        super(materialIn);
    }

    // Override to fix a client side visual affect when placing blocks in a different orientation.
    @Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
    public void placementState(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, CallbackInfoReturnable<BlockState> cir) {
        facing = placer.getHorizontalFacing().getOpposite();
        if (Config.accurateBlockPlacement && Hotkeys.isKeyDown(Hotkeys.toggleBlockFlip.getKeyCode())) {
            facing = facing.getOpposite();
        }

        cir.setReturnValue(this.defaultState().set(FACING, facing));
    }
}
