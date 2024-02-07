package carpetclient.mixins.optifine;

import carpetclient.CarpetClient;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

public class OptifinePlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            Class.forName("optifine.OptiFineForgeTweaker");
            CarpetClient.LOGGER.info("Optifine detected");
            return true;
        } catch (ClassNotFoundException e) {
			CarpetClient.LOGGER.info("Optifine not detected");
            return false;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInf) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInf) {

	}
}
