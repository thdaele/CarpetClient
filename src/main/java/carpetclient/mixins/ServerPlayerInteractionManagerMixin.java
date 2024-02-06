package carpetclient.mixins;

import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.ServerPlayerInteractionManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Mixins class to fix ghost block fix from mining
 */
@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow public World world;
    @Shadow public ServerPlayerEntity player;

    /*
    Injection to add block updates for the block that is being miss minsed fixing ghost block mining.
     */
    @Inject(method = "startMiningBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateBlockMiningProgress(ILnet/minecraft/util/math/BlockPos;I)V"))
    public void post(BlockPos pos, Direction side, CallbackInfo ci) {
        player.networkHandler.sendPacket(new BlockUpdateS2CPacket(world, pos));
    }
}
