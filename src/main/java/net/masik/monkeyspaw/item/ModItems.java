package net.masik.monkeyspaw.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.masik.monkeyspaw.MonkeysPaw;
import net.masik.monkeyspaw.component.ModComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item MONKEYS_PAW = registerItem("monkeys_paw",
            new Item.Settings()
                    .maxCount(1)
                    .component(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(5))
                    .component(ModComponents.WISHES, 5)
                    .rarity(Rarity.EPIC));

    private static Item registerItem(String name, Item.Settings settings) {

        Identifier id = Identifier.of(MonkeysPaw.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

        return Registry.register(Registries.ITEM, key, new Item(settings.registryKey(key)));

    }

    private static void addToToolsTab(FabricItemGroupEntries entries) {

        entries.add(MONKEYS_PAW);

    }

    public static void registerModItems() {

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addToToolsTab);

    }

}
