package me.juancarloscp52.bedrockify.client.features.heldItemTooltips;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.ContainerTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.EnchantmentTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.PotionTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.Tooltip;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HeldItemTooltips {

    private static final int TOOLTIP_SIZE = 6;

    private static final boolean B_DAB_LOADED = FabricLoader.getInstance().isModLoaded("detailab");

    public void drawItemWithCustomTooltips(GuiGraphics drawContext, Font fontRenderer, Component text, float x, float y, int color, ItemStack currentStack) {
        final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        final int screenBorder = settings.getScreenSafeArea();
        int tooltipOffset = 0;

        //Set tooltip position depending on hotbar displayed information
        LocalPlayer player = Minecraft.getInstance().player;
        if(null ==player || null== Minecraft.getInstance().gameMode)
            return;
        if(Minecraft.getInstance().gameMode.canHurtPlayer()){
            y-=16;
            if(player.getArmorValue()>0 || (B_DAB_LOADED && Inventory.EQUIPMENT_SLOT_MAPPING.keySet().intStream().anyMatch(value -> player.getInventory().getItem(value).has(DataComponents.GLIDER)))){
                y-=10;
            }
            if(player.getAbsorptionAmount()>0){
                y-=10;
            }
        }else if((player.getVehicle()!=null && player.getVehicle() instanceof LivingEntity)){
            y-=16;
        }

        // Get the current held item tooltips and convert to Text.
        final List<Component> tooltips = new ArrayList<>();
        for (Tooltip tooltip : getTooltips(currentStack)) {
            tooltips.add(tooltip.getTooltipText());
        }
        // Limit the maximum number of shown tooltips to tooltipSize.
        final boolean showMoreTooltip = (tooltips.size() > TOOLTIP_SIZE);
        if (showMoreTooltip) {
            // Store the number of items.
            final int xMore = tooltips.size() - (TOOLTIP_SIZE -1);
            // Trim tooltips.
            tooltips.subList(TOOLTIP_SIZE - 1, tooltips.size()).clear();
            // Add the "and x more..." tooltip.
            tooltips.add(Component.translatable("item.container.more_items", xMore).withStyle(ChatFormatting.GRAY));
        }

        tooltipOffset = 12 * tooltips.size();
        //Render background behind tooltip.
        int maxLength = getMaxTooltipLength(tooltips,fontRenderer,currentStack);
        renderBackground(drawContext, y, screenBorder, tooltipOffset, maxLength, color >> 24 & 0xff);


        int i = tooltips.size() - 1;
        for (Component elem : tooltips) {
            // Render the tooltip.
            renderTooltip(drawContext, fontRenderer, y - screenBorder - (12 * i), color, ((MutableComponent)elem).withStyle(ChatFormatting.GRAY));
            --i;
        }
        // Render the item name.
        drawContext.drawString(fontRenderer, text, (int)x, (int)(y - tooltipOffset - screenBorder), color);
    }

    /**
     * Gets a List with the given item tooltips.
     * @param currentStack Current item stack of the player.
     * @return List with the tooltip information.
     */
    public static List<Tooltip> getTooltips(ItemStack currentStack) {
        final Item item = currentStack.getItem();
        final List<Tooltip> result = new ArrayList<>();
        if (item == Items.ENCHANTED_BOOK || currentStack.isEnchanted()) {
            var enchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(currentStack);
            enchantmentsComponent.keySet().forEach(enchantment -> result.add(new EnchantmentTooltip(enchantment.value(), enchantmentsComponent.getLevel(enchantment))));

        } else if (item instanceof PotionItem || item instanceof TippedArrowItem) {
            result.addAll(generateTooltipsForPotion(currentStack));

        } else if (item == Items.OMINOUS_BOTTLE) {
            var ominousComponent = currentStack.getComponents().get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
            if (ominousComponent != null) {
                List<MobEffectInstance> list = List.of(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, ominousComponent.value(), false, false, true));
                result.addAll(generateTooltipsForPotion(currentStack, list));
            }

        } else if(currentStack.getComponents().has(DataComponents.CONTAINER)){
            var container = currentStack.get(DataComponents.CONTAINER);
            if(container != null){
                generateTooltipsFromContainer(container.nonEmptyItemCopyStream().toList(), result);
            }

        } else if (currentStack.getComponents().has(DataComponents.BUNDLE_CONTENTS)){
            var container = currentStack.getComponents().get(DataComponents.BUNDLE_CONTENTS);
            if(container != null){
                generateTooltipsFromContainer(container.itemCopyStream().toList(), result);
            }
        }

        return result;
    }

    /**
     * Checks if the tooltips of two items are equal.
     */
    public boolean equals(ItemStack item1, ItemStack item2){
        List<Tooltip> itemTooltips1 = getTooltips(item1);
        List<Tooltip> itemTooltips2 = getTooltips(item2);
        // Overriding Object#equals in the class Tooltip allows the use of utility classes provided by Java.
        return Objects.equals(itemTooltips1, itemTooltips2);
    }

    private static void generateTooltipsFromContainer(List<ItemStack> items, List<Tooltip> instance){
        for(ItemStack item : items){
            if(!item.isEmpty())
                instance.add(new ContainerTooltip(item));
        }
    }

    private static List<PotionTooltip> generateTooltipsForPotion(ItemStack stack, Iterable<MobEffectInstance> effects){
        List<PotionTooltip> tooltips = new ArrayList<>();
        for (MobEffectInstance statusEffectInstance : effects) {
            Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
            int i = statusEffectInstance.getAmplifier();
            MutableComponent mutableText = PotionContents.getPotionDescription(registryEntry, i);
            if (!statusEffectInstance.endsWithin(20)) {
                mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(statusEffectInstance, stack.getComponents().getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F), Minecraft.getInstance().level.tickRateManager().tickrate()));
            }

            tooltips.add(new PotionTooltip(mutableText.withStyle(registryEntry.value().getCategory().getTooltipFormatting())));
        }

        if (tooltips.isEmpty()) {
            tooltips.add(new PotionTooltip(Component.translatable("effect.none")));
        }
        return tooltips;
    }

    private static List<PotionTooltip> generateTooltipsForPotion(ItemStack stack) {
        return generateTooltipsForPotion(stack, stack.get(DataComponents.POTION_CONTENTS).getAllEffects());
    }

    private void renderBackground(GuiGraphics drawContext, float y, int screenBorder, int tooltipOffset, int maxLength, int alpha) {
        Minecraft client = Minecraft.getInstance();
        int background = Mth.lerpInt(alpha / 255f, 0, Mth.ceil((255.0D * BedrockifyClient.getInstance().settings.heldItemTooltipBackground))) << 24;
        drawContext.fill(Mth.ceil((client.getWindow().getGuiScaledWidth()-maxLength)/2f-3), Mth.ceil(y - tooltipOffset -5- screenBorder), Mth.ceil((client.getWindow().getGuiScaledWidth()+maxLength)/2f+1), Mth.ceil(y - tooltipOffset -4- screenBorder),background);
        drawContext.fill(Mth.ceil((client.getWindow().getGuiScaledWidth()-maxLength)/2f-3), Mth.ceil(y+12-screenBorder), Mth.ceil((client.getWindow().getGuiScaledWidth()+maxLength)/2f+1), Mth.ceil(y+13-screenBorder),background);
        drawContext.fill(Mth.ceil((client.getWindow().getGuiScaledWidth()-maxLength)/2f-4), Mth.ceil(y - tooltipOffset -4- screenBorder), Mth.ceil((client.getWindow().getGuiScaledWidth()+maxLength)/2f+2), Mth.ceil(y+12-screenBorder),background);
    }

    /**
     * Renders an item tooltip with the given text and height in screen.
     */
    private void renderTooltip(GuiGraphics drawContext, Font fontRenderer, float y, int color, Component text) {
        int enchantX = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - fontRenderer.width(text)) / 2;
        drawContext.drawString(fontRenderer, text, enchantX, (int)y, color);
    }

    private int getMaxTooltipLength(List<Component> tooltips, Font textRenderer, ItemStack itemStack){
        int maxLength=textRenderer.width(itemStack.getHoverName());
        for(Component elem : tooltips){
            int tipLength = textRenderer.width(elem);
            if(maxLength<tipLength)
                maxLength=tipLength;
        }
        return maxLength;
    }
}
