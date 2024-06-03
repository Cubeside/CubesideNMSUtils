package de.cubeside.nmsutils.paper1_20_6;

import de.cubeside.nmsutils.BlockUtils;
import de.cubeside.nmsutils.NMSUtils;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockUtilsImpl implements BlockUtils {
    private final NMSUtilsImpl nmsUtils;

    private Field vaultRewardedPlayersField;

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

    @Override
    public Set<UUID> getVaultRewardedPlayers(Block block) {
        CraftBlock craftBlock = ((CraftBlock) block);
        CraftWorld world = ((CraftWorld) block.getWorld());

        BlockEntity blockEntity = world.getHandle().getBlockEntity(craftBlock.getPosition());
        if (blockEntity instanceof VaultBlockEntity vaultBlock) {
            VaultServerData serverData = vaultBlock.getServerData();
            if (vaultRewardedPlayersField == null) {
                for (Field field : serverData.getClass().getDeclaredFields()) {
                    if (Set.class.isAssignableFrom(field.getType())) {
                        vaultRewardedPlayersField = field;
                        field.setAccessible(true);
                    }
                }
                if (vaultRewardedPlayersField == null) {
                    throw new IllegalStateException("vaultRewardedPlayersField not found!");
                }
            }
            try {
                @SuppressWarnings("unchecked")
                Set<UUID> playersSet = (Set<UUID>) vaultRewardedPlayersField.get(serverData);
                return Collections.unmodifiableSet(playersSet);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException("Could not access vault rewardedPlayers field", e);
            }
        }
        throw new IllegalArgumentException("This block is not a vault");
    }
}
