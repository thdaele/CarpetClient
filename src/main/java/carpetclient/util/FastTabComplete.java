package carpetclient.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.*;
import net.minecraft.server.command.handler.CommandListener;
import net.minecraft.server.command.handler.CommandRegistry;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.dedicated.command.*;

public class FastTabComplete extends CommandRegistry implements CommandListener {

    public FastTabComplete(){
        this.register(new TimeCommand());
        this.register(new GameModeCommand());
        this.register(new DifficultyCommand());
        this.register(new DefaultGameModeCommand());
        this.register(new KillCommand());
        this.register(new ToggleDownfallCommand());
        this.register(new WeatherCommand());
        this.register(new ExperienceCommand());
        this.register(new TpCommand());
        this.register(new TeleportCommand());
        this.register(new GiveCommand());
        this.register(new ReplaceItemCommand());
        this.register(new StatsCommand());
        this.register(new EffectCommand());
        this.register(new EnchantCommand());
        this.register(new ParticleCommand());
        this.register(new MeCommand());
        this.register(new SeedCommand());
        this.register(new HelpCommand());
        this.register(new DebugCommand());
        this.register(new TellCommand());
        this.register(new SayCommand());
        this.register(new SpawnPointCommand());
        this.register(new SetWorldSpawnCommand());
        this.register(new GameRuleCommand());
        this.register(new ClearCommand());
        this.register(new TestForCommand());
        this.register(new SpreadPlayersCommand());
        this.register(new PlaySoundCommand());
        this.register(new ScoreboardCommand());
        this.register(new ExecuteCommand());
        this.register(new TriggerCommand());
        this.register(new AdvancementCommand());
        this.register(new RecipeCommand());
        this.register(new SummonCommand());
        this.register(new SetBlockCommand());
        this.register(new FillCommand());
        this.register(new CloneCommand());
        this.register(new TestForBlocksCommand());
        this.register(new BlockDataCommand());
        this.register(new TestForBlockCommand());
        this.register(new TellRawCommand());
        this.register(new WorldBorderCommand());
        this.register(new TitleCommand());
        this.register(new EntityDataCommand());
        this.register(new StopSoundCommand());
        this.register(new LocateCommand());
        this.register(new ReloadCommand());
        this.register(new FunctionCommand());
        this.register(new OpCommand());
        this.register(new DeOpCommand());
        this.register(new StopCommand());
        this.register(new SaveAllCommand());
        this.register(new SaveOffCommand());
        this.register(new SaveOnCommand());
        this.register(new BanIpCommand());
        this.register(new PardonIpCommand());
        this.register(new BanCommand());
        this.register(new BanListCommand());
        this.register(new PardonCommand());
        this.register(new KickCommand());
        this.register(new ListCommand());
        this.register(new WhitelistCommand());
        this.register(new SetIdleTimeoutCommand());
    }

    @Override
    protected MinecraftServer getServer() {
        return null;
    }

    @Override
    public void sendSuccess(CommandSource sender, Command command, int flags, String translationKey, Object... translationArgs) {
    }
}
