package carpetclient.mixins;

import net.minecraft.network.CompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CompressionDecoder.class)
public class CompressionDecoderMixin {
    @ModifyConstant(method = "decode", constant = @Constant(intValue = 2097152), remap = false)
    private int limitExpander(int timeoutSeconds) {
        return Integer.MAX_VALUE;
    }
}
