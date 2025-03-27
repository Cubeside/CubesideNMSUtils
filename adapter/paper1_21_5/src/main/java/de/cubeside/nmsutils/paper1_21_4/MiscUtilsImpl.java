package de.cubeside.nmsutils.paper1_21_4;

import de.cubeside.nmsutils.MiscUtils;
import de.cubeside.nmsutils.NMSUtils;
import io.papermc.paper.adventure.PaperAdventure;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team.Visibility;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.scoreboard.Team.OptionStatus;

public class MiscUtilsImpl implements MiscUtils {
    private final NMSUtilsImpl nmsUtils;

    private Field fieldBlockBehaviour_properties;
    private MapColor transparentColor;
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
                if (f.getType() == MapColor.class) {
                    fieldBlockStateBase_materialColor = f;
                    fieldBlockStateBase_materialColor.setAccessible(true);
                }
            }
            if (fieldBlockStateBase_materialColor == null) {
                throw new IllegalStateException("Could not find BlockStateBase materialColor field!");
            }

            try {
                Constructor<MapColor> constructorMaterialColor = MapColor.class.getDeclaredConstructor(int.class, int.class);
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
            properties.mapColor(transparentColor);
            for (BlockState state : b.getStateDefinition().getPossibleStates()) {
                fieldBlockStateBase_materialColor.set(state, transparentColor);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not set the MaterialColor!");
        }
    }

    @Override
    public Object createTeamParametersPacketObject(BaseComponent displayName, BaseComponent prefix, BaseComponent suffix, OptionStatus nameTagDisplay, OptionStatus collisionRule, @SuppressWarnings("deprecation") org.bukkit.ChatColor color, boolean seeFriendlyInvisibles, boolean allowFriendlyFire) {
        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam team = new PlayerTeam(scoreboard, BaseComponent.toPlainText(displayName));
        team.setDisplayName(baseComponentToComponent(displayName));
        team.setPlayerPrefix(baseComponentToComponent(prefix));
        team.setPlayerSuffix(baseComponentToComponent(suffix));
        team.setNameTagVisibility(Visibility.values()[nameTagDisplay.ordinal()]);
        team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.values()[collisionRule.ordinal()]);
        team.setColor(color == null ? ChatFormatting.RESET : CraftChatMessage.getColor(color));
        team.setSeeFriendlyInvisibles(seeFriendlyInvisibles);
        team.setAllowFriendlyFire(allowFriendlyFire);
        return new ClientboundSetPlayerTeamPacket.Parameters(team);
    }

    private static Component baseComponentToComponent(BaseComponent c) {
        if (c == null) {
            return PaperAdventure.asVanilla(net.kyori.adventure.text.Component.empty());
        }
        String json = net.md_5.bungee.chat.ComponentSerializer.toString(c);
        return PaperAdventure.asVanilla(net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(json));
    }

    @Override
    public Class<? extends Object> getNumberFormatClass() {
        return NumberFormat.class;
    }

    @Override
    public Object getBlankNumberFormatInstance() {
        return BlankFormat.INSTANCE;
    }
}
