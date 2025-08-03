package easton.bigbeacons.mixin;

import easton.bigbeacons.BigBeacons;
import easton.bigbeacons.PlayerModdedDuck;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Mutable
    @Shadow @Final public static StatusEffect[][] EFFECTS_BY_LEVEL;

    @Redirect(
            method = "<clinit>()V",
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;")
    )
    private static Stream<StatusEffect[]> addEffectsToBeacon(Object[] array) {
        EFFECTS_BY_LEVEL = new StatusEffect[][]{
                {StatusEffects.SPEED, StatusEffects.HASTE},
                {StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST},
                {StatusEffects.STRENGTH},
                {StatusEffects.REGENERATION},
                {StatusEffects.FIRE_RESISTANCE},
                {StatusEffects.SATURATION},
                {StatusEffects.ABSORPTION},
                {StatusEffects.LUCK}
        };
        return Arrays.stream(EFFECTS_BY_LEVEL);
    }

    @ModifyConstant(method = "updateLevel", constant = @Constant(intValue = 4))
    private static int moreLevels(int curr) {
        return 16;
    }

    @Inject(method = "applyPlayerEffects", at=@At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void applyLevelThreeEffects(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci, double d, int i, int j, Box box, List<PlayerEntity> list) {
        if (beaconLevel >= 10 && Objects.equals(primaryEffect, secondaryEffect)) {
            for (PlayerEntity playerEntity : list) {
                playerEntity.addStatusEffect(new StatusEffectInstance(primaryEffect, j, 2, true, true));
            }
        }
        if (beaconLevel >= 16) {
            for (PlayerEntity player : list) {
                // we can't give the effect to vanilla players, it will cause them to disconnect since they don't know what it is
                // so we check if they have the mod locally (not an SPE) or if the server has them as having the mod
                if (!(player instanceof ServerPlayerEntity) || ((PlayerModdedDuck) player).hasMod()) {
                    player.addStatusEffect(new StatusEffectInstance(BigBeacons.FLIGHT, j, 0, true, false));
                }
            }
        }
    }

}
