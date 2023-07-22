package top.seraphjack.simplelogin.server.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import top.seraphjack.simplelogin.SLConstants;
import top.seraphjack.simplelogin.server.storage.Position;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = SLConstants.MODID)
public class CapabilityLoader {
    public static final Capability<ILastPos> CAPABILITY_LAST_POS = CapabilityManager.get(new CapabilityToken<>() {});

    private CapabilityLoader() {
        throw new UnsupportedOperationException("No instance.");
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ILastPos.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                new ResourceLocation(SLConstants.MODID, "sl_last_pos"),
                new CapabilityLastPos.Provider()
            );
        }
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) throws Throwable {
        Capability[] capabilities = new Capability[]{CAPABILITY_LAST_POS};

        for (Capability capability : capabilities) {
            if (
                event.getOriginal().getCapability(capability, null).isPresent() &&
                event.getEntity().getCapability(capability, null).isPresent()
            ) {
                ILastPos originalLastPos = (ILastPos) event
                    .getOriginal()
                    .getCapability(capability, null)
                    .orElseThrow(RuntimeException::new);

                ILastPos entityLastPos = (ILastPos) event
                    .getEntity()
                    .getCapability(capability, null)
                    .orElseThrow(RuntimeException::new);

                Tag nbt = originalLastPos.getLastPos().toNBT();

                entityLastPos.setLastPos(Position.fromNBT((CompoundTag) nbt));
            }
        }
    }
}
