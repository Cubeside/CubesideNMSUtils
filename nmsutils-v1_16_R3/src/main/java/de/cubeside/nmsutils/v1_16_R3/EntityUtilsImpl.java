package de.cubeside.nmsutils.v1_16_R3;

import de.cubeside.nmsutils.EntityUtils;
import de.cubeside.nmsutils.NMSUtils;
import java.lang.reflect.Field;
import java.util.logging.Level;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EnumMoveType;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPiglin;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftShulker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

public class EntityUtilsImpl implements EntityUtils {

    private final NMSUtilsImpl nmsUtils;

    private Field fieldEntityTracker;

    public EntityUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public void clearAI(org.bukkit.entity.Entity entity) {
        if (entity instanceof Mob) {
            nmsUtils.getPlugin().getServer().getMobGoals().removeAllGoals((Mob) entity);
        }
    }

    @Override
    public int getShulkerOpenState(org.bukkit.entity.Entity shulker) {
        try {
            if (shulker.getType() != EntityType.SHULKER) {
                return 0;
            }
            CraftShulker cs = (CraftShulker) shulker;
            return cs.getHandle().eN();
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not get shulker open state", e);
        }
        return 0;
    }

    @Override
    public void setShulkerOpenState(org.bukkit.entity.Entity shulker, int state) {
        try {
            if (shulker.getType() != EntityType.SHULKER) {
                return;
            }
            state = Math.max(0, Math.min(Byte.MAX_VALUE, state));
            CraftShulker cs = (CraftShulker) shulker;
            cs.getHandle().a(state);
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not set shulker open state", e);
        }
    }

    @Override
    public boolean isPiglinDancing(org.bukkit.entity.Entity piglin) {
        try {
            if (piglin.getType() != EntityType.PIGLIN) {
                return false;
            }
            CraftPiglin cs = (CraftPiglin) piglin;
            return cs.getHandle().eU();
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not get piglin dancing state", e);
        }
        return false;
    }

    @Override
    public void setPiglinDancing(org.bukkit.entity.Entity piglin, boolean dancing) {
        try {
            if (piglin.getType() != EntityType.PIGLIN) {
                return;
            }
            CraftPiglin cs = (CraftPiglin) piglin;
            cs.getHandle().u(dancing);
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not set piglin dancing state", e);
        }
    }

    @Override
    public void sendEntityPositionUpdate(org.bukkit.entity.Entity entity) {
        Entity handle = ((CraftEntity) entity).getHandle();
        if (fieldEntityTracker == null) {
            try {
                fieldEntityTracker = Entity.class.getDeclaredField("tracker");
                fieldEntityTracker.setAccessible(true);
            } catch (Exception e) {
                nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not get tracker field", e);
            }
        }
        try {
            PlayerChunkMap.EntityTracker ete = (PlayerChunkMap.EntityTracker) fieldEntityTracker.get(handle);
            if (ete != null) {
                PacketPlayOutEntityTeleport positionPacket = new PacketPlayOutEntityTeleport(handle);
                ete.trackedPlayers.stream().forEach(viewer -> {
                    viewer.playerConnection.sendPacket(positionPacket);
                });
            }
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not send teleport packet", e);
        }
    }

    @Override
    public void moveEntity(org.bukkit.entity.Entity e, double x, double y, double z) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.move(EnumMoveType.SELF, new Vec3D(x, y, z));
    }

    @Override
    public void moveEntity(org.bukkit.entity.Entity e, Vector v) {
        moveEntity(e, v.getX(), v.getY(), v.getZ());
    }

    @Override
    public void setEntityYaw(org.bukkit.entity.Entity e, float yaw) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.yaw = yaw;
    }
}
