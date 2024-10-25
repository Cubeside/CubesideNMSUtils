package de.cubeside.nmsutils.paper1_21_3;

import de.cubeside.nmsutils.BlockUtils;
import de.cubeside.nmsutils.NMSUtils;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockUtilsImpl implements BlockUtils {
    private final NMSUtilsImpl nmsUtils;

    private Field vaultRewardedPlayersField;
    private Field trialSpawnerDataCooldownEndsAtField;

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
        if (!(blockEntity instanceof VaultBlockEntity vaultBlock)) {
            throw new IllegalArgumentException("This block is not a vault");
        }
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

    @Override
    public void setTrialSpawnerCooldown(Block block, int ticks) {
        CraftBlock craftBlock = ((CraftBlock) block);
        CraftWorld world = ((CraftWorld) block.getWorld());

        BlockEntity blockEntity = world.getHandle().getBlockEntity(craftBlock.getPosition());
        if (!(blockEntity instanceof TrialSpawnerBlockEntity trialSpawnerBlock) || !(block.getBlockData() instanceof TrialSpawner spawnerData)) {
            throw new IllegalArgumentException("This block is not a trial spawner");
        }
        if (trialSpawnerDataCooldownEndsAtField == null) {
            // first long field
            for (Field field : TrialSpawnerData.class.getDeclaredFields()) {
                if (long.class.isAssignableFrom(field.getType())) {
                    trialSpawnerDataCooldownEndsAtField = field;
                    field.setAccessible(true);
                    break;
                }
            }
            if (trialSpawnerDataCooldownEndsAtField == null) {
                throw new IllegalStateException("trialSpawnerDataCooldownEndsAtField not found!");
            }
        }
        trialSpawnerBlock.trialSpawner.getData().reset(trialSpawnerBlock.trialSpawner);
        long cooldownEnd = world.getHandle().getGameTime() + ticks;
        try {
            trialSpawnerDataCooldownEndsAtField.set(trialSpawnerBlock.trialSpawner.getData(), cooldownEnd);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Could not access trialSpawnerData cooldownEndsAt field", e);
        }

        spawnerData.setTrialSpawnerState(TrialSpawner.State.COOLDOWN);
        block.setBlockData(spawnerData);
    }

    @Override
    public int getNetworkBlockDataId(BlockData block) {
        return net.minecraft.world.level.block.Block.getId(((CraftBlockData) block).getState());
    }
}
