package gay.xujun.journeytool.item;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.ItemStack;
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

import net.minecraft.item.tooltip.TooltipType;

import java.util.List;

public class UpgradedDiamondPickaxe extends PickaxeItem {

    public UpgradedDiamondPickaxe(Settings settings) {
        super(ToolMaterials.DIAMOND, settings);
    }

    /* ===== ツールチップ（説明文） ===== */
    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(
            ItemStack stack,
            TooltipContext context,
            List<Text> tooltip,
            TooltipType type
    ) {
        tooltip.add(
                Text.literal("Range 3×3")
                        .formatted(Formatting.GOLD)
        );

        tooltip.add(
                Text.literal("Sneak to mine 1×1")
                        .formatted(Formatting.GRAY)
        );
    }


    /* ===== 3×3 採掘処理 ===== */
    @Override
    public boolean postMine(
            ItemStack stack,
            World world,
            BlockState state,
            BlockPos pos,
            LivingEntity miner
    ) {
        if (world.isClient) {
            return super.postMine(stack, world, state, pos, miner);
        }

        if (!(miner instanceof PlayerEntity player)) {
            return super.postMine(stack, world, state, pos, miner);
        }

        // Shift中は1×1
        if (player.isSneaking()) {
            return super.postMine(stack, world, state, pos, miner);
        }

        float pitch = player.getPitch();
        boolean vertical = pitch > 45 || pitch < -45;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {

                BlockPos targetPos;

                if (vertical) {
                    // 上下向き → 床・天井 3×3
                    targetPos = pos.add(x, 0, y);
                } else {
                    // 横向き → 壁 3×3
                    Direction dir = player.getHorizontalFacing();
                    if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                        targetPos = pos.add(x, y, 0);
                    } else {
                        targetPos = pos.add(0, y, x);
                    }
                }

                if (targetPos.equals(pos)) continue;

                BlockState targetState = world.getBlockState(targetPos);
                if (targetState.isAir()) continue;
                if (targetState.getHardness(world, targetPos) < 0) continue;

                world.breakBlock(targetPos, true, player);
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }
}