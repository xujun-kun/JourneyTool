package gay.xujun.journeytool;

import gay.xujun.journeytool.item.UpgradedDiamondPickaxe;
import gay.xujun.journeytool.JourneyTool;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item UPGRADED_DIAMOND_PICKAXE = register(
            "upgraded_diamond_pickaxe",
            new UpgradedDiamondPickaxe(new Item.Settings())
    );

    private static Item register(String id, Item item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(JourneyTool.MOD_ID, id),
                item
        );
    }

    public static void registerModItems() {
        JourneyTool.LOGGER.info("诶 醒醒醒醒 该起床了");
    }
}
