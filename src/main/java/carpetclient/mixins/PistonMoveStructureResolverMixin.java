package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.block.piston.PistonMoveStructureResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonMoveStructureResolver.class)
public class PistonMoveStructureResolverMixin {

    /*
    Edits the push limit of pistons to allow visually better
     */
    @ModifyConstant(method = "addColumn", constant = @Constant(intValue = 12), expect = 3)
    private int pushLimit(int orig) {
        return Config.pushLimit;
    }
}
