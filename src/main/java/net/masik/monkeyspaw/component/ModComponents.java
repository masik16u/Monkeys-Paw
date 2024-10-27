package net.masik.monkeyspaw.component;

import com.mojang.serialization.Codec;
import net.masik.monkeyspaw.MonkeysPaw;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {

    public static final ComponentType<Integer> WISHES = Registry.register(
            Registries.DATA_COMPONENT_TYPE, Identifier.of(MonkeysPaw.MOD_ID, "wishes"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void registerModComponents() {

    }

}
