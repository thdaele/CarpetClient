package carpetclient.mixins;

import net.minecraft.client.RecipeCollection;
import net.minecraft.client.crafting.ClientRecipeBook;
import net.minecraft.item.CreativeModeTab;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientRecipeBook.class)
public interface IClientRecipeBook {

    @Invoker
    static RecipeCollection callSetupTab(CreativeModeTab srcTab) {
        return null;
    }

    @Invoker
    static CreativeModeTab callGetTab(ItemStack stackIn) {
        return null;
    }
}
