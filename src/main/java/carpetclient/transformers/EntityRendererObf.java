package carpetclient.transformers;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Provide necessary obfuscation mapping at run time
 */

@Mixin(GameRenderer.class)
@SuppressWarnings("unused")
public abstract class EntityRendererObf {
    private static GameRenderer __TARGET;

    private void getMouseOverObf(float partialTicks) {
        __TARGET.pick(partialTicks);
    }

    private void updateRendererObf() {
        __TARGET.tick();
    }

    private void stopUseShaderObf() {
        __TARGET.closeShader();
    }
}
