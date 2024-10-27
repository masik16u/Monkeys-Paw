package net.masik.monkeyspaw.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.masik.monkeyspaw.effect.ModEffects;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "clearStatusEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onStatusEffectsRemoved(Ljava/util/Collection;)V", shift = At.Shift.AFTER))
    private void bypassClearEffects(CallbackInfoReturnable<Boolean> cir, @Local Map<RegistryEntry<StatusEffect>, StatusEffectInstance> map) {

        LivingEntity entity = (LivingEntity) (Object) this;

        for (Map.Entry<RegistryEntry<StatusEffect>, StatusEffectInstance> entry : map.entrySet()) {

            if (ModEffects.MONKEYS_PAW_EFFECTS.contains(entry.getKey())) {
                entity.addStatusEffect(entry.getValue());
            }

        }

    }

    @Inject(method = "tryUseDeathProtector", at = @At("RETURN"), cancellable = true)
    private void secondChanceEffect(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.hasStatusEffect(ModEffects.SECOND_CHANCE)) {
            entity.setHealth(1.0F);
            entity.getWorld().sendEntityStatus(entity, EntityStatuses.USE_TOTEM_OF_UNDYING);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 1));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0));
            entity.removeStatusEffect(ModEffects.SECOND_CHANCE);
            cir.setReturnValue(true);
        }

    }

}
