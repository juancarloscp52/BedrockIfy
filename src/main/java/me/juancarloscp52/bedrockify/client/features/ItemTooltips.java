package me.juancarloscp52.bedrockify.client.features;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifySettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTooltips {

    public int drawItemWithCustomTooltips(TextRenderer fontRenderer, MatrixStack matrices, StringRenderable text, float x, float y, int color, ItemStack currentStack) {
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        // Get the current held item tooltips.
        Map<String, Integer[]> tooltips = getTooltips(currentStack);
        int tooltipOffset = 0;
        BedrockifySettings settings = BedrockifyClient.getInstance().settings;

        // Draw item tooltips if the option is enabled.
        if(settings.getHeldItemTooltip()>0) {
            if (tooltips != null) {
                //Compute the max tooltip offset (used for the item name).
                int count = 0;
                // Limit the maximum number of shown tooltips to 4.
                boolean showMoreTooltip = false;

                if (tooltips.size() > 4) {
                    showMoreTooltip = true;
                    tooltipOffset = 12 * 4;
                    count++;
                } else
                    tooltipOffset = tooltips.size() * 12;

                //Render background behind tooltip if enabled.
                if(settings.getHeldItemTooltip()==2){
                    int maxLength = getMaxTooltipLength(tooltips,fontRenderer,currentStack.getTranslationKey());
                    renderBackground(matrices, y, screenBorder, tooltipOffset, maxLength);
                }


                for (Map.Entry<String, Integer[]> elem : tooltips.entrySet()) {
                    // Prevent from drawing more than 4 tooltips.
                    if (count > 3)
                        break;
                    // Render the tooltip.
                    renderTooltip(fontRenderer, matrices, y - screenBorder - (12 * count), color, getTooltip(elem).formatted(Formatting.GRAY));
                    count++;
                }

                // show the "and x more..." tooltip if the item has more than 4 tooltips.
                if(showMoreTooltip)
                    renderTooltip(fontRenderer, matrices, y - screenBorder, color, new TranslatableText("container.shulkerBox.more", tooltips.size() - 4).formatted(Formatting.GRAY));

            }else if(settings.getHeldItemTooltip()==2){
                // draw the background
                renderBackground(matrices,y,screenBorder,tooltipOffset,fontRenderer.getWidth(text));
            }
        }

        // Render the item name.
        return fontRenderer.drawWithShadow(matrices, text, x, y - tooltipOffset - screenBorder, color);
    }

    /**
     * Gets a Map with the given item tooltips.
     * <p>The map key is the Translation key for the tooltip.</p>
     * <p>The map value is an integer array containing the Level/Power of the tooltip and the duration (if it has one).</p>
     *
     * @param currentStack Current item stack of the player.
     * @return Map with the tooltip information.
     */
    private Map<String, Integer[]> getTooltips(ItemStack currentStack) {
        //If the item is a enchanted book, retrieve the enchantments.
        if (currentStack.getItem() == Items.ENCHANTED_BOOK || currentStack.hasEnchantments()) {
            return enchantmentMapToStringMap(EnchantmentHelper.get(currentStack));
            //If the item is a potion, retrieve the potion effects.
        } else if (currentStack.getItem() instanceof PotionItem) {
            return effectListToStringMap(PotionUtil.getPotionEffects(currentStack));
        }
        return null;
    }

    /**
     * Gets a tooltip map from the given enchantment map.
     *
     * @param enchantments enchantment map of an item.
     * @return Tooltip map.
     */
    private Map<String, Integer[]> enchantmentMapToStringMap(Map<Enchantment, Integer> enchantments) {
        HashMap<String, Integer[]> stringHashMap = new HashMap<>();
        enchantments.forEach((enchantment, value) -> stringHashMap.put(enchantment.getTranslationKey(), new Integer[]{value}));
        return stringHashMap;
    }

    /**
     * Gets a tooltip map from the given {@link StatusEffectInstance} list.
     *
     * @param effects effects list of an item.
     * @return Tooltip map.
     */
    private Map<String, Integer[]> effectListToStringMap(List<StatusEffectInstance> effects) {
        HashMap<String, Integer[]> stringHashMap = new HashMap<>();
        effects.forEach((current) -> stringHashMap.put(current.getTranslationKey(), new Integer[]{current.getAmplifier(), current.getDuration()}));
        return stringHashMap;
    }

    private void renderBackground(MatrixStack matrices, float y, int screenBorder, int tooltipOffset, int maxLength) {
        MinecraftClient client = MinecraftClient.getInstance();
        int background = MathHelper.ceil((255.0D * MinecraftClient.getInstance().options.textBackgroundOpacity))<<24;
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

    private int getMaxTooltipLength(Map<String,Integer[]> tooltips, TextRenderer textRenderer, String stackTranslationKey){
        int count =0;
        int maxLength=textRenderer.getWidth(new TranslatableText(stackTranslationKey));
        for(Map.Entry<String,Integer[]> elem : tooltips.entrySet()){
            int tipLength = textRenderer.getWidth(getTooltip(elem));
            if (count > 3)
                tipLength = textRenderer.getWidth(new TranslatableText("container.shulkerBox.more", tooltips.size() - 4));
            if(maxLength<tipLength)
                maxLength=tipLength;
            if ( count>3 )
                break ;
            count++ ;
        }
        return maxLength;
    }

    private TranslatableText getTooltip(Map.Entry<String,Integer[]> tooltipEntry){
        TranslatableText tooltip = new TranslatableText(tooltipEntry.getKey());
        // If the value has two items, set the potion potency and duration. Else, set the enchantment level.
        if (tooltipEntry.getValue().length > 1) {
            if (tooltipEntry.getValue()[0] > 0)
                tooltip.append(" ").append(new TranslatableText("potion.potency." + tooltipEntry.getValue()[0]));
            // Only show duration if it is more than 1 second.
            if (tooltipEntry.getValue()[1] >= 20)
                tooltip.append(" (" + ChatUtil.ticksToString(tooltipEntry.getValue()[1]) + ")");
        } else
            tooltip.append(" ").append(new TranslatableText("enchantment.level." + tooltipEntry.getValue()[0]));

        return tooltip;
    }
}
