package net.masik.monkeyspaw;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.masik.monkeyspaw.command.WishCommand;
import net.masik.monkeyspaw.component.ModComponents;
import net.masik.monkeyspaw.effect.ModEffects;
import net.masik.monkeyspaw.item.ModItems;
import net.masik.monkeyspaw.sound.ModSounds;
import net.masik.monkeyspaw.util.ModLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonkeysPaw implements ModInitializer {

	public static final String MOD_ID = "monkeys_paw";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register(WishCommand::register);

		ModItems.registerModItems();
		ModComponents.registerModComponents();

		ModSounds.registerSounds();

		ModEffects.registerEffects();

		ModLootTableModifiers.modifyLootTables();

	}
}