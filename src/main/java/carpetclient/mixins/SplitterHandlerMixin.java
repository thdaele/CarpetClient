package carpetclient.mixins;

import net.minecraft.network.SplitterHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SplitterHandler.class)
public class SplitterHandlerMixin {
    @ModifyConstant(method = "decode", constant = @Constant(intValue = 3), remap = false)
    private int limitExpander(int timeoutSeconds) {
        return 5;
    }
}
