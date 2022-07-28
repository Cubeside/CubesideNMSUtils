package de.cubeside.nmsutils.v1_19_R1_1;

import java.util.function.Function;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

public class PathfinderGoalLimitedRandomStrollLand extends RandomStrollGoal {

    private Function<Vector, Boolean> checkVectorFunction;
    // private EntityCreature entity;

    public PathfinderGoalLimitedRandomStrollLand(PathfinderMob entity, double velocity, Function<Vector, Boolean> checkVectorFunction) {
        super(entity, velocity, 1, false);
        // this.entity = entity;
        this.checkVectorFunction = checkVectorFunction;
    }

    @Override
    protected Vec3 getPosition() {
        Vec3 base = super.getPosition();
        if (base == null || checkVectorFunction.apply(org.bukkit.craftbukkit.v1_19_R1.util.CraftVector.toBukkit(base)) != Boolean.TRUE) {
            base = null;
        }
        return base;
    }
}
