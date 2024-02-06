package carpetclient.mixins;

import carpetclient.mixinInterface.AMixinRegistryNamespaced;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import net.minecraft.util.CrudeIncrementalIntIdentityHashMap;
import net.minecraft.util.IdObjectIterable;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.MappedRegistry;

@Mixin(IdRegistry.class)
public class IdRegistryMixin<K, V> extends MappedRegistry<K, V> implements IdObjectIterable<V>, AMixinRegistryNamespaced {

    @Shadow
    @Final
    protected CrudeIncrementalIntIdentityHashMap<V> ids = new CrudeIncrementalIntIdentityHashMap<V>(256);

    @Shadow
    @Final
    protected Map<V, K> keys;

    public void carpetClient$clear() {
        ids.clear();
        keys.clear();
        entries.clear();
    }
}
