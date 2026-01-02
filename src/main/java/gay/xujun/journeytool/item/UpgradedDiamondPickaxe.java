package gay.xujun.journeytool.item;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

public class UpgradedDiamondPickaxe extends PickaxeItem {

    public UpgradedDiamondPickaxe(Settings settings) {
        super(ToolMaterials.DIAMOND, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Range 3×3").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("Sneak to mine 1×1").formatted(Formatting.GRAY));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (world.isClient || !(miner instanceof PlayerEntity player)) {
            return super.postMine(stack, world, state, pos, miner);
        }

        if (player.isSneaking()) return super.postMine(stack, world, state, pos, miner);

        boolean vertical = player.getPitch() > 45 || player.getPitch() < -45;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos targetPos;
                if (vertical) {
                    targetPos = pos.add(x, 0, y);
                } else {
                    Direction dir = player.getHorizontalFacing();
                    targetPos = (dir == Direction.NORTH || dir == Direction.SOUTH)
                            ? pos.add(x, y, 0) : pos.add(0, y, x);
                }

                if (targetPos.equals(pos)) continue;
                BlockState targetState = world.getBlockState(targetPos);
                if (targetState.isAir() || targetState.getHardness(world, targetPos) < 0) continue;

                if (this.isCorrectForDrops(stack, targetState)) {
                    world.breakBlock(targetPos, true, player);
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }
}