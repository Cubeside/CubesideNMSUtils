package de.cubeside.nmsutils;

import org.bukkit.entity.Entity;
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

    void setEntityYaw(Entity e, float yaw);

}
