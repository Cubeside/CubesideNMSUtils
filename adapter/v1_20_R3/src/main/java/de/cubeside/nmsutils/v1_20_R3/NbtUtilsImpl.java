package de.cubeside.nmsutils.v1_20_R3;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import de.cubeside.nmsutils.NbtUtils;
import de.cubeside.nmsutils.nbt.CompoundTag;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;

public class NbtUtilsImpl implements NbtUtils {
    // private final NMSUtilsImpl nmsUtils;

    public NbtUtilsImpl(NMSUtilsImpl nmsUtils) {
        // this.nmsUtils = nmsUtils;
    }

    @Override
    public CompoundTag parseBinary(byte[] in) {
        try {
            return new CompoundTagImpl(net.minecraft.nbt.NbtIo.read(new DataInputStream(new ByteArrayInputStream(in))));
        } catch (IOException e) {
            throw new RuntimeException("invalid nbt");
        }
    }

    @Override
    public byte[] writeBinary(CompoundTag in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            net.minecraft.nbt.NbtIo.write(((CompoundTagImpl) in).handle, new DataOutputStream(os));
        } catch (IOException e) {
            throw new RuntimeException("could not serialize nbt");
        }
        return os.toByteArray();
    }

    @Override
    public CompoundTag parseString(String in) {
        try {
            return new CompoundTagImpl(net.minecraft.nbt.NbtUtils.snbtToStructure(in));
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse snbt string", e);
        }
    }

    @Override
    public String writeString(CompoundTag in) {
        return net.minecraft.nbt.NbtUtils.structureToSnbt(((CompoundTagImpl) in).handle);
    }

    @Override
    public int getCurrentDataVersion() {
        return SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    private CompoundTag updateNbt(DSL.TypeReference type, CompoundTag in, int oldVersion) {
        int currentVersion = getCurrentDataVersion();
        if (oldVersion >= currentVersion || in == null) {
            return in;
        }
        Dynamic<Tag> dyn = new Dynamic<>(NbtOps.INSTANCE, ((CompoundTagImpl) in).handle);
        Tag out = DataFixers.getDataFixer().update(type, dyn, oldVersion, currentVersion).getValue();
        if (out instanceof net.minecraft.nbt.CompoundTag compound) {
            return new CompoundTagImpl(compound);
        }
        throw new RuntimeException("Expected CompoundTag as result of update, but got " + out.getClass().getName() + " !");
    }

    private String updateName(DSL.TypeReference type, String in, int oldVersion) {
        int currentVersion = getCurrentDataVersion();
        if (oldVersion >= currentVersion || in == null) {
            return in;
        }
        Tag out = DataFixers.getDataFixer().update(type, new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(in)), oldVersion, currentVersion).getValue();
        if (out instanceof net.minecraft.nbt.StringTag stringTag) {
            return stringTag.getAsString();
        }
        throw new RuntimeException("Expected StringTag as result of update, but got " + out.getClass().getName() + " !");
    }

    @Override
    public CompoundTag updateEntity(CompoundTag in, int oldVersion) {
        return updateNbt(References.ENTITY, in, oldVersion);
    }

    @Override
    public CompoundTag updateBlockState(CompoundTag in, int oldVersion) {
        return updateNbt(References.BLOCK_ENTITY, in, oldVersion);
    }

    @Override
    public String updateItemTypeName(String in, int oldVersion) {
        return updateName(References.ITEM_NAME, in, oldVersion);
    }

    @Override
    public String updateBlockTypeName(String in, int oldVersion) {
        return updateName(References.BLOCK_NAME, in, oldVersion);
    }
}
