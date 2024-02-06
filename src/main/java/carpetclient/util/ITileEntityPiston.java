package carpetclient.util;

import net.minecraft.block.entity.BlockEntity;

public interface ITileEntityPiston
{
    void carpetClient$setCarriedBlockEntity(BlockEntity blockEntity);
    BlockEntity carpetClient$getCarriedBlockEntity();

    long carpetClient$getLastTicked();
    float carpetClient$getLastProgress();
}
