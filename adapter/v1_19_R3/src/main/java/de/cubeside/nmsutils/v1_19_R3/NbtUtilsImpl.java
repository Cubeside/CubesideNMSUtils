package de.cubeside.nmsutils.v1_19_R3;

import de.cubeside.nmsutils.NbtUtils;
import de.cubeside.nmsutils.nbt.CompoundTag;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
}
