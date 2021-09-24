package de.cubeside.nmsutils.v1_16_R3;

import de.cubeside.nmsutils.MiscUtils;
import de.cubeside.nmsutils.NMSUtils;
import org.bukkit.Material;

public class MiscUtilsImpl implements MiscUtils {
    private final NMSUtilsImpl nmsUtils;

    public MiscUtilsImpl(NMSUtilsImpl nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }

    @Override
    public void setBlockMapColorTransparent(Material m) {
        throw new IllegalStateException("Not available in this version!");
    }
}
