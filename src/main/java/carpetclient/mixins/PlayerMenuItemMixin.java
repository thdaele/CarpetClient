package carpetclient.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.PlayerMenuItem;
import net.minecraft.client.network.PlayerInfo;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerMenuItem.class)
public abstract class PlayerMenuItemMixin {
    @Shadow
    @Final
    private GameProfile profile;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/texture/TextureManager;bind(Lnet/minecraft/resource/Identifier;)V"))
    private void redirectbindTexture(TextureManager textureManager, Identifier resource) {
        final Minecraft mc = Minecraft.getInstance();
        final PlayerInfo npi = mc.player.networkHandler.getOnlinePlayer(this.profile.getName());
        if (npi != null) {
            mc.getTextureManager().bind(npi.getSkinTexture());
        }
    }
}
