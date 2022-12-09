package de.cubeside.nmsutils.v1_19_R2;

import de.cubeside.nmsutils.MiscUtils;
import de.cubeside.nmsutils.NMSUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftMagicNumbers;

public class MiscUtilsImpl implements MiscUtils {
    private final NMSUtilsImpl nmsUtils;

    private Field fieldBlockBehaviour_properties;
    private MaterialColor transparentColor;
    private Field fieldBlockStateBase_materialColor;

    public MiscUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public void setBlockMapColorTransparent(Material m) {
        if (fieldBlockBehaviour_properties == null) {
            Class<Properties> classBlockProperties = BlockBehaviour.Properties.class;
            Class<BlockBehaviour> classBlockBehaviour = BlockBehaviour.class;
            fieldBlockBehaviour_properties = null;
            for (Field f : classBlockBehaviour.getDeclaredFields()) {
                if (f.getType() == classBlockProperties) {
                    fieldBlockBehaviour_properties = f;
                    fieldBlockBehaviour_properties.setAccessible(true);
                }
            }
            if (fieldBlockBehaviour_properties == null) {
                throw new IllegalStateException("Could not find block properties field!");
            }

            Class<BlockStateBase> classBlockStateBase = BlockBehaviour.BlockStateBase.class;
            for (Field f : classBlockStateBase.getDeclaredFields()) {
                if (f.getType() == MaterialColor.class) {
                    fieldBlockStateBase_materialColor = f;
                    fieldBlockStateBase_materialColor.setAccessible(true);
                }
            }
            if (fieldBlockStateBase_materialColor == null) {
                throw new IllegalStateException("Could not find BlockStateBase materialColor field!");
            }

            try {
                Constructor<MaterialColor> constructorMaterialColor = MaterialColor.class.getDeclaredConstructor(int.class, int.class);
                constructorMaterialColor.setAccessible(true);
                transparentColor = constructorMaterialColor.newInstance(0, 0);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not create custom transparent MaterialColor!");
            }
        }

        if (!m.isBlock()) {
            throw new IllegalArgumentException("Material must be a block");
        }
        Block b = CraftMagicNumbers.getBlock(m);
        try {
            BlockBehaviour.Properties properties = (Properties) fieldBlockBehaviour_properties.get(b);
            properties.color(transparentColor);
            for (BlockState state : b.getStateDefinition().getPossibleStates()) {
                fieldBlockStateBase_materialColor.set(state, transparentColor);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not set the MaterialColor!");
        }
    }
}
