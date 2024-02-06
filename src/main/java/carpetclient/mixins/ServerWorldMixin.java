package carpetclient.mixins;

import carpetclient.util.IWorldServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements IWorldServer
{
    @Unique
	private boolean blockActionsProcessed;

    @Inject(method = "tick", at = @At("HEAD"))
    private void resetBlockActionsProcessed(CallbackInfo ci)
    {
        this.blockActionsProcessed = false;
    }

    @Inject(method = "doBlockEvents", at = @At("RETURN"))
    private void setBlockActionsProcessed(CallbackInfo ci)
    {
        this.blockActionsProcessed = true;
    }

    @Override
    public boolean carpetClient$haveBlockActionsProcessed()
    {
        return this.blockActionsProcessed;
    }
}
