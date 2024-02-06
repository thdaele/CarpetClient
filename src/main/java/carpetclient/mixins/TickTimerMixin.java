package carpetclient.mixins;

import carpetclient.mixinInterface.AMixinTimer;
import carpetclient.rules.TickRate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TickTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tick rate magic
 */
@Mixin(TickTimer.class)
public abstract class TickTimerMixin implements AMixinTimer {
    @Shadow
    public int ticksThisFrame;

    @Shadow
    public float partialTick;

    @Shadow
    public float tickDelta;

    @Shadow
    private long lastTickTime;

    @Shadow
    private float mspt;

    @Unique
	public int elapsedTicksWorld;
    @Unique
	public float renderPartialTicksWorld;
    @Unique
	public float elapsedPartialTicksWorld;

    @Unique
	public int elapsedTicksPlayer;
    @Unique
	public float renderPartialTicksPlayer;
    @Unique
	public float elapsedPartialTicksPlayer;

    @Unique
	private final float tickLengthPlayer = 1000.0F / TickRate.NORMAL_RATE;

    @Override
    public int carpetClient$getElapsedTicksPlayer() {
        return this.elapsedTicksPlayer;
    }

    // @Override
    // public float getRenderPartialTicksWorld() {
    //     return this.renderPartialTicksWorld;
    // }
    @Override
    public float carpetClient$getRenderPartialTicksPlayer() {
        return this.renderPartialTicksPlayer;
    }
    @Override
    public void carpetClient$setRenderPartialTicksWorld(float value) {
        this.renderPartialTicksWorld = value;
        this.partialTick = this.renderPartialTicksWorld;
    }
    @Override
    public void carpetClient$setRenderPartialTicksPlayer(float value) {
        this.renderPartialTicksPlayer = value;
    }

    @Override
    public void carpetClient$setWorldTickRate(float tps) {
        this.mspt = 1000.0F / tps;
        this.renderPartialTicksPlayer = this.renderPartialTicksWorld;
    }

    @Override
    public float carpetClient$getWorldTickRate() {
        return 1000.0F / this.mspt;
    }

    @Override
    public float carpetClient$getPlayerTickRate() {
        return 1000.0F / this.tickLengthPlayer;
    }

    @Inject(method = "advance", at = @At("HEAD"), cancellable = true)
    public void updateTimer(CallbackInfo ci) {
        long i = Minecraft.getTime();
        long old = this.lastTickTime;
        this.lastTickTime = i;

        this.elapsedPartialTicksWorld = (float)(i - old) / this.mspt;
        this.renderPartialTicksWorld += this.elapsedPartialTicksWorld;
        this.elapsedTicksWorld = (int)this.renderPartialTicksWorld;
        this.renderPartialTicksWorld -= (float)this.elapsedTicksWorld;

        this.elapsedPartialTicksPlayer = (float)(i - old) / this.tickLengthPlayer;
        this.renderPartialTicksPlayer += this.elapsedPartialTicksPlayer;
        this.elapsedTicksPlayer = (int)this.renderPartialTicksPlayer;
        this.renderPartialTicksPlayer -= (float)this.elapsedTicksPlayer;

        // mostly used for EntityRenderer.updateCameraAndRender, hooked now with Mixin
        this.partialTick = this.renderPartialTicksWorld;
        // mostly used for GuiScreen.drawScreen
        this.tickDelta = this.elapsedPartialTicksPlayer;
        // mostly used for Minecraft.runTick
        this.ticksThisFrame = Math.max(this.elapsedTicksWorld, this.elapsedTicksPlayer);

        ci.cancel();
    }
}
