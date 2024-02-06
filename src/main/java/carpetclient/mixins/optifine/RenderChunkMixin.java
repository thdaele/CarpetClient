package carpetclient.mixins.optifine;

import carpetclient.Config;
import net.minecraft.client.render.world.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderChunk.class)
public class RenderChunkMixin {

    /**
     * Optifine Shenanegans that causes blinky clippy pistons. This helps remedy the issue.
     * @param ci
     */
    @Inject(method = "isDirtyFromPlayer", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void optifineDisablePlayerUpdatesBoolean(CallbackInfoReturnable<Boolean> ci) {
        if (!Config.clipThroughPistons.getValue()) return;
        ci.setReturnValue(true);
    }
}
