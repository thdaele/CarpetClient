package carpetclient.mixins;

import carpetclient.Config;
import carpetclient.Hotkeys;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ObserverBlock;
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
Mixen class to make observers properly rotate without visual glitches when doing "accurateBlockPlacement".
 */
@Mixin(ObserverBlock.class)
public class ObserverBlockMixin extends FacingBlock {

    protected ObserverBlockMixin(Material materialIn) {
        super(materialIn);
    }

    // Override for placing blocks in the correct orientation when using accurate block placement
    @Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, CallbackInfoReturnable<BlockState> cir) {
        if (Config.accurateBlockPlacement) {
            if (!Hotkeys.isKeyDown(Hotkeys.toggleBlockFacing.getKeyCode())) {
                facing = Direction.nearest(pos, placer).getOpposite();
            }
            if (Hotkeys.isKeyDown(Hotkeys.toggleBlockFlip.getKeyCode())) {
                facing = facing.getOpposite();
            }
        } else {
            facing = Direction.nearest(pos, placer).getOpposite();
        }
        cir.setReturnValue(this.defaultState().set(FACING, facing));
    }
}
