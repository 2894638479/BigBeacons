package easton.bigbeacons;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BigBeacons implements ModInitializer {

	public static final String MOD_ID = "bigbeacons";
	public static final StatusEffect FLIGHT = new FlightEffect();
	public static final Identifier PACKET_ID = Identifier.of("bigbeacons", "mod-check");


	@Override
	public void onInitialize() {
		Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "flight"), FLIGHT);
		ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, ( mc, player,handler,buf,sender) -> {
			((PlayerModdedDuck)player).setHasMod(true);
		});
	}

	public static void sendS2CCheckHasModPacket(ServerPlayerEntity player){
		PacketByteBuf sendBuf = new PacketByteBuf(Unpooled.buffer());
		sendBuf.writeBoolean(true);
		ServerPlayNetworking.send(player,PACKET_ID,sendBuf);
	}
}
