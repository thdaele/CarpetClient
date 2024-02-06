package carpetclient;

import java.util.List;

import carpetclient.coders.EDDxample.PistonHelper;
import carpetclient.coders.EDDxample.ShowBoundingBoxes;
import carpetclient.coders.EDDxample.VillageMarker;
import carpetclient.gui.chunkgrid.Controller;
import carpetclient.gui.chunkgrid.GuiChunkGrid;
import carpetclient.random.RandomtickDisplay;
import carpetclient.pluginchannel.CarpetPluginChannel;
import carpetclient.rules.CarpetRules;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Window;
import net.minecraft.network.PacketByteBuf;

public class CarpetClient implements ClientModInitializer {

    private static boolean gameRunnin = false;
    private static boolean loggedOut = false;

    @Override
    public void initClient() {
        CarpetPluginChannel.init();
        Config.load();
        GuiChunkGrid.instance = new GuiChunkGrid();
        Hotkeys.init();

        MinecraftClientEvents.TICK_END.register(this::onTick);
    }

    private void onTick(Minecraft minecraft) {
        gameRunnin = minecraft.isIntegratedServerRunning() || minecraft.getCurrentServerEntry() != null;

        if (gameRunnin) {
            Hotkeys.onTick(minecraft);
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

    public static void onPostRenderEntities(float partialTicks) {
        if (gameRunnin) {
            MainRender.mainRender(partialTicks);
        }
    }

    public static void onPostRender(float partialTicks) {
        if (gameRunnin) {
            try {
                PistonHelper.draw(partialTicks);
                RandomtickDisplay.draw(partialTicks);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void onPostRenderHUD(int screenWidth, int screenHeight) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.renderMinimap(screenWidth, screenHeight);
        }
    }

    public static void onViewportResized(Window resolution, int displayWidth, int displayHeight) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.getController().updateMinimap();
        }
    }

    public static void onFullScreenToggled(boolean fullScreen) {
        if (GuiChunkGrid.instance.getMinimapType() != 0) {
            GuiChunkGrid.instance.getController().updateMinimap();
        }
    }
}
