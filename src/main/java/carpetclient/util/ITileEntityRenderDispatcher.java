package carpetclient.util;

import net.minecraft.block.entity.BlockEntity;

public interface ITileEntityRenderDispatcher
{
    void carpetClient$renderTileEntityOffset(BlockEntity tileentityIn, float partialTicks, int destroyStage, double xOffset, double yOffset, double zOffset);
}
