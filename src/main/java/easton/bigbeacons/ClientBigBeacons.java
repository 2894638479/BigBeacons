package easton.bigbeacons;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class ClientBigBeacons implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BigBeacons.PACKET_ID,
            (MinecraftClient mc, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) -> {
            mc.execute(() -> {
                PacketByteBuf sendBuf = new PacketByteBuf(Unpooled.buffer());
                sendBuf.writeBoolean(true);
                sender.sendPacket(BigBeacons.PACKET_ID,sendBuf);
            });
        });
    }
}
