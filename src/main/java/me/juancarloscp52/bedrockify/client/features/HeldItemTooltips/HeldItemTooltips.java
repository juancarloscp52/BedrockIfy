package me.juancarloscp52.bedrockify.client.features.HeldItemTooltips;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.Tooltip.EnchantmentTooltip;
import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.Tooltip.PotionTooltip;
import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.Tooltip.ShulkerBoxTooltip;
import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.Tooltip.Tooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HeldItemTooltips {

    public int drawItemWithCustomTooltips(TextRenderer fontRenderer, MatrixStack matrices, Text text, float x, float y, int color, ItemStack currentStack) {
        int screenBorder = Bedrockify.getInstance().settings.getScreenSafeArea();
        // Get the current held item tooltips.
        List<Tooltip> tooltips = getTooltips(currentStack);
        int tooltipOffset = 0;
        BedrockifySettings settings = Bedrockify.getInstance().settings;

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
                    int maxLength = getMaxTooltipLength(tooltips,fontRenderer,currentStack);
                    renderBackground(matrices, y, screenBorder, tooltipOffset, maxLength);
                }


                for (Tooltip elem : tooltips) {
                    // Prevent from drawing more than 4 tooltips.
                    if (count > 3)
                        break;
                    // Render the tooltip.
                    renderTooltip(fontRenderer, matrices, y - screenBorder - (12 * count), color, elem.getTooltipText().formatted(Formatting.GRAY));
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
     * Gets a List with the given item tooltips.
     * @param currentStack Current item stack of the player.
     * @return List with the tooltip information.
     */
    public List<Tooltip> getTooltips(ItemStack currentStack) {
        //If the item is a enchanted book, retrieve the enchantments.
        if (currentStack.getItem() == Items.ENCHANTED_BOOK || currentStack.hasEnchantments()) {
            return getTooltipsFromEnchantMap(EnchantmentHelper.get(currentStack));
            //If the item is a potion, retrieve the potion effects.
        } else if (currentStack.getItem() instanceof PotionItem) {
            return getTooltipsFromEffectList(PotionUtil.getPotionEffects(currentStack));
        } else if(currentStack.getItem().toString().contains("shulker_box")){
            CompoundTag compoundTag = currentStack.getSubTag("BlockEntityTag");
            if(compoundTag != null && compoundTag.contains("Items", 9)){
                return getTooltipsFromShulkerBox(compoundTag);
            }
        }
        return null;
    }

    /**
     * Checks if the tooltips of two items are equal.
     */
    public boolean equals(ItemStack item1, ItemStack item2){
        List<Tooltip> itemTooltips1 = getTooltips(item1);
        List<Tooltip> itemTooltips2 = getTooltips(item2);
        if(itemTooltips1==null && itemTooltips2 == null)
            return true;
        else if (itemTooltips1== null || itemTooltips2==null)
            return false;

        if(itemTooltips1.size()!=itemTooltips2.size())
            return false;

        Iterator<Tooltip> iterator1 = itemTooltips1.iterator();
        Iterator<Tooltip> iterator2 = itemTooltips2.iterator();
        while (iterator1.hasNext()){
            Tooltip tooltip1 = iterator1.next();
            Tooltip tooltip2 = iterator2.next();
            if(!tooltip1.getTooltipText().equals(tooltip2.getTooltipText()))
                return false;
        }
        return true;
    }
    /**
     * Gets a tooltip list from the given shulkerBox compound tag.
     *
     * @param compoundTag compoundTag with item information.
     * @return Tooltip list.
     */
    private List<Tooltip> getTooltipsFromShulkerBox(CompoundTag compoundTag){
        ArrayList<Tooltip> shulkerTooltips = new  ArrayList<Tooltip>();
        DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
        Inventories.fromTag(compoundTag, items);
        for(ItemStack item : items){
            if(!item.isEmpty())
            shulkerTooltips.add(new ShulkerBoxTooltip(item));
        }
        return shulkerTooltips;
    }

    /**
     * Gets a tooltip list from the given enchantment map.
     *
     * @param enchantments enchantment map of an item.
     * @return Tooltip list.
     */
    private List<Tooltip> getTooltipsFromEnchantMap(Map<Enchantment, Integer> enchantments) {
        ArrayList<Tooltip> enchantmentTooltips = new ArrayList<>();
        enchantments.forEach((enchantment, value) -> enchantmentTooltips.add(new EnchantmentTooltip(enchantment,value)));
        return enchantmentTooltips;
    }

    /**
     * Gets a tooltip list from the given {@link StatusEffectInstance} list.
     *
     * @param effects effects list of an item.
     * @return Tooltip list.
     */
    private List<Tooltip> getTooltipsFromEffectList(List<StatusEffectInstance> effects) {
        ArrayList<Tooltip> effectTooltips = new ArrayList<>();
        effects.forEach((current) -> effectTooltips.add(new PotionTooltip(current)));
        return effectTooltips;
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

    private int getMaxTooltipLength(List<Tooltip> tooltips, TextRenderer textRenderer, ItemStack itemStack){
        int count =0;
        int maxLength=textRenderer.getWidth(itemStack.getName());
        for(Tooltip elem : tooltips){
            int tipLength = textRenderer.getWidth(elem.getTooltipText());
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
}
