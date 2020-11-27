package de.cubeside.nmsutils.v1_16_R3;

import java.util.function.Function;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.util.Vector;

public class PathfinderGoalLimitedRandomStrollLand extends PathfinderGoalRandomStrollLand {

    private Function<Vector, Boolean> checkVectorFunction;
    // private EntityCreature entity;

    public PathfinderGoalLimitedRandomStrollLand(EntityCreature entity, double velocity, Function<Vector, Boolean> checkVectorFunction) {
        super(entity, velocity);
        // this.entity = entity;
        this.checkVectorFunction = checkVectorFunction;
    }

    @Override
    protected Vec3D g() {
        Vec3D base = super.g();
        if (base == null || checkVectorFunction.apply(CraftVector.toBukkit(base)) != Boolean.TRUE) {
            base = null;
        }
        return base;
    }
}
