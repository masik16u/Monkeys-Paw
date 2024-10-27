package net.masik.monkeyspaw.effect.custom;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

public class NightmareEffect extends StatusEffect {
    public NightmareEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {

        if (entity.isPlayer()) {

            Random random = Random.create();

            if ((world.isNight() || world.isThundering() || world.isRaining()) && random.nextInt(400) == 136) {

                PhantomEntity phantomEntity = EntityType.PHANTOM.create(world, SpawnReason.NATURAL);
                EntityData entityData = null;

                if (phantomEntity != null) {
                    phantomEntity.refreshPositionAndAngles(entity.getBlockPos().up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21)), 0.0F, 0.0F);
                    entityData = phantomEntity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.NATURAL, entityData);
                    world.spawnEntityAndPassengers(phantomEntity);
                }

            }
        }

        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
