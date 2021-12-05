package de.cubeside.nmsutils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.format.MappingFormat;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MappingTree.FieldMapping;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import org.bukkit.Bukkit;

/**
 * Modified from https://github.com/PaperMC/Paper/blob/675d1e3f58ab86d5a3b1bc8bbcf2beefc9696078/patches/server/0420-Deobfuscate-stacktraces-in-log-messages-crash-report.patch
 * see there for the license
 */
public enum ReobfHelper {
    INSTANCE;

    public static final String MOJANG_PLUS_YARN_NAMESPACE = "mojang+yarn";
    public static final String SPIGOT_NAMESPACE = "spigot";

    private final Map<String, ClassMapping> mappingsByObfName;
    private final Map<String, ClassMapping> mappingsByMojangName;

    ReobfHelper() {
        final Set<ClassMapping> maps = loadMappingsIfPresent();
        if (maps != null) {
            this.mappingsByObfName = maps.stream().collect(Collectors.toUnmodifiableMap(ClassMapping::obfName, map -> map));
            this.mappingsByMojangName = maps.stream().collect(Collectors.toUnmodifiableMap(ClassMapping::mojangName, map -> map));
        } else {
            this.mappingsByObfName = null;
            this.mappingsByMojangName = null;
        }
    }

    public Map<String, ClassMapping> mappingsByObfName() {
        return this.mappingsByObfName;
    }

    public Map<String, ClassMapping> mappingsByMojangName() {
        return this.mappingsByMojangName;
    }

    /**
     * Attempts to get the obf name for a given class by its Mojang name. Will
     * return the input string if mappings are not present.
     *
     * @param fullyQualifiedMojangName
     *            fully qualified class name (dotted)
     * @return mapped or original fully qualified (dotted) class name
     */
    public String reobfClassName(final String fullyQualifiedMojangName) {
        if (this.mappingsByMojangName == null) {
            return fullyQualifiedMojangName;
        }

        final ClassMapping map = this.mappingsByMojangName.get(fullyQualifiedMojangName);
        if (map == null) {
            return fullyQualifiedMojangName;
        }

        return map.obfName();
    }

    /**
     * Attempts to get the Mojang name for a given class by its obf name. Will
     * return the input string if mappings are not present.
     *
     * @param fullyQualifiedObfName
     *            fully qualified class name (dotted)
     * @return mapped or original fully qualified (dotted) class name
     */
    public String deobfClassName(final String fullyQualifiedObfName) {
        if (this.mappingsByObfName == null) {
            return fullyQualifiedObfName;
        }

        final ClassMapping map = this.mappingsByObfName.get(fullyQualifiedObfName);
        if (map == null) {
            return fullyQualifiedObfName;
        }

        return map.mojangName();
    }

    private static Set<ClassMapping> loadMappingsIfPresent() {
        try (final InputStream mappingsInputStream = Bukkit.class.getClassLoader().getResourceAsStream("META-INF/mappings/reobf.tiny")) {
            if (mappingsInputStream == null) {
                return null;
            }
            final MemoryMappingTree tree = new MemoryMappingTree();
            MappingReader.read(new InputStreamReader(mappingsInputStream, StandardCharsets.UTF_8), MappingFormat.TINY_2, tree);
            final Set<ClassMapping> classes = new HashSet<>();

            final StringPool pool = new StringPool();
            for (final MappingTree.ClassMapping cls : tree.getClasses()) {
                final Map<String, String> methods = new HashMap<>();
                final Map<String, String> fields = new HashMap<>();

                for (final MappingTree.MethodMapping methodMapping : cls.getMethods()) {
                    methods.put(
                            pool.string(methodOrFieldKey(
                                    methodMapping.getName(MOJANG_PLUS_YARN_NAMESPACE),
                                    methodMapping.getDesc(MOJANG_PLUS_YARN_NAMESPACE))),
                            pool.string(methodMapping.getName(SPIGOT_NAMESPACE)));
                }
                for (final FieldMapping fieldsMapping : cls.getFields()) {
                    fields.put(
                            pool.string(fieldsMapping.getName(MOJANG_PLUS_YARN_NAMESPACE)),
                            pool.string(fieldsMapping.getName(SPIGOT_NAMESPACE)));
                }

                final ClassMapping map = new ClassMapping(
                        cls.getName(SPIGOT_NAMESPACE).replace('/', '.'),
                        cls.getName(MOJANG_PLUS_YARN_NAMESPACE).replace('/', '.'),
                        Map.copyOf(methods), Map.copyOf(fields));
                classes.add(map);
            }

            return Set.copyOf(classes);
        } catch (final IOException ex) {
            System.err.println("Failed to load mappings for stacktrace deobfuscation.");
            ex.printStackTrace();
            return null;
        }
    }

    public static String methodOrFieldKey(final String obfName, final String obfDescriptor) {
        return obfName + obfDescriptor;
    }

    private static final class StringPool {
        private final Map<String, String> pool = new HashMap<>();

        public String string(final String string) {
            return this.pool.computeIfAbsent(string, Function.identity());
        }
    }

    public record ClassMapping(
            String obfName,
            String mojangName,
            Map<String, String> methodsByMojang,
            Map<String, String> fieldsByMojang) {
    }

    final static Map<String, ReobfHelper.ClassMapping> mappings = ReobfHelper.INSTANCE.mappingsByObfName();

    public static String getObfuscatedFieldName(Class<?> clazz, String field) {
        if (mappings != null) {
            ClassMapping classMapping = mappings.get(clazz.getName());
            if (classMapping != null) {
                String fieldName = classMapping.fieldsByMojang().get(field);
                if (fieldName != null) {
                    return fieldName;
                }
            }
        }
        return field;
    }

    public static Field getFieldByMojangName(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(getObfuscatedFieldName(clazz, fieldName));
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
