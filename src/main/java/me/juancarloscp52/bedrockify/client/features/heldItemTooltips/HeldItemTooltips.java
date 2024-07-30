package me.juancarloscp52.bedrockify.client.features.heldItemTooltips;

import com.google.common.collect.Lists;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.ContainerTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.EnchantmentTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.PotionTooltip;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip.Tooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Objects;

public class HeldItemTooltips {

    private final int  tooltipSize = 6;

    public int drawItemWithCustomTooltips(DrawContext drawContext, TextRenderer fontRenderer, Text text, float x, float y, int color, ItemStack currentStack) {
        final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        final int screenBorder = settings.getScreenSafeArea();
        int tooltipOffset = 0;

        //Set tooltip position depending on hotbar displayed information
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(null ==player || null==MinecraftClient.getInstance().interactionManager)
            return 0;
        if(MinecraftClient.getInstance().interactionManager.hasStatusBars()){
            y-=16;
            if(player.getArmor()>0){
                y-=10;
            }
            if(player.getAbsorptionAmount()>0){
                y-=10;
            }
        }else if((player.getVehicle()!=null && player.getVehicle() instanceof LivingEntity)){
            y-=16;
        }

        // Draw item tooltips if the option is enabled.
        if(settings.heldItemTooltips) {
            // Get the current held item tooltips and convert to Text.
            final List<Text> tooltips = Lists.newArrayList();
            for (Tooltip tooltip : getTooltips(currentStack)) {
                tooltips.add(tooltip.getTooltipText());
            }
            // Limit the maximum number of shown tooltips to tooltipSize.
            final boolean showMoreTooltip = (tooltips.size() > tooltipSize);
            if (showMoreTooltip) {
                // Store the number of items.
                final int xMore = tooltips.size() - (tooltipSize-1);
                // Trim tooltips.
                tooltips.subList(tooltipSize - 1, tooltips.size()).clear();
                // Add the "and x more..." tooltip.
                tooltips.add(Text.translatable("container.shulkerBox.more", xMore).formatted(Formatting.GRAY));
            }

            tooltipOffset = 12 * tooltips.size();
            //Render background behind tooltip.
            int maxLength = getMaxTooltipLength(tooltips,fontRenderer,currentStack);
            renderBackground(drawContext, y, screenBorder, tooltipOffset, maxLength, color >> 24 & 0xff);


            int i = tooltips.size() - 1;
            for (Text elem : tooltips) {
                // Render the tooltip.
                renderTooltip(drawContext, fontRenderer, y - screenBorder - (12 * i), color, ((MutableText)elem).formatted(Formatting.GRAY));
                --i;
            }
        }

        // Render the item name.
        return drawContext.drawTextWithShadow(fontRenderer, text, (int)x, (int)(y - tooltipOffset - screenBorder), color);
    }

    /**
     * Gets a List with the given item tooltips.
     * @param currentStack Current item stack of the player.
     * @return List with the tooltip information.
     */
    public List<Tooltip> getTooltips(ItemStack currentStack) {
        final Item item = currentStack.getItem();
        final List<Tooltip> result = Lists.newArrayList();
        //If the item is a enchanted book, retrieve the enchantments.
        if (item == Items.ENCHANTED_BOOK || currentStack.hasEnchantments()) {
            var enchantmentsComponent = EnchantmentHelper.getEnchantments(currentStack);
            enchantmentsComponent.getEnchantments().forEach(enchantment -> result.add(new EnchantmentTooltip(enchantment.value(), enchantmentsComponent.getLevel(enchantment))));
            //If the item has a potion effects, retrieve them.
        } else if (item instanceof PotionItem || item instanceof TippedArrowItem) {
            List<Text> generated = Lists.newArrayList();
            // Lingering Potion has its own multiplier of duration, and it is hardcoded.
            item.appendTooltip(currentStack, Item.TooltipContext.DEFAULT, generated, TooltipType.BASIC);
            generateTooltipsForPotion(generated, result);
        } else if(currentStack.getComponents().contains(DataComponentTypes.CONTAINER)){
            var container = currentStack.getComponents().get(DataComponentTypes.CONTAINER);
            if(container != null){
                generateTooltipsFromContainer(container.stream().toList(), result);
            }
        } else if (currentStack.getComponents().contains(DataComponentTypes.BUNDLE_CONTENTS)){
            var container = currentStack.getComponents().get(DataComponentTypes.BUNDLE_CONTENTS);
            if(container != null){
                generateTooltipsFromContainer(container.stream().toList(), result);
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

    /**
     * Formats a generated tooltip list from the given {@link Text} list.
     *
     * @param texts tooltip list of an item.
     * @param instance Where the list of {@link Tooltip} is stored.
     */
    private static void generateTooltipsForPotion(List<Text> texts, List<Tooltip> instance) {
        // Trim lines after "When Applied" string if present.
        int startIndex = texts.indexOf(ScreenTexts.EMPTY);
        if (startIndex > 0) {
            texts.subList(startIndex, texts.size()).clear();
        }
        texts.forEach((current) -> instance.add(new PotionTooltip(current)));
    }

    private void renderBackground(DrawContext drawContext, float y, int screenBorder, int tooltipOffset, int maxLength, int alpha) {
        MinecraftClient client = MinecraftClient.getInstance();
        int background = MathHelper.lerp(alpha / 255f, 0, MathHelper.ceil((255.0D * BedrockifyClient.getInstance().settings.heldItemTooltipBackground))) << 24;
        drawContext.fill(MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-3),MathHelper.ceil(y - tooltipOffset -5- screenBorder), MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+1),MathHelper.ceil(y - tooltipOffset -4- screenBorder),background);
        drawContext.fill(MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-3),MathHelper.ceil(y+12-screenBorder), MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+1),MathHelper.ceil(y+13-screenBorder),background);
        drawContext.fill(MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-4), MathHelper.ceil(y - tooltipOffset -4- screenBorder),MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+2), MathHelper.ceil(y+12-screenBorder),background);
    }

    /**
     * Renders an item tooltip with the given text and height in screen.
     */
    private void renderTooltip(DrawContext drawContext, TextRenderer fontRenderer, float y, int color, Text text) {
        int enchantX = (MinecraftClient.getInstance().getWindow().getScaledWidth() - fontRenderer.getWidth(text)) / 2;
        drawContext.drawTextWithShadow(fontRenderer, text, enchantX, (int)y, color);
    }

    private int getMaxTooltipLength(List<Text> tooltips, TextRenderer textRenderer, ItemStack itemStack){
        int maxLength=textRenderer.getWidth(itemStack.getName());
        for(Text elem : tooltips){
            int tipLength = textRenderer.getWidth(elem);
            if(maxLength<tipLength)
                maxLength=tipLength;
        }
        return maxLength;
    }
}
