package carpetclient;

import java.util.List;

import carpetclient.coders.EDDxample.PistonHelper;
import carpetclient.coders.EDDxample.ShowBoundingBoxes;
import carpetclient.coders.EDDxample.VillageMarker;
import carpetclient.gui.chunkgrid.Controller;
import carpetclient.gui.chunkgrid.GuiChunkGrid;
import carpetclient.random.RandomtickDisplay;
import com.mumfrey.liteloader.*;
import carpetclient.pluginchannel.CarpetPluginChannel;
import carpetclient.rules.CarpetRules;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Window;
import net.minecraft.network.PacketByteBuf;

public class CarpetClient implements ClientModInitializer {

    private boolean gameRunnin = false;
    private boolean loggedOut = false;

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

	@Override
	public void initClient() {
		Config.load();
		GuiChunkGrid.instance = new GuiChunkGrid();
		Hotkeys.init();
	}

    @Override
    public String getName() {
        return "Carpet Client";
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        gameRunnin = minecraft.isIntegratedServerRunning() || minecraft.getCurrentServerEntry() != null;

        if (gameRunnin) {
            Hotkeys.onTick(minecraft, partialTicks, inGame, clock);
            Controller.tick();
            loggedOut = true;
        } else if (loggedOut) {
            loggedOut = false;
            CarpetRules.resetToDefaults();
            Config.resetToDefaults();
            VillageMarker.clearLists(0);
            ShowBoundingBoxes.clear();
            GuiChunkGrid.instance = new GuiChunkGrid();
        }
    }

    // Needed method for plugin channels. Data from the server.
    @Override
    public void onCustomPayload(String channel, PacketByteBuf data) {
        CarpetPluginChannel.packatReceiver(channel, data);
    }

    // Needed method for plugin channels. Adds the list of channels that the client will listen for.
    @Override
    public List<String> getChannels() {
        return CarpetPluginChannel.CARPET_PLUGIN_CHANNEL;
    }

    @Override
    public void onPostRenderEntities(float partialTicks) {
        if (gameRunnin) {
            MainRender.mainRender(partialTicks);
        }
    }

    @Override
    public void onPostRender(float partialTicks) {
        if (gameRunnin) {
            try {
                PistonHelper.draw(partialTicks);
                RandomtickDisplay.draw(partialTicks);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void onPreRenderHUD(int screenWidth, int screenHeight) {
    }

    @Override
    public void onPostRenderHUD(int screenWidth, int screenHeight) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.renderMinimap(screenWidth, screenHeight);
        }
    }

    @Override
    public void onViewportResized(Window resolution, int displayWidth, int displayHeight) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.getController().updateMinimap();
        }
    }

    @Override
    public void onFullScreenToggled(boolean fullScreen) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.getController().updateMinimap();
        }
    }
}
