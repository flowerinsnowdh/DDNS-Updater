package cc.carm.app.aliddns.model;

public enum UpdateRequestType {
    ALIYUN("aliyun", AliyunUpdateRequest.class);

    public final String id;
    public final Class<? extends UpdateRequest> type;

    UpdateRequestType(String id, Class<? extends UpdateRequest> type) {
        this.id = id;
        this.type = type;
    }
}
