package de.cubeside.nmsutils;

import java.util.function.Function;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
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
     */
    default void loadChunkEntities(Chunk c) {
        c.getWorld().loadChunk(c); // in minecraft pre 1.17 this will load the entities
    }
}
