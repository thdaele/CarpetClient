package carpetclient.rules;

import carpetclient.Config;
import carpetclient.mixinInterface.AMixinTimer;
import carpetclient.mixins.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketByteBuf;

/**
 * Tick rate method to edit the clients tick rate changes based on the servers tick rate.
 */
public class TickRate {
    public static final float NORMAL_RATE = 20.0F;
    public static boolean runTickRate = false;

    /**
     * Setter tick rate in the config files for later setting the client to the tick rate.
     *
     * @param setTick Tick rate that is to be set on the client.
     */
    public static void setTickRate(float setTick) {
        Config.tickRate = setTick;
        setTickClient();
    }

    /**
     * Sets the game tick after the values are set.
     */
    public static void setTickClient() {
        runTickRate = Config.setTickRate.getValue() && (Config.tickRate != NORMAL_RATE);
        ((AMixinTimer) ((IMinecraft) Minecraft.getInstance()).getTimer()).carpetClient$setWorldTickRate(runTickRate ? Config.tickRate : NORMAL_RATE);
    }

    /**
     * A data packet handler for unpacking and setting the client tick rate.
     *
     * @param data Data from the server sent when tick rates are changed.
     */
    public static void setTickRate(PacketByteBuf data) {
        Config.tickRate = data.readFloat();
        setTickRate(Config.tickRate);
    }
}
