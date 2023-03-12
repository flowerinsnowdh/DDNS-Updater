package cc.carm.app.aliddns.model.json.cloudflare;

import java.util.List;
import java.util.Objects;

public class JsonListDnsRecordsResponse {
    public List<Result> result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonListDnsRecordsResponse that = (JsonListDnsRecordsResponse) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.result != null ? this.result.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JsonListDnsRecordsResponse{" +
                "result=" + result +
                '}';
    }

    public static class Result {
        public String id;
        public String zoneId;
        public String zoneName;
        public String name;
        public String type;
        public String content;
        public boolean proxiable;
        public boolean proxied;
        public long ttl;
        public boolean locked;
        public Meta meta;
        public String comment;
        public List<String> tags;
        public String createdOn;
        public String modifiedOn;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return proxiable == result.proxiable && proxied == result.proxied && ttl == result.ttl && locked == result.locked && Objects.equals(id, result.id) && Objects.equals(zoneId, result.zoneId) && Objects.equals(zoneName, result.zoneName) && Objects.equals(name, result.name) && Objects.equals(type, result.type) && Objects.equals(content, result.content) && Objects.equals(meta, result.meta) && Objects.equals(comment, result.comment) && Objects.equals(tags, result.tags) && Objects.equals(createdOn, result.createdOn) && Objects.equals(modifiedOn, result.modifiedOn);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            result = 31 * result + (zoneId != null ? zoneId.hashCode() : 0);
            result = 31 * result + (zoneName != null ? zoneName.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (content != null ? content.hashCode() : 0);
            result = 31 * result + (proxiable ? 1231 : 1237);
            result = 31 * result + (proxied ? 1231 : 1237);
            result = 31 * result + ((int) (ttl ^ (ttl >>> 32)));
            result = 31 * result + (locked ? 1231 : 1237);
            result = 31 * result + (meta != null ? meta.hashCode() : 0);
            result = 31 * result + (comment != null ? comment.hashCode() : 0);
            result = 31 * result + (tags != null ? tags.hashCode() : 0);
            result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
            result = 31 * result + (modifiedOn != null ? modifiedOn.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "id='" + id + '\'' +
                    ", zoneId='" + zoneId + '\'' +
                    ", zoneName='" + zoneName + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    ", proxiable=" + proxiable +
                    ", proxied=" + proxied +
                    ", ttl=" + ttl +
                    ", locked=" + locked +
                    ", meta=" + meta +
                    ", comment='" + comment + '\'' +
                    ", tags=" + tags +
                    ", createdOn='" + createdOn + '\'' +
                    ", modifiedOn='" + modifiedOn + '\'' +
                    '}';
        }

        public static class Meta {
            public boolean autoAdded;
            public boolean managedByApps;
            public boolean managedByArgoTunnel;
            public String source;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Meta meta = (Meta) o;
                return autoAdded == meta.autoAdded && managedByApps == meta.managedByApps && managedByArgoTunnel == meta.managedByArgoTunnel && Objects.equals(source, meta.source);
            }

            @Override
            public int hashCode() {
                int result = 17;
                result = 31 * result + (autoAdded ? 1231 : 1237);
                result = 31 * result + (managedByApps ? 1231 : 1237);
                result = 31 * result + (managedByArgoTunnel ? 1231 : 1237);
                result = 31 * result + (source != null ? source.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "Meta{" +
                        "autoAdded=" + autoAdded +
                        ", managedByApps=" + managedByApps +
                        ", managedByArgoTunnel=" + managedByArgoTunnel +
                        ", source='" + source + '\'' +
                        '}';
            }
        }
    }
}
