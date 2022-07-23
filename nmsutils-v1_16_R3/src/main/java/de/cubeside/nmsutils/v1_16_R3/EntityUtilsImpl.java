package de.cubeside.nmsutils.v1_16_R3;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import de.cubeside.nmsutils.EntityUtils;
import de.cubeside.nmsutils.NMSUtils;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.logging.Level;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityBat;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.EntityVex;
import net.minecraft.server.v1_16_R3.EnumMoveType;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftMob;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPiglin;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftShulker;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftVex;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Vex;
import org.bukkit.util.Vector;

public class EntityUtilsImpl implements EntityUtils {
    private static final String FIELD_BAT_TARGET_NAME = "d";

    private final NMSUtilsImpl nmsUtils;

    private Field fieldEntityTracker;
    private Field fieldBatTarget;

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
    public void addGoalFloat(Mob mob) {
        if (!Bukkit.getMobGoals().hasGoal(mob, VanillaGoal.FLOAT)) {
            EntityInsentient h = ((CraftMob) mob).getHandle();
            h.goalSelector.addGoal(1, new PathfinderGoalFloat(h));
        }
    }

    @Override
    public void addGoalLimitedStrollLand(Creature mob, double velocity, Function<Vector, Boolean> checkTargetFunction) {
        EntityCreature h = ((CraftCreature) mob).getHandle();
        h.goalSelector.addGoal(7, new PathfinderGoalLimitedRandomStrollLand(h, velocity, checkTargetFunction));
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
    public void setEntityHeadRotation(org.bukkit.entity.Entity e, float headRotation) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.setHeadRotation(headRotation);
    }

    @Override
    public float getEntityHeadRotation(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.getHeadRotation();
    }

    @Override
    public void setEntityYaw(org.bukkit.entity.Entity e, float yaw) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.yaw = yaw;
    }

    @Override
    public float getEntityYaw(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.yaw;
    }

    @Override
    public void setEntityPitch(org.bukkit.entity.Entity e, float pitch) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.pitch = pitch;
    }

    @Override
    public float getEntityPitch(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.yaw;
    }

    @Override
    public void setEntityNavigationTarget(org.bukkit.entity.Entity entity, Location target, double speed) {
        if (entity instanceof Bat) {
            try {
                if (fieldBatTarget == null) {
                    fieldBatTarget = EntityBat.class.getDeclaredField(FIELD_BAT_TARGET_NAME);
                    fieldBatTarget.setAccessible(true);
                }
                fieldBatTarget.set(((CraftBat) entity).getHandle(), new BlockPosition(target.getX(), target.getY(), target.getZ()));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "could not set field", e);
            }
        } else if (entity instanceof Vex) {
            EntityVex entityVex = ((CraftVex) entity).getHandle();
            entityVex.getControllerMove().a(target.getX(), target.getY(), target.getZ(), speed);
            if (entityVex.getGoalTarget() == null) {
                entityVex.getControllerLook().a(target.getX(), target.getY(), target.getZ(), 180, 20);
            }
        } else {
            ((CraftCreature) entity).getHandle().getNavigation().a(target.getX(), target.getY(), target.getZ(), speed);
        }
    }

    @Override
    public void setEntityLeftHanded(org.bukkit.entity.Entity ent, boolean left) {
        Entity nmsEntity = ((CraftEntity) ent).getHandle();
        if (nmsEntity instanceof EntityInsentient) {
            ((EntityInsentient) nmsEntity).setLeftHanded(left);
        }
    }

    @Override
    public boolean isEntityLeftHanded(org.bukkit.entity.Entity ent) {
        Entity nmsEntity = ((CraftEntity) ent).getHandle();
        if (nmsEntity instanceof EntityInsentient) {
            return ((EntityInsentient) nmsEntity).isLeftHanded();
        }
        return false;
    }

    @Override
    public boolean isEntityInvisible(org.bukkit.entity.Entity entity) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        return nmsEntity.isInvisible();
    }

    @Override
    public void setEntityInvisible(org.bukkit.entity.Entity entity, boolean invisible) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.setInvisible(invisible);
    }

    @Override
    public boolean hasEntityNoClip(org.bukkit.entity.Entity entity) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        return nmsEntity.noclip;
    }

    @Override
    public void setEntityNoClip(org.bukkit.entity.Entity entity, boolean noClip) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.noclip = noClip;
    }

    @Override
    public void setOnGround(org.bukkit.entity.Entity entity, boolean onGround) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.setOnGround(onGround);
    }

    @Override
    public org.bukkit.entity.Entity getEntityById(World world, int id) {
        Entity entity = ((CraftWorld) world).getHandle().getEntity(id);
        return entity == null ? null : entity.getBukkitEntity();
    }

    @Override
    public void setPose(org.bukkit.entity.Entity entity, Pose pose) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.setPose(EntityPose.values()[pose.ordinal()]);
    }
}
