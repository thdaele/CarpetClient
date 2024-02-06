package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(method = "setBlockStateFromPacket", at = @At("HEAD"), cancellable = true)
    private void fixPistonBlinking(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(Config.clipThroughPistons.getValue() && state.getBlock() == Blocks.MOVING_BLOCK) {
            cir.setReturnValue(true);
        }
    }
}
