package de.cubeside.nmsutils.v1_18_R1;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import de.cubeside.nmsutils.EntityUtils;
import de.cubeside.nmsutils.NMSUtils;
import de.cubeside.nmsutils.util.ReobfHelper;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.logging.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftBat;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftMob;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPiglin;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftShulker;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftVex;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Vex;
import org.bukkit.util.Vector;

public class EntityUtilsImpl implements EntityUtils {
    private static final Field FIELD_BAT_TARGET = ReobfHelper.getFieldByMojangName(net.minecraft.world.entity.ambient.Bat.class, "targetPosition"); // "bW";
    private static final Field FIELD_ENTITY_TRACKER = ReobfHelper.getFieldByMojangName(Entity.class, "tracker"); // "tracker";

    private final NMSUtilsImpl nmsUtils;

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
            net.minecraft.world.entity.Mob h = ((CraftMob) mob).getHandle();
            h.goalSelector.addGoal(1, new FloatGoal(h));
        }
    }

    @Override
    public void addGoalLimitedStrollLand(Creature mob, double velocity, Function<Vector, Boolean> checkTargetFunction) {
        PathfinderMob h = ((CraftCreature) mob).getHandle();
        h.goalSelector.addGoal(7, new PathfinderGoalLimitedRandomStrollLand(h, velocity, checkTargetFunction));
    }

    @Override
    public int getShulkerOpenState(org.bukkit.entity.Entity shulker) {
        try {
            if (shulker.getType() != EntityType.SHULKER) {
                return 0;
            }
            CraftShulker cs = (CraftShulker) shulker;
            return cs.getHandle().getRawPeekAmount();
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
            cs.getHandle().setRawPeekAmount(state);
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
            return cs.getHandle().isDancing();
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
            cs.getHandle().setDancing(dancing);
        } catch (Exception e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not set piglin dancing state", e);
        }
    }

    @Override
    public void sendEntityPositionUpdate(org.bukkit.entity.Entity entity) {
        Entity handle = ((CraftEntity) entity).getHandle();
        try {
            ChunkMap.TrackedEntity ete = (ChunkMap.TrackedEntity) FIELD_ENTITY_TRACKER.get(handle);
            if (ete != null) {
                ClientboundTeleportEntityPacket positionPacket = new ClientboundTeleportEntityPacket(handle);
                ete.seenBy.stream().forEach(viewer -> {
                    viewer.send(positionPacket);
                });
            }
        } catch (ReflectiveOperationException e) {
            nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "Could not send teleport packet", e);
        }
    }

    @Override
    public void moveEntity(org.bukkit.entity.Entity e, double x, double y, double z) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.move(MoverType.SELF, new Vec3(x, y, z));
    }

    @Override
    public void moveEntity(org.bukkit.entity.Entity e, Vector v) {
        moveEntity(e, v.getX(), v.getY(), v.getZ());
    }

    @Override
    public void setEntityHeadRotation(org.bukkit.entity.Entity e, float headRotation) {
        Entity handle = ((CraftEntity) e).getHandle();
        if (handle instanceof LivingEntity) {
            // required for goats
            ((LivingEntity) handle).yHeadRot = headRotation;
        } else {
            handle.setYHeadRot(headRotation);
        }
    }

    @Override
    public float getEntityHeadRotation(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.getYHeadRot();
    }

    @Override
    public void setEntityYaw(org.bukkit.entity.Entity e, float yaw) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.setYRot(yaw);
    }

    @Override
    public float getEntityYaw(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.getYRot();
    }

    @Override
    public void setEntityPitch(org.bukkit.entity.Entity e, float pitch) {
        Entity handle = ((CraftEntity) e).getHandle();
        handle.setXRot(pitch);
    }

    @Override
    public float getEntityPitch(org.bukkit.entity.Entity e) {
        Entity handle = ((CraftEntity) e).getHandle();
        return handle.getXRot();
    }

    @Override
    public void setEntityNavigationTarget(org.bukkit.entity.Entity entity, Location target, double speed) {
        if (entity instanceof Bat) {
            try {
                FIELD_BAT_TARGET.set(((CraftBat) entity).getHandle(), new BlockPos(target.getX(), target.getY(), target.getZ()));
            } catch (ReflectiveOperationException e) {
                nmsUtils.getPlugin().getLogger().log(Level.SEVERE, "could not set field", e);
            }
        } else if (entity instanceof Vex) {
            net.minecraft.world.entity.monster.Vex entityVex = ((CraftVex) entity).getHandle();
            entityVex.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), speed);
            if (entityVex.getTarget() == null) {
                entityVex.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ(), 180, 20);
            }
        } else {
            ((CraftCreature) entity).getHandle().getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speed);
        }
    }

    @Override
    public void setEntityLeftHanded(org.bukkit.entity.Entity ent, boolean left) {
        Entity nmsEntity = ((CraftEntity) ent).getHandle();
        if (nmsEntity instanceof net.minecraft.world.entity.Mob mob) {
            mob.setLeftHanded(left);
        }
    }

    @Override
    public boolean isEntityLeftHanded(org.bukkit.entity.Entity ent) {
        Entity nmsEntity = ((CraftEntity) ent).getHandle();
        if (nmsEntity instanceof net.minecraft.world.entity.Mob mob) {
            return mob.isLeftHanded();
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
        return nmsEntity.noPhysics;
    }

    @Override
    public void setEntityNoClip(org.bukkit.entity.Entity entity, boolean noClip) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.noPhysics = noClip;
    }

    @Override
    public boolean areChunkEntitiesLoaded(Chunk c) {
        return ((CraftWorld) c.getWorld()).getHandle().areEntitiesLoaded(ChunkPos.asLong(c.getX(), c.getZ()));
    }

    @Override
    public void loadChunkEntities(Chunk c) {
        int x = c.getX();
        int z = c.getZ();
        World world = c.getWorld();
        if (!world.isChunkLoaded(x, z)) {
            world.getChunkAt(x, z);
        }

        if (areChunkEntitiesLoaded(c)) {
            return;
        }
        ServerLevel serverLevel = ((CraftWorld) world).getHandle();
        serverLevel.entityManager.updateChunkStatus(new ChunkPos(x, z), Visibility.TRACKED);
        // now wait until entities are loaded
        if (!areChunkEntitiesLoaded(c)) {
            serverLevel.getServer().managedBlock(() -> {
                if (areChunkEntitiesLoaded(c)) {
                    return true;
                }
                // process chunk inbox
                serverLevel.entityManager.tick();
                return areChunkEntitiesLoaded(c);
            });
        }
    }

    @Override
    public void setOnGround(org.bukkit.entity.Entity entity, boolean onGround) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.setOnGround(onGround);
    }
}
