package net.masik.monkeyspaw.effect;

import net.masik.monkeyspaw.MonkeysPaw;
import net.masik.monkeyspaw.effect.custom.ModStatusEffect;
import net.masik.monkeyspaw.effect.custom.InevitableDeathEffect;
import net.masik.monkeyspaw.effect.custom.NightmareEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> INEVITABLE_DEATH = registerStatusEffect("inevitable_death",
            new InevitableDeathEffect(StatusEffectCategory.HARMFUL, 0x750216));
    public static final RegistryEntry<StatusEffect> SECOND_CHANCE = registerStatusEffect("second_chance",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xb26411));
    public static final RegistryEntry<StatusEffect> VULNERABILITY = registerStatusEffect("vulnerability",
            new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x750216));
    public static final RegistryEntry<StatusEffect> DIVINE_SWORD = registerStatusEffect("divine_sword",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xb26411));
    public static final RegistryEntry<StatusEffect> NIGHTMARE = registerStatusEffect("nightmare",
            new NightmareEffect(StatusEffectCategory.HARMFUL, 0x750216));

    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MonkeysPaw.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {

    }

    public static final ArrayList<RegistryEntry<StatusEffect>> MONKEYS_PAW_EFFECTS = new ArrayList<>() {{

        add(INEVITABLE_DEATH);
        add(SECOND_CHANCE);
        add(VULNERABILITY);
        add(DIVINE_SWORD);
        add(NIGHTMARE);

    }};

}
