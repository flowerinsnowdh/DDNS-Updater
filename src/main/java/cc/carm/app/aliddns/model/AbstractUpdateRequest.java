package cc.carm.app.aliddns.model;

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
}
