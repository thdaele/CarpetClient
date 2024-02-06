package carpetclient.mixins;

import carpetclient.Config;
import carpetclient.Hotkeys;
import carpetclient.coders.skyrising.PacketSplitter;
import com.mumfrey.liteloader.core.PluginChannels;
import io.netty.buffer.Unpooled;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.crafting.recipe.Recipe;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.menu.ActionType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerHandActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerUseBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Injecting code for block rotation. Editing the x value when sending the package "CPacketPlayerTryUseItemOnBlock" to be decoded by carpet.
 */

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private GameMode gameMode;
    @Shadow
    private @Final
    ClientPlayNetworkHandler networkHandler;

    @Shadow
    private void updateSelectedHotbarSlot() {
    }

    /**
     * Totally crazy problem solved by redirect. omg!
     * Changed this value to use accurate block placement to rotate blocks.
     *
     * @param connection
     * @param packetIn
     * @param player
     * @param worldIn
     * @param pos
     * @param direction
     * @param vec
     * @param hand
     */
    @Redirect(method = "useBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/handler/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"))
    public void sendPacketReplace(ClientPlayNetworkHandler connection,
                                  Packet<?> packetIn, // sendPacket vars
                                  LocalClientPlayerEntity player, ClientWorld worldIn, BlockPos pos, Direction direction, Vec3d vec, InteractionHand hand // processRightClickBlock vars
    ) {
        float f = (float) (vec.x - (double) pos.getX());
        float f1 = (float) (vec.y - (double) pos.getY());
        float f2 = (float) (vec.z - (double) pos.getZ());
        ItemStack item = player.getHandStack(hand);
        if (Config.accurateBlockPlacement) f = blockRotation(player, pos, f, direction, item);
        connection.sendPacket(new PlayerUseBlockC2SPacket(pos, direction, hand, f, f1, f2));
    }

    /**
     * Checks for the item types that should be accurate placed, skipps everything else. if f value is above 1 then the protocal is already being used and also returns false to skip rotation.
     */
    private boolean rotationType(float f, ItemStack is) {
        if (f > 1) {
            return false;
        }

        if (isPiston(is)) {
            return true;
        } else if (isObserver(is)) {
            return true;
        } else if (isDiode(is)) {
            return true;
        }else if(isDispenser(is)){
            return true;
        }else if(isGlazedTerracotta(is)){
            return true;
        }

        return false;
    }


//    @ModifyVariable(method = "processRightClickBlock", ordinal = 0, at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;connection:Lnet/minecraft/client/network/NetHandlerPlayClient;"))
    /**
     * A rotation alogrithm that will sneek data in the unused x value. Data will be decoded by carpet
     * mod "accurateBlockPlacement" and place the block in the orientation that is coded.
     *
     * @param player    The player that is plasing the block.
     * @param pos       Position of the Block being placed
     * @param f         old X value that is used unused currently in vanilla minecraft. Y value is used to place blocks on the top of bottom part (stairs/slabs).
     * @param direction The direction of the block being placed into. Rather the facing side the player is clicking on.
     * @param itemstack The item stack or item that is being placed.
     * @return value that is coded for specific orientation that is determined by the players choices.
     */
    private float blockRotation(LocalClientPlayerEntity player, BlockPos pos, float f, Direction direction, ItemStack itemstack) {
        if (!rotationType(f, itemstack)) return f;

        if (Hotkeys.isKeyDown(Hotkeys.toggleBlockFacing.getKeyCode())) {
            // rotate pistons for placing head into blocks
            if (isPiston(itemstack)) {
                direction = direction.getOpposite();
            }

            float i = Hotkeys.isKeyDown(Hotkeys.toggleBlockFlip.getKeyCode()) ? direction.getOpposite().getId() : direction.getId();
            return i + 2;
        } else {
            Direction facing = null;

            // different type of placement for diodes
            if (isDiode(itemstack) || isGlazedTerracotta(itemstack)) {
                facing = player.getHorizontalFacing().getOpposite();
            } else {
                facing = Direction.nearest(pos.offset(direction), player);
            }

            // rotate for observers
            if (isObserver(itemstack)) {
                facing = facing.getOpposite();
            }

//            float i = GuiScreen.isAltKeyDown() ? facing.getOpposite().getIndex() : facing.getIndex();
            float i = Hotkeys.isKeyDown(Hotkeys.toggleBlockFlip.getKeyCode()) ? facing.getOpposite().getId() : facing.getId();
            return i + 2;
        }
    }

    /**
     * Checks to see if item is a repeater/comperator that is being placed.
     *
     * @param itemstack The stack that is being placed.
     * @return true if item is repeater/comperator that is being placed or not.
     */
    private boolean isDiode(ItemStack itemstack) {
        int id = Item.getId(itemstack.getItem());
        return id == 356 || id == 404;
    }

    /**
     * Checks to see if item is a piston/sticky-piston that is being placed.
     *
     * @param itemstack The stack that is being placed.
     * @return true if item is piston/sticky-piston that is being placed or not.
     */
    private boolean isPiston(ItemStack itemstack) {
        int id = Item.getId(itemstack.getItem());
        return id == 29 || id == 33;
    }

    /**
     * Checks to see if item is a observer that is being placed.
     *
     * @param itemstack The stack that is being placed.
     * @return true if item is observer that is being placed or not.
     */
    public boolean isObserver(ItemStack itemstack) {
        int id = Item.getId(itemstack.getItem());
        return id == 218;
    }

    /**
     * Checks to see if its dispenser or dropper.
     *
     * @param itemstack The item that is to be checked if it should be allowed to get player rotated.
     * @return Returns if the item type is allowed or not to be rotated.
     */
    private boolean isDispenser(ItemStack itemstack) {
        int id = Item.getId(itemstack.getItem());
        return id == 23 || id == 158;
    }

    /**
     * Checks to see if its any kind of glazed terracotta.
     *
     * @param itemstack The item that is to be checked if it should be allowed to get player rotated.
     * @return Returns if the item type is allowed or not to be rotated.
     */
    private boolean isGlazedTerracotta(ItemStack itemstack){
        int id = Item.getId(itemstack.getItem());
        return id >= 235 && id <= 250;
    }

    @Inject(method = "placeRecipe", at = @At("RETURN"))
    private void onSendPlaceRecipe(int window, Recipe recipe, boolean makeAll, PlayerEntity player, CallbackInfo ci) {
        if(!Config.fastCrafting.getValue()) return;
        if (Screen.isShiftDown() && Screen.isAltDown() ){
            this.minecraft.interactionManager.clickSlot(window, 0, 1, ActionType.QUICK_MOVE, this.minecraft.player);
        } else if(Screen.isShiftDown() && Screen.isControlDown() && Config.controlQCrafting) {
            this.minecraft.interactionManager.clickSlot(window, 0, 1, ActionType.THROW, this.minecraft.player);
        }
    }

    // CrispyLumps was here

    /**
     * Fixes the mining packets for carpet client users to add careful break and remove blocks shortly reappearing when mining slower then instant mine.
     */
    @Redirect(method = "startMiningBlock", at = @At(value = "INVOKE", target = "net/minecraft/client/network/handler/ClientPlayNetworkHandler.sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 2))
    public void miningPacket(ClientPlayNetworkHandler connection,
                                  Packet<?> packetIn, // sendPacket vars
                                    BlockPos loc, Direction face // processRightClickBlock vars
    ) {
        if(!Config.betterMiner) {
            connection.sendPacket(new PlayerHandActionC2SPacket(PlayerHandActionC2SPacket.Action.START_DESTROY_BLOCK, loc, face));
            return;
        }
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        BlockState iblockstate = this.minecraft.world.getBlockState(loc);
        boolean instaMine = iblockstate.getMiningSpeed(this.minecraft.player, this.minecraft.player.world, loc) >= 1.0F;
        data.writeBoolean(true);
        data.writeBlockPos(loc);
        data.writeByte(face.getId());
        data.writeBoolean(instaMine);
        data.writeBoolean(Config.carefulBreak.getValue());

        PacketSplitter.send("carpet:mine", data, PluginChannels.ChannelPolicy.DISPATCH_ALWAYS);
    }

    /**
     * Fixes the mining packets for carpet client users to add careful break and remove blocks shortly reappearing when mining slower then instant mine.
     */
    @Redirect(method = "updateBlockMining", at = @At(value = "INVOKE", target = "net/minecraft/client/network/handler/ClientPlayNetworkHandler.sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 1))
    public void miningPacketEnd(ClientPlayNetworkHandler connection,
                             Packet<?> packetIn, // sendPacket vars
                             BlockPos loc, Direction face // processRightClickBlock vars
    ) {
        if(!Config.betterMiner) {
            connection.sendPacket(new PlayerHandActionC2SPacket(PlayerHandActionC2SPacket.Action.STOP_DESTROY_BLOCK, loc, face));
            return;
        }

        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(false);
        data.writeBlockPos(loc);
        data.writeByte(face.getId());
        data.writeBoolean(true);
        data.writeBoolean(Config.carefulBreak.getValue());

        PacketSplitter.send("carpet:mine", data, PluginChannels.ChannelPolicy.DISPATCH_ALWAYS);
    }

}
