package net.masik.monkeyspaw.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.masik.monkeyspaw.command.WishCommand;
import net.masik.monkeyspaw.component.ModComponents;
import net.masik.monkeyspaw.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetCustomModelDataLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModLootTableModifiers {

    public static void modifyLootTables() {

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (key == RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("chests/woodland_mansion"))) {

                LootPool.Builder lootPool = LootPool.builder()
                        .with(EmptyEntry.builder().weight(8))
                        .with(ItemEntry.builder(ModItems.MONKEYS_PAW)
                                .apply(SetComponentsLootFunction.builder(ModComponents.WISHES, 1))
                                .apply(SetComponentsLootFunction.builder(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(1)))
                                .weight(4))
                        .with(ItemEntry.builder(ModItems.MONKEYS_PAW)
                                .apply(SetComponentsLootFunction.builder(ModComponents.WISHES, 2))
                                .apply(SetComponentsLootFunction.builder(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(2)))
                                .weight(4))
                        .with(ItemEntry.builder(ModItems.MONKEYS_PAW)
                                .apply(SetComponentsLootFunction.builder(ModComponents.WISHES, 3))
                                .apply(SetComponentsLootFunction.builder(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(3)))
                                .weight(4))
                        .with(ItemEntry.builder(ModItems.MONKEYS_PAW)
                                .apply(SetComponentsLootFunction.builder(ModComponents.WISHES, 4))
                                .apply(SetComponentsLootFunction.builder(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(4)))
                                .weight(2))
                        .with(ItemEntry.builder(ModItems.MONKEYS_PAW)
                                .apply(SetComponentsLootFunction.builder(ModComponents.WISHES, 5))
                                .apply(SetComponentsLootFunction.builder(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(5)))
                                .weight(1))
                        .rolls(ConstantLootNumberProvider.create(1));

                tableBuilder.pool(lootPool);

                LootPool.Builder tagsLootPool = LootPool.builder()
                        .with(EmptyEntry.builder().weight(3))
                        .rolls(ConstantLootNumberProvider.create(1));

                for (String wish : WishCommand.WISHES_TEXT) {
                    tagsLootPool = tagsLootPool.with(ItemEntry.builder(Items.NAME_TAG).apply(SetNameLootFunction.builder(Text.literal(wish.substring(0, 1).toUpperCase() + wish.substring(1)), SetNameLootFunction.Target.ITEM_NAME)));
                }

                tableBuilder.pool(tagsLootPool);

            }
        });

    }

}
