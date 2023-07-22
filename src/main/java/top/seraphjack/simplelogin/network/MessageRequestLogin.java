package top.seraphjack.simplelogin.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import top.seraphjack.simplelogin.client.PasswordHolder;

@SuppressWarnings({"InstantiationOfUtilityClass", "unused"})
public class MessageRequestLogin {
    public MessageRequestLogin() {}

    public static void encode(MessageRequestLogin msg, FriendlyByteBuf buffer) {
        // NO-OP
    }

    public static MessageRequestLogin decode(FriendlyByteBuf buffer) {
        return new MessageRequestLogin();
    }

    public static void handle(MessageRequestLogin message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> NetworkLoader
            .INSTANCE
            .sendToServer(new MessageLogin(PasswordHolder.instance().password()))
        );
        ctx.get().setPacketHandled(true);
    }
}
