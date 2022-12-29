package de.cubeside.nmsutils;

import java.util.function.Function;
import java.util.logging.Level;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

public interface EntityUtils {
    public NMSUtils getNMSUtils();

    public void clearAI(Entity entity);

    public int getShulkerOpenState(Entity shulker);

    public void setShulkerOpenState(Entity shulker, int state);

    public boolean isPiglinDancing(Entity piglin);

    public void setPiglinDancing(Entity piglin, boolean dancing);

    public void sendEntityPositionUpdate(Entity entity);

    void moveEntity(Entity e, double x, double y, double z);

    void moveEntity(Entity e, Vector v);

    void setEntityHeadRotation(Entity e, float headRotation);

    float getEntityHeadRotation(Entity e);

    void setEntityYaw(Entity e, float yaw);

    float getEntityYaw(Entity e);

    void setEntityPitch(Entity e, float pitch);

    float getEntityPitch(Entity e);

    void setEntityNavigationTarget(Entity entity, Location target, double speed);

    void setEntityLeftHanded(Entity ent, boolean left);

    boolean isEntityLeftHanded(Entity ent);

    void addGoalFloat(Mob mob);

    void addGoalLimitedStrollLand(Creature mob, double velocity, Function<Vector, Boolean> checkTargetFunction);

    boolean isEntityInvisible(Entity entity);

    /**
     * Probably needs to be delayed by 1 or 2 ticks in order to work correctly.
     */
    void setEntityInvisible(Entity entity, boolean invisible);

    boolean hasEntityNoClip(Entity entity);

    /**
     * Changes whether or not the entity can go through walls.
     *
     * @param entity
     *            The entity
     * @param noClip
     *            true if entity should pass walls, false if not (default for most entities)
     */
    void setEntityNoClip(Entity entity, boolean noClip);

    /**
     * Checks if the entities are laoded for a chunk
     *
     * @param c
     *            The chunk
     * @return
     *         true if the entities are loaded
     */
    default boolean areChunkEntitiesLoaded(Chunk c) {
        return c.getWorld().isChunkLoaded(c);
    }

    /**
     * Loads the entities for a given chunk
     *
     * @param c
     *            The chunk
     * @deprecated Not possible to implement in 1.19.3
     */
    @Deprecated(forRemoval = true)
    default void loadChunkEntities(Chunk c) {
        c.getWorld().loadChunk(c); // in minecraft pre 1.17 this will load the entities
    }

    /**
     * Updates the on ground state of an entity
     *
     * @param ent
     *            the entity to modify
     * @param onGround
     *            the new on ground state
     */
    void setOnGround(Entity ent, boolean onGround);

    /**
     * Gets an entity that is loaded in some world by its internal id.
     *
     * @param world
     *            the world the entity is in
     * @param id
     *            the id of the entity
     * @return the entity or null if no entitiy with the given id is found in that world
     */
    Entity getEntityById(World world, int id);

    /**
     * Sets the pose of the Entity
     *
     * @param end
     *            The entity
     * @param pose
     *            The new pose of the entity
     */
    void setPose(Entity ent, Pose pose);

    /**
     * Sets the remaining anger time of a wolf
     *
     * @param entity
     *            a wolf
     * @param timeInTicks
     *            the remaining time after that the angry status is reset
     */
    void setWolfAngerTime(Wolf entity, int timeInTicks);

    /**
     * Checks wether a mob is currenty aggressive. This may change behaviour and optics.
     *
     * @param entity
     *            the mob to check
     * @return is this mob is agressive
     */
    boolean isAggressive(Mob entity);

    /**
     * Sets if a mob is aggressive. This may change behaviour and optics.
     *
     * @param entity
     *            the mob
     * @param aggressive
     *            the new aggressivity
     */
    void setAggressive(Mob entity, boolean aggressive);

    /**
     * Checks if raider is currently celebrating.
     *
     * @param entity
     *            the raider to check
     * @return is he is celebrating
     */
    boolean isCelebrating(Raider entity);

    /**
     * Sets is a raider is celebrating
     *
     * @param entity
     *            the raider
     * @param celebrating
     *            the new celebrating status
     */
    void setCelebrating(Raider entity, boolean celebrating);

    /**
     * Set how many ticks ago the last pose change of the camel was
     *
     * @param entity
     *            the camel
     * @param tick
     *            number of ticks since the last pose change
     */
    default void setCamelLastPoseChange(Camel entity, long tick) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
    }

    /**
     * Get how many ticks ago the last pose change of a camel happened
     *
     * @param entity
     *            the camel
     * @return
     *         number of ticks since the last pose change
     */
    default long getCamelLastPoseChange(Camel entity) {
        getNMSUtils().getPlugin().getLogger().log(Level.SEVERE, "Call to unimplemented method", new RuntimeException());
        return 0L;
    }
}
