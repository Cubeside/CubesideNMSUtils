package de.cubeside.nmsutils.paper1_20_6;

import de.cubeside.nmsutils.BlockUtils;
import de.cubeside.nmsutils.NMSUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockUtilsImpl implements BlockUtils {
    private final NMSUtilsImpl nmsUtils;

    public BlockUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public boolean useBlock(Block block, Player player) {
        CraftBlock craftBlock = ((CraftBlock) block);
        CraftWorld world = ((CraftWorld) block.getWorld());

        BlockHitResult blockHitResult = new BlockHitResult(craftBlock.getPosition().getCenter(), Direction.UP, craftBlock.getPosition(), false);
        return craftBlock.getNMS().useWithoutItem(world.getHandle(), ((CraftPlayer) player).getHandle(), blockHitResult).consumesAction();
    }
}
