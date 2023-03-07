package cc.carm.app.aliddns.model;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 已支持的UpdateRequest类型
 */
public class UpdateRequestTypes {
    @DeclaredUpdateRequestType
    public static UpdateRequestType<AliyunUpdateRequest> ALIYUN = new UpdateRequestType<>(
            "aliyun", AliyunUpdateRequest.class, config ->
                        new AliyunUpdateRequest(
                                config.getString("access-key", "xx"),
                                config.getString("access-secret", "xx"),
                                config.getString("domain", "xx"),
                                config.getString("record", "xx"),
                                config.getBoolean("ipv6", false)
                        )
    );

    @DeclaredUpdateRequestType
    public static UpdateRequestType<CloudflareV4UpdateRequest> CLOUDFLARE_V4 = new UpdateRequestType<>(
            "cloudflare", CloudflareV4UpdateRequest.class, config ->
                    new CloudflareV4UpdateRequest(
                            config.getString("domain", "xx"),
                            config.getString("record", "xx"),
                            config.getBoolean("ipv6", false),
                            config.getString("zone-identifier", "xx"),
                            config.getString("identifier", "xx"),
                            config.getLong("ttl", 1L),
                            config.getBoolean("proxied", false),
                            config.getString("comments")
                    )
    );

    public static class UpdateRequestType<T extends UpdateRequest> {
        public final String id;
        public final Class<T> type;
        public final ICreatableRequestType<T> creatable;

        public UpdateRequestType(String id, Class<T> type, ICreatableRequestType<T> creatable) {
            this.id = id;
            this.type = type;
            this.creatable = creatable;
        }

        public T create(ConfigurationWrapper<?> config) {
            return creatable.create(config);
        }
    }

    /**
     * 声明后的字段会被自动识别为UpdateRequestType
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DeclaredUpdateRequestType {
    }

    /**
     * 获取本类中定义的UpdateRequestType列表
     * Map key: ID
     * Map Value: Type
     *
     * @return 本类中定义的UpdateRequestType列表
     */
    public static Map<String, UpdateRequestType<?>> values() {
        HashMap<String, UpdateRequestType<?>> values = new HashMap<>();
        for (Field field : UpdateRequestTypes.class.getFields()) {
            // 必须是 @DeclaredUpdateRequestType public static UpdateRequestType
            if (Modifier.isStatic(field.getModifiers()) &&
                    field.isAnnotationPresent(DeclaredUpdateRequestType.class) &&
                    field.getType().equals(UpdateRequestType.class)
            ) {
                try {
                    UpdateRequestType<?> value = (UpdateRequestType<?>) field.get(null);
                    values.put(value.id, value);
                } catch (IllegalAccessException e) {
                    // 既然已经是public了，就不管这个异常了
                    throw new RuntimeException(e);
                }
            }
        }
        return values;
    }

    public static Optional<UpdateRequestType<?>> getByID(String id) {
        return Optional.ofNullable(values().get(id));
    }
}
