package de.cubeside.nmsutils;

import org.bukkit.Material;

public interface MiscUtils {

    NMSUtils getNMSUtils();

    /**
     * Sets the color on maps for this block to be transparent
     *
     * @param m
     *            the block
     */
    void setBlockMapColorTransparent(Material m);

}
