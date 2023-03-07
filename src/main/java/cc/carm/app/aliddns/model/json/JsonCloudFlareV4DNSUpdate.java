package cc.carm.app.aliddns.model.json;

import java.util.List;
import java.util.Objects;

/**
 * <p>调用API <a href="https://api.cloudflare.com/client/v4/zones/:zone_identifier/dns_records/:identifier">https://api.cloudflare.com/client/v4/zones/:zone_identifier/dns_records/:identifier</a></p>
 */
public class JsonCloudFlareV4DNSUpdate {
    /**
     * 记录类型
     * 例如A, AAAA
     */
    public String type;
    public String name;
    public String content;
    public long ttl;
    public boolean proxied;
    public String comment;
    public List<String> tags;

    public JsonCloudFlareV4DNSUpdate(String type, String name, String content, long ttl, boolean proxied, String comment, List<String> tags) {
        this.type = type;
        this.name = name;
        this.content = content;
        this.ttl = ttl;
        this.proxied = proxied;
        this.comment = comment;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonCloudFlareV4DNSUpdate that = (JsonCloudFlareV4DNSUpdate) o;
        return ttl == that.ttl && proxied == that.proxied && Objects.equals(type, that.type) && Objects.equals(name, that.name) && Objects.equals(content, that.content) && Objects.equals(comment, that.comment) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (ttl ^ (ttl >>> 32));
        result = 31 * result + (proxied ? 1231 : 1237);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JsonCloudFlareV4DNSUpdate{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", ttl=" + ttl +
                ", proxied=" + proxied +
                ", comment='" + comment + '\'' +
                ", tags=" + tags +
                '}';
    }
}
