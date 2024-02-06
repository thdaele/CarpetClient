package carpetclient.transformers;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TickTimer;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.sound.music.MusicManager;
import net.minecraft.client.sound.system.SoundManager;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Provide necessary obfuscation mapping at run time
 */

@Mixin(Minecraft.class)
@SuppressWarnings("unused")
public abstract class MinecraftObf {
    private static Minecraft __TARGET;

    private void runTickObf() throws Exception {
        __TARGET.tick();
    }

    @Shadow
    private @Final TickTimer timer;
    private Object timerObf() {
        return this.timer;
    }

    @Shadow
    private int itemUseDelay;
    private int rightClickDelayTimerObf() {
        return this.itemUseDelay;
    }

    private Object ingameGUIObf() {
        return __TARGET.gui;
    }

    private Object playerControllerObf() {
        return __TARGET.interactionManager;
    }

    @Shadow
    private TextureManager textureManager;
    private Object renderEngineObf() {
        return this.textureManager;
    }

    private void displayGuiScreenObf() {
        __TARGET.openScreen(null);
    }

    @Shadow
    private int attackCooldown;
    private int leftClickCounterObf() {
        return this.attackCooldown;
    }

    private Object currentScreenObf() {
        return __TARGET.screen;
    }

    @Shadow
    private void handleMouseEvents() throws IOException {};
    private void runTickMouseObf() throws IOException {
        this.handleMouseEvents();
    }

    @Shadow
    private void handleKeyboardEvents() throws IOException {};
    private void runTickKeyboardObf() throws IOException {
        this.handleKeyboardEvents();
    }

    @Shadow
    private int joinPlayerCounter;
    private int joinPlayerCounterObf() {
        return this.joinPlayerCounter;
    }

    private Object worldObf() {
        return __TARGET.world;
    }

    private Object renderGlobalObf() {
        return __TARGET.worldRenderer;
    }

    @Shadow
    private MusicManager musicManager;
    private Object musicTickerObf() {
        return this.musicManager;
    }

    @Shadow
    private SoundManager soundManager;
    private Object soundHandlerObf() {
        return this.soundManager;
    }

    private Object effectRendererObf() {
        return __TARGET.particleManager;
    }

    @Shadow
    private Connection connection;
    private Object networkManagerObf() {
        return this.connection;
    }
}
