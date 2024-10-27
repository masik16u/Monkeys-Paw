package net.masik.monkeyspaw.sound;

import net.masik.monkeyspaw.MonkeysPaw;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent MONKEYS_PAW_CRACKLE = registerSoundEvent("monkeys_paw_crackle");

    private static SoundEvent registerSoundEvent(String name) {

        return Registry.register(Registries.SOUND_EVENT, Identifier.of(MonkeysPaw.MOD_ID, name),
                SoundEvent.of(Identifier.of(MonkeysPaw.MOD_ID, name)));

    }

    public static void registerSounds() {

    }

}
