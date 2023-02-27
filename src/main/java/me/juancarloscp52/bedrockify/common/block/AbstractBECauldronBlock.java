package me.juancarloscp52.bedrockify.common.block;

import com.google.common.collect.Maps;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractBECauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {
    private final Map<Item, CauldronBehavior> behaviorMap;

    public AbstractBECauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, precipitation -> false, Maps.newHashMap());
        this.behaviorMap = behaviorMap;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final ItemStack itemStack = player.getStackInHand(hand);
        final CauldronBehavior behavior;
        if (Bedrockify.getInstance().settings.bedrockCauldron) {
            behavior = this.behaviorMap.get(itemStack.getItem());
        } else {
            behavior = CauldronBehavior.WATER_CAULDRON_BEHAVIOR.get(itemStack.getItem());
        }

        return (behavior == null) ? ActionResult.PASS : behavior.interact(state, world, pos, player, hand, itemStack);
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
    }

    /**
     * Allows to keep water state.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WaterCauldronBlockEntity(pos, state);
    }
}
