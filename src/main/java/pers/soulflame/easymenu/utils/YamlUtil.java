package pers.soulflame.easymenu.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.Reader;
import java.io.Writer;

/**
 * <p>yaml工具类</p>
 */
@SuppressWarnings("unused")
public final class YamlUtil {

    private YamlUtil() {

    }

    private static final Yaml yaml;

    static {
        final var options = new DumperOptions();
        options.setIndentWithIndicator(true);
        options.setIndicatorIndent(2);
        final var representer = new Representer(options) {
            @Override
            protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
                if (propertyValue == null) return null;
                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        };
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        final var propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        representer.setPropertyUtils(propertyUtils);
        yaml = new Yaml(representer);
    }

    public static String toYaml(Object object) {
        return yaml.dump(object);
    }

    public static void toYaml(Object object, Writer writer) {
        yaml.dump(object, writer);
    }

    public static <T> T load(String string) {
        return yaml.load(string);
    }

    public static <T> T load(Reader reader) {
        return yaml.load(reader);
    }

    public static <T> T loadAs(String string, Class<T> type) {
        return yaml.loadAs(string, type);
    }

    public static <T> T loadAs(Reader reader, Class<T> type) {
        return yaml.loadAs(reader, type);
    }
}
