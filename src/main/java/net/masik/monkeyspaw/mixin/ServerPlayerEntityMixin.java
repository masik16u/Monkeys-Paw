package net.masik.monkeyspaw.mixin;

import com.mojang.datafixers.util.Either;
import net.masik.monkeyspaw.effect.ModEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void nightmareEffect(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir) {

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (player.hasStatusEffect(ModEffects.NIGHTMARE)) {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE));
        }

    }

}
