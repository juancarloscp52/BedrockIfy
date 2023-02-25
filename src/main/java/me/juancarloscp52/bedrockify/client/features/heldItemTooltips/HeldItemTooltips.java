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
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HeldItemTooltips {

    private final int  tooltipSize = 6;

    public int drawItemWithCustomTooltips(TextRenderer fontRenderer, MatrixStack matrices, Text text, float x, float y, int color, ItemStack currentStack) {
        final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        final int screenBorder = settings.getScreenSafeArea();
        int tooltipOffset = 0;

        // Draw item tooltips if the option is enabled.
        if(settings.getHeldItemTooltip()>0) {
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
            //Render background behind tooltip if enabled.
            if(settings.getHeldItemTooltip()==2){
                int maxLength = getMaxTooltipLength(tooltips,fontRenderer,currentStack);
                renderBackground(matrices, y, screenBorder, tooltipOffset, maxLength);
            }

            int i = tooltips.size() - 1;
            for (Text elem : tooltips) {
                // Render the tooltip.
                renderTooltip(fontRenderer, matrices, y - screenBorder - (12 * i), color, ((MutableText)elem).formatted(Formatting.GRAY));
                --i;
            }
        }

        // Render the item name.
        return fontRenderer.drawWithShadow(matrices, text, x, y - tooltipOffset - screenBorder, color);
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
            generateTooltipsFromEnchantMap(EnchantmentHelper.get(currentStack), result);
            //If the item is a potion, retrieve the potion effects.
        } else if (item instanceof PotionItem) {
            List<Text> generated = Lists.newArrayList();
            // Lingering Potion has its own multiplier of duration, and it is hardcoded.
            item.appendTooltip(currentStack, null, generated, TooltipContext.BASIC);
            generateTooltipsForPotion(generated, result);
        } else if(item.toString().contains("shulker_box")){
            NbtCompound compoundTag = currentStack.getSubNbt("BlockEntityTag");
            if(compoundTag != null && compoundTag.contains("Items", 9)){
                generateTooltipsFromShulkerBox(compoundTag, result);
            }
        } else if(item instanceof BundleItem){
            if(currentStack.getTooltipData().isPresent() && currentStack.isOf(Items.BUNDLE)){
                generateTooltipsFromContainer(((BundleTooltipData)currentStack.getTooltipData().get()).getInventory(), result);
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
    /**
     * Gets a tooltip list from the given shulkerBox compound tag.
     *
     * @param compoundTag compoundTag with item information.
     * @param instance Where the list of {@link Tooltip} is stored.
     */
    private static void generateTooltipsFromShulkerBox(NbtCompound compoundTag, List<Tooltip> instance){
        DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
        Inventories.readNbt(compoundTag, items);
        generateTooltipsFromContainer(items, instance);
    }

    private static void generateTooltipsFromContainer(List<ItemStack> items, List<Tooltip> instance){
        for(ItemStack item : items){
            if(!item.isEmpty())
                instance.add(new ContainerTooltip(item));
        }
    }

    /**
     * Gets a tooltip list from the given enchantment map.
     *
     * @param enchantments enchantment map of an item.
     * @param instance Where the list of {@link Tooltip} is stored.
     */
    private static void generateTooltipsFromEnchantMap(Map<Enchantment, Integer> enchantments, List<Tooltip> instance) {
        enchantments.forEach((enchantment, value) -> instance.add(new EnchantmentTooltip(enchantment,value)));
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

    private void renderBackground(MatrixStack matrices, float y, int screenBorder, int tooltipOffset, int maxLength) {
        MinecraftClient client = MinecraftClient.getInstance();
        int background = MathHelper.ceil((255.0D * MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue()))<<24;
        DrawableHelper.fill(matrices, MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-3),MathHelper.ceil(y - tooltipOffset -5- screenBorder), MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+1),MathHelper.ceil(y - tooltipOffset -4- screenBorder),background);
        DrawableHelper.fill(matrices,MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-3),MathHelper.ceil(y+12-screenBorder), MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+1),MathHelper.ceil(y+13-screenBorder),background);
        DrawableHelper.fill(matrices,MathHelper.ceil((client.getWindow().getScaledWidth()-maxLength)/2f-4), MathHelper.ceil(y - tooltipOffset -4- screenBorder),MathHelper.ceil((client.getWindow().getScaledWidth()+maxLength)/2f+2), MathHelper.ceil(y+12-screenBorder),background);
    }

    /**
     * Renders an item tooltip with the given text and height in screen.
     */
    private void renderTooltip(TextRenderer fontRenderer, MatrixStack matrices, float y, int color, Text text) {
        int enchantX = (MinecraftClient.getInstance().getWindow().getScaledWidth() - fontRenderer.getWidth(text)) / 2;
        fontRenderer.drawWithShadow(matrices, text, enchantX, y, color);
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
