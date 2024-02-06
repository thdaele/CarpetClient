package carpetclient.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {

    @Unique
	private int counter = 0;

    @Unique
	public void updateScreen()
    {
        ++counter;
        if (counter % 20 == 0)
        {
            Minecraft.getInstance().getNetworkHandler().sendPacket(new KeepAliveC2SPacket());
        }
    }
}
