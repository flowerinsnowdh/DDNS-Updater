package cc.carm.app.aliddns.model;

import cc.carm.app.aliddns.Main;
import cc.carm.app.aliddns.manager.RequestManager;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

// 配置文件的requests节点
public class RequestRegistry {

    protected final LinkedHashMap<String, AliyunUpdateRequest> requests;
    protected int updateCount;
    protected boolean hasV6Request;

    public RequestRegistry(LinkedHashMap<String, AliyunUpdateRequest> requests) {
        this.requests = requests;
        this.updateCount = 1;
        this.hasV6Request = requests.values().stream().anyMatch(AliyunUpdateRequest::isIPv6);
    }

    public LinkedHashMap<String, AliyunUpdateRequest> listRequests() {
        return requests;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void countUpdate() {
        this.updateCount++;
    }

    public boolean hasV6Request() {
        return hasV6Request;
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        listRequests().forEach((k, r) -> data.put(k, r.serialize()));
        return data;
    }

    public static RequestRegistry loadFrom(ConfigurationWrapper<?> section) {
        LinkedHashMap<String, AliyunUpdateRequest> data = new LinkedHashMap<>();
        if (section == null) return new RequestRegistry(data);
        for (String taskName : section.getKeys(false)) {
            ConfigurationWrapper<?> requestSection = section.getConfigurationSection(taskName);
            if (requestSection == null) continue;

            AliyunUpdateRequest request = new AliyunUpdateRequest(
                    requestSection.getString("access-key", "xx"),
                    requestSection.getString("access-secret", "xx"),
                    requestSection.getString("domain", "xx"),
                    requestSection.getString("record", "xx"),
                    requestSection.getBoolean("ipv6", false)
            );

            if (request.isIPv6() && !RequestManager.isIPV6Enabled()) {
                Main.info("记录 [" + taskName + "] 为IPv6任务，但本实例未启用IPv6，跳过加载。");
                continue;
            }

            data.put(taskName, request);
        }
        return new RequestRegistry(data);
    }

    public static RequestRegistry defaults() {
        LinkedHashMap<String, AliyunUpdateRequest> data = new LinkedHashMap<>();
        data.put("demo", new AliyunUpdateRequest("YOUR-ACCESS-KEY", "YOUR-ACCESS-SECRET", "example.com", "@", false));
        return new RequestRegistry(data);
    }


}
