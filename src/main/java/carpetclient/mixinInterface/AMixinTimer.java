package carpetclient.mixinInterface;

/**
 * Duck interface for MixinTimer.java
 */
public interface AMixinTimer {
    int carpetClient$getElapsedTicksPlayer();
    // float getRenderPartialTicksWorld();
    float carpetClient$getRenderPartialTicksPlayer();
    void carpetClient$setRenderPartialTicksWorld(float value);
    void carpetClient$setRenderPartialTicksPlayer(float value);

    void carpetClient$setWorldTickRate(float tps);
    float carpetClient$getWorldTickRate();
    float carpetClient$getPlayerTickRate();
}
