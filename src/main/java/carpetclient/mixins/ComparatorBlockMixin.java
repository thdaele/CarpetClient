package carpetclient.mixins;

import carpetclient.Config;
import carpetclient.Hotkeys;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.DiodeBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.BooleanProperty;
import net.minecraft.block.state.property.EnumProperty;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
Mixen class to make comperator properly rotate without visual glitches when doing "accurateBlockPlacement".
 */
@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin extends DiodeBlock {

    @Shadow
    public static @Final
    BooleanProperty POWERED;
    @Shadow
    public static @Final
    EnumProperty<ComparatorBlock.Mode> MODE;

    protected ComparatorBlockMixin(boolean powered) {
        super(powered);
    }

    // Override for placing blocks in the correct orientation when using accurate block placement
    @Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, CallbackInfoReturnable<BlockState> cir) {
        facing = placer.getHorizontalFacing().getOpposite();
        if (Config.accurateBlockPlacement && Hotkeys.isKeyDown(Hotkeys.toggleBlockFlip.getKeyCode())) {
            facing = facing.getOpposite();
        }

        cir.setReturnValue(this.defaultState().set(FACING, facing).set(POWERED, Boolean.FALSE).set(MODE, ComparatorBlock.Mode.COMPARE));
    }
}
