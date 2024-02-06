package carpetclient.mixins;

import carpetclient.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
Mixen class to implement relaxed block placement, placement of torches on jack-o-lanterns.
*/
@Mixin(TorchBlock.class)
public class TorchBlockMixin extends Block {

    public TorchBlockMixin(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

//    @Overwrite
//    private boolean canPlaceOn(World worldIn, BlockPos pos)
//    {
//        Block block = worldIn.getBlockState(pos).getBlock();
//        boolean flag = block == Blocks.END_GATEWAY || (block == Blocks.LIT_PUMPKIN && !Config.relaxedBlockPlacement);
//
//        if (worldIn.getBlockState(pos).isTopSolid())
//        {
//            return !flag;
//        }
//        else
//        {
//            boolean flag1 = block instanceof BlockFence || block == Blocks.GLASS || block == Blocks.COBBLESTONE_WALL || block == Blocks.STAINED_GLASS;
//            return flag1 && !flag;
//        }
//    }

    @Inject(method = "canSitOnTop", at = @At("HEAD"), cancellable = true)
    public void canPlaceOnOver(World worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block block = worldIn.getBlockState(pos).getBlock();
        boolean flag = block == Blocks.END_GATEWAY || (block == Blocks.LIT_PUMPKIN && !Config.relaxedBlockPlacement);

        if (worldIn.getBlockState(pos).isFullBlock())
        {
            cir.setReturnValue(!flag);
        }
        else
        {
            boolean flag1 = block instanceof FenceBlock || block == Blocks.GLASS || block == Blocks.COBBLESTONE_WALL || block == Blocks.STAINED_GLASS;
            cir.setReturnValue(flag1 && !flag);
        }
    }
}
