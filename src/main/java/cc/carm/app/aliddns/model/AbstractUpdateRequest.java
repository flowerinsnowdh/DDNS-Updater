package cc.carm.app.aliddns.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractUpdateRequest implements UpdateRequest {
    public final String domain;
    public final String record;

    public final boolean ipv6;

    protected AbstractUpdateRequest(String domain, String record, boolean ipv6) {
        this.domain = domain;
        this.record = record;
        this.ipv6 = ipv6;
    }

    @Override
    public String getTypeID() {
        return getType().id;
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public String getRecord() {
        return this.record;
    }

    @Override
    public String getFullDomain() {
        if (getRecord().equals("@")) {
            return getDomain();
        } else {
            return getRecord() + "." + getDomain();
        }
    }

    @Override
    public boolean isIPv6() {
        return this.ipv6;
    }

    @Override
    public String getRecordType() {
        return isIPv6() ? "AAAA" : "A";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", getTypeID());
        data.put("domain", getDomain());
        data.put("record", getRecord());
        data.put("ipv6", isIPv6());

        serialize(data);
        return data;
    }

    /**
     * 继续序列化
     * 已写好domain, record, ipv6, type四个参数，请继续序列化其他内容
     *
     * @param data 序列化数据
     */
    public abstract void serialize(Map<String, Object> data);
}
