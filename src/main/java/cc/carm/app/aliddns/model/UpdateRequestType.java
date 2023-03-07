package cc.carm.app.aliddns.model;

import java.util.Optional;

public enum UpdateRequestType {
    ALIYUN("aliyun", AliyunUpdateRequest.class),
    CLOUDFLARE_V4("cloudflare", CloudflareV4UpdateRequest.class);

    public final String id;
    public final Class<? extends UpdateRequest> type;

    UpdateRequestType(String id, Class<? extends UpdateRequest> type) {
        this.id = id;
        this.type = type;
    }

    public static Optional<UpdateRequestType> getByID(String id) {
        for (UpdateRequestType value : values()) {
            if (value.id.equalsIgnoreCase(id)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
