package top.seraphjack.simplelogin;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

import top.seraphjack.simplelogin.command.CommandLoader;
import top.seraphjack.simplelogin.network.NetworkLoader;
import top.seraphjack.simplelogin.server.ServerLoader;

@Mod(SLConstants.MODID)
@ParametersAreNonnullByDefault
public final class SimpleLogin {
    public static final Logger logger = LogManager.getLogger(SLConstants.MODID);

    public SimpleLogin() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommandLoader::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ServerLoader::serverSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> NetworkLoader.registerPackets());

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SLConfig.SERVER_SPEC);
    }
}
