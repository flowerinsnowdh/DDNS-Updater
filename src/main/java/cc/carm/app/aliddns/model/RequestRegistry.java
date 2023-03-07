package cc.carm.app.aliddns.model;

import cc.carm.app.aliddns.Main;
import cc.carm.app.aliddns.manager.RequestManager;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

// 配置文件的requests节点
public class RequestRegistry {

    protected final LinkedHashMap<String, UpdateRequest> requests;
    protected int updateCount;
    protected boolean hasV6Request;

    public RequestRegistry(LinkedHashMap<String, UpdateRequest> requests) {
        this.requests = requests;
        this.updateCount = 1;
        this.hasV6Request = requests.values().stream().anyMatch(UpdateRequest::isIPv6);
    }

    public LinkedHashMap<String, UpdateRequest> listRequests() {
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
        LinkedHashMap<String, UpdateRequest> data = new LinkedHashMap<>();
        if (section == null) return new RequestRegistry(data);
        for (String taskName : section.getKeys(false)) {
            ConfigurationWrapper<?> requestSection = section.getConfigurationSection(taskName);
            if (requestSection == null) continue;

            String type = requestSection.getString("type");

            Optional<UpdateRequestTypes.UpdateRequestType<?>> optType = UpdateRequestTypes.getByID(type);
            if (optType.isPresent()) {
                UpdateRequest request;
                try {
                    request = optType.get().create(requestSection);
                } catch (IllegalArgumentException ex) {
                    Main.info("记录 [" + taskName + "] 加载出现错误，可能是配置文件格式不正确。跳过加载。");
                    ex.printStackTrace();
                    continue;
                }
                if (request.isIPv6() && !RequestManager.isIPV6Enabled()) {
                    Main.info("记录 [" + taskName + "] 为IPv6任务，但本实例未启用IPv6，跳过加载。");
                    continue;
                }

                data.put(taskName, request);
            }
        }
        return new RequestRegistry(data);
    }

    /**
     * 示例
     *
     * @return 示例
     */
    public static RequestRegistry example() {
        LinkedHashMap<String, UpdateRequest> data = new LinkedHashMap<>();
        data.put("demo", new AliyunUpdateRequest("YOUR-ACCESS-KEY", "YOUR-ACCESS-SECRET", "example.com", "@", false));
        return new RequestRegistry(data);
    }


}
