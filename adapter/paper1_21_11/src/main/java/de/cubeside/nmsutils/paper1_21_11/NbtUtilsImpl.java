package de.cubeside.nmsutils.paper1_21_11;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import de.cubeside.nmsutils.NbtUtils;
import de.cubeside.nmsutils.nbt.CompoundTag;
import de.cubeside.nmsutils.nbt.ListTag;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NbtUtilsImpl implements NbtUtils {
    private final NMSUtilsImpl nmsUtils;

    public NbtUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public CompoundTag createEmptyCompound() {
        return new CompoundTagImpl(new net.minecraft.nbt.CompoundTag());
    }

    @Override
    public ListTag createEmptyList() {
        return new ListTagImpl(new net.minecraft.nbt.ListTag());
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
        return SharedConstants.getCurrentVersion().dataVersion().version();
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
            return stringTag.value();
        }
        throw new RuntimeException("Expected StringTag as result of update, but got " + out.getClass().getName() + " !");
    }

    @Override
    public CompoundTag updateEntity(CompoundTag in, int oldVersion) {
        return updateNbt(References.ENTITY, in, oldVersion);
    }

    @Override
    public CompoundTag updateItem(CompoundTag in, int oldVersion) {
        return updateNbt(References.ITEM_STACK, in, oldVersion);
    }

    @Override
    public CompoundTag updateBlockEntity(CompoundTag in, int oldVersion) {
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

    @Override
    public CompoundTag getItemStackNbt(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        net.minecraft.nbt.CompoundTag compoundTag = (net.minecraft.nbt.CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
                CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), CraftItemStack.asNMSCopy(stack)).getOrThrow();
        net.minecraft.nbt.NbtUtils.addCurrentDataVersion(compoundTag);
        return new CompoundTagImpl(compoundTag);
    }

    @Override
    public ItemStack createItemStack(CompoundTag tag) {
        net.minecraft.nbt.CompoundTag compound = ((CompoundTagImpl) tag).handle;
        final int dataVersion = compound.getIntOr("DataVersion", 0);
        compound = PlatformHooks.get().convertNBT(References.ITEM_STACK, DataFixers.getDataFixer(), compound, dataVersion, getCurrentDataVersion());

        if (compound.getStringOr("id", "minecraft:air").equals("minecraft:air")) {
            return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.EMPTY);
        }
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.CODEC.parse(
                CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), compound).getOrThrow());
    }

    @Override
    public CompoundTag getBlockEntityNbt(org.bukkit.block.Block block) {
        CraftBlock craftBlock = (CraftBlock) block;
        BlockPos blockPosition = craftBlock.getPosition();
        BlockEntity tileEntity = craftBlock.getHandle().getBlockEntity(blockPosition);
        if (tileEntity == null) {
            return null;
        }
        net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
        tag = tileEntity.saveWithFullMetadata(CraftRegistry.getMinecraftRegistry());
        return new CompoundTagImpl(tag);
    }

    @Override
    public void setBlockEntityNbt(org.bukkit.block.Block block, CompoundTag tag) {
        CraftBlock craftBlock = (CraftBlock) block;
        BlockPos blockPosition = craftBlock.getPosition();
        BlockEntity tileEntity = craftBlock.getHandle().getBlockEntity(blockPosition);
        if (tileEntity == null) {
            return;
        }
        net.minecraft.nbt.CompoundTag nativeNbt = ((CompoundTagImpl) tag).handle;
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(tileEntity.problemPath(), nmsUtils.getPlugin().getSLF4JLogger())) {
            ValueInput valueInput = TagValueInput.create(scopedCollector, craftBlock.getHandle().registryAccess(), nativeNbt);
            tileEntity.loadWithComponents(valueInput);
        }
    }
}
