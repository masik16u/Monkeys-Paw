package net.masik.monkeyspaw.effect.custom;

import net.masik.monkeyspaw.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class InevitableDeathEffect extends StatusEffect {
    public InevitableDeathEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {

        if (entity.getStatusEffect(ModEffects.INEVITABLE_DEATH).isDurationBelow(1)) {
            entity.damage(world, entity.getDamageSources().magic(), 1000);
        }

        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
