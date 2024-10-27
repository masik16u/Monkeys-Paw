package net.masik.monkeyspaw.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.masik.monkeyspaw.component.ModComponents;
import net.masik.monkeyspaw.effect.ModEffects;
import net.masik.monkeyspaw.item.ModItems;
import net.masik.monkeyspaw.sound.ModSounds;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Random;

public class WishCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        dispatcher.register(CommandManager.literal("wish")
                .then(CommandManager.argument("wish", MessageArgumentType.message())
                .executes(WishCommand::run)));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();

        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null) {
            source.sendFeedback(() -> Text.literal("[Monkey's Paw] Error: Player is null").formatted(Formatting.RED), false);
            return -1;
        }

        ItemStack stack = player.getMainHandStack();

        if (stack.getItem() != ModItems.MONKEYS_PAW) {
            player.sendMessageToClient(Text.literal("Player should hold Monkey's Paw").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), true);
            return -1;
        }

        if (!stack.getComponents().contains(DataComponentTypes.CUSTOM_MODEL_DATA) || !stack.getComponents().contains(ModComponents.WISHES)) {
            source.sendFeedback(() -> Text.literal("[Monkey's Paw] Error: Monkey's Paw doesn't have right components").formatted(Formatting.RED), false);
            return -1;
        }

        if (stack.get(ModComponents.WISHES) == 0) {
            player.sendMessageToClient(Text.literal("No wishes left").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), true);
            return -1;
        }

        String wish = MessageArgumentType.getMessage(context, "wish").getLiteralString();

        if (wish == null) {
            source.sendFeedback(() -> Text.literal("[Monkey's Paw] Error: Wish is null").formatted(Formatting.RED), false);
            return -1;
        }

        wish = wish.toLowerCase();

        int wishTextIndex = WISHES_TEXT.indexOf(wish);

        if (wishTextIndex == -1) {
            player.sendMessageToClient(Text.literal("Wish doesn't exist").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), true);
            return -1;
        }

        Random random = new Random();

        if (wish.equals(WISH_INVISIBILITY)) {

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 12000, 0, false, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 12000, 0, false, false, true));

        }
        else if (wish.equals(WISH_RISK_LIFE)) {

            if (player.hasStatusEffect(ModEffects.INEVITABLE_DEATH) || player.hasStatusEffect(ModEffects.SECOND_CHANCE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            if (random.nextBoolean()) {
                player.addStatusEffect(new StatusEffectInstance(ModEffects.INEVITABLE_DEATH, 48000, 0, false, false, true));
            }
            else {
                player.addStatusEffect(new StatusEffectInstance(ModEffects.SECOND_CHANCE, 48000, 0, false, false, true));
            }

        }
        else if (wish.equals(WISH_STRENGTH)) {

            if (player.hasStatusEffect(ModEffects.VULNERABILITY) || player.hasStatusEffect(ModEffects.DIVINE_SWORD)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.addStatusEffect(new StatusEffectInstance(ModEffects.VULNERABILITY, 48000, 0, false, false, true));
            player.addStatusEffect(new StatusEffectInstance(ModEffects.DIVINE_SWORD, 48000, 0, false, false, true));

        }
        else if (wish.equals(WISH_SACRIFICE_STOMACH)) {

            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();
            int foodLevel = player.getHungerManager().getFoodLevel();

            int healthMissing = (int) Math.floor(maxHealth - health);

            int foodLevelLeft = foodLevel - healthMissing;

            player.getHungerManager().setFoodLevel(Math.max(foodLevelLeft, 0));
            player.getHungerManager().setSaturationLevel(0);
            player.setHealth(health + healthMissing + Math.min(foodLevelLeft, 0));

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 400, 1));

        }
        else if (wish.equals(WISH_ESCAPE)) {

            player.requestTeleport(random.nextDouble(player.getX() - 5000, player.getX() + 5000), player.getY(),
                    random.nextDouble(player.getZ() - 5000, player.getZ() + 5000));

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 1));

        }
        else if (wish.equals(WISH_CLEAR_WEATHER)) {

            if (!player.getServerWorld().isRaining() || player.hasStatusEffect(ModEffects.NIGHTMARE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.getServerWorld().setWeather(24000, 0, false, false);

            player.addStatusEffect(new StatusEffectInstance(ModEffects.NIGHTMARE, 48000, 0, false, false, true));

        }
        else if (wish.equals(WISH_RAINY_WEATHER)) {

            if ((player.getServerWorld().isRaining() && !player.getServerWorld().isThundering()) || player.hasStatusEffect(ModEffects.NIGHTMARE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.getServerWorld().setWeather(0, 24000, true, false);

            player.addStatusEffect(new StatusEffectInstance(ModEffects.NIGHTMARE, 48000, 0, false, false, true));

        }
        else if (wish.equals(WISH_THUNDERSTORM)) {

            if (player.getServerWorld().isThundering() || player.hasStatusEffect(ModEffects.NIGHTMARE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.getServerWorld().setWeather(0, 24000, true, true);

            player.addStatusEffect(new StatusEffectInstance(ModEffects.NIGHTMARE, 96000, 0, false, false, true));

        }
        else if (wish.equals(WISH_DAYTIME)) {

            if (player.getServerWorld().isDay() || player.hasStatusEffect(ModEffects.NIGHTMARE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.getServerWorld().setTimeOfDay(player.getServerWorld().getTimeOfDay() - player.getServerWorld().getTimeOfDay() % 24000L);

            player.addStatusEffect(new StatusEffectInstance(ModEffects.NIGHTMARE, 48000, 0, false, false, true));

        }
        else if (wish.equals(WISH_NIGHTTIME)) {

            if (player.getServerWorld().isNight() || player.hasStatusEffect(ModEffects.NIGHTMARE)) {
                wishDuplicateMessage(player);
                return -1;
            }

            player.getServerWorld().setTimeOfDay(player.getServerWorld().getTimeOfDay() + 13000L - player.getServerWorld().getTimeOfDay() % 24000L);

            player.addStatusEffect(new StatusEffectInstance(ModEffects.NIGHTMARE, 24000, 0, false, false, true));

        }

        source.getWorld().playSound(null, player.getBlockPos(), ModSounds.MONKEYS_PAW_CRACKLE, SoundCategory.AMBIENT, 1f, 1f);

        stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(stack.get(DataComponentTypes.CUSTOM_MODEL_DATA).value() - 1));
        stack.set(ModComponents.WISHES, stack.get(ModComponents.WISHES) - 1);

        return 1;

    }

    private static void wishDuplicateMessage(ServerPlayerEntity player) {
        player.sendMessageToClient(Text.literal("Wish cannot be granted").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), true);
    }

    private static final String WISH_INVISIBILITY = "i wish to be unseen";
    private static final String WISH_RISK_LIFE = "i wish to risk my life";
    private static final String WISH_STRENGTH = "i wish for strength";
    private static final String WISH_SACRIFICE_STOMACH = "i wish to sacrifice my stomach";
    private static final String WISH_ESCAPE = "i wish to escape";
    private static final String WISH_CLEAR_WEATHER = "i wish for clear weather";
    private static final String WISH_RAINY_WEATHER = "i wish for rainy weather";
    private static final String WISH_THUNDERSTORM = "i wish for thunderstorm";
    private static final String WISH_DAYTIME = "i wish for daytime";
    private static final String WISH_NIGHTTIME = "i wish for nighttime";

    public static final ArrayList<String> WISHES_TEXT = new ArrayList<>() {{
       add(WISH_INVISIBILITY);
       add(WISH_RISK_LIFE);
       add(WISH_STRENGTH);
       add(WISH_SACRIFICE_STOMACH);
       add(WISH_ESCAPE);
       add(WISH_CLEAR_WEATHER);
       add(WISH_RAINY_WEATHER);
       add(WISH_THUNDERSTORM);
       add(WISH_DAYTIME);
       add(WISH_NIGHTTIME);
    }};

}
