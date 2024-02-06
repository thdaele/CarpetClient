package carpetclient.mixins;

import net.minecraft.crafting.CraftingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingManager.class)
public interface ICraftingManager {

    @Accessor("nextId")
    static void setNextId(int i) {
        throw new AssertionError("Mixin didn't patch me!");
    }
}
