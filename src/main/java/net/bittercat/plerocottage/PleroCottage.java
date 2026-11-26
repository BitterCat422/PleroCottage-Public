package net.bittercat.plerocottage;

import com.mojang.logging.LogUtils;

import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ModContent;
import net.bittercat.plerocottage.init.ModRegistries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.bittercat.plerocottage.network.NetworkHandler;
import org.slf4j.Logger;

@Mod(PleroCottage.MODID)
public final class PleroCottage {

    public static final String MODID = "plerocottage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PleroCottage(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModContent.bootstrap();
        // 触发网络消息注册，确保客户端与服务端使用同一协议。
        NetworkHandler.init();
        ModRegistries.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        MinecraftForge.EVENT_BUS.register(this);
        PleroCottageMachine.initialize(modEventBus);
    }
}
