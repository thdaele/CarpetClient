package carpetclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockEntity;

@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin extends BlockEntity {

    @ModifyConstant(method = "readNbt", constant = @Constant(intValue = -32) , expect = 3)
    public int modifyNeg32(int orig) {
        return Integer.MIN_VALUE;
    }

    @ModifyConstant(method = "readNbt", constant = @Constant(intValue = 32) , expect = 6)
    public int modify32(int orig) {
        return Integer.MAX_VALUE;
    }

}
