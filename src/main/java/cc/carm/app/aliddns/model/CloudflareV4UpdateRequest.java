package cc.carm.app.aliddns.model;

import com.aliyuncs.exceptions.ClientException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * <p>代表一个Cloudflare API v4更新请求</p>
 * <p>Cloudflare更新请求有两个特殊字段</p>
 * <ul>
 *     <li>zone-identifier</li>
 *     <li>identifier</li>
 * </ul>
 */
public class CloudflareV4UpdateRequest extends AbstractUpdateRequest {
    /**
     * 区域ID
     * 可在CloudFlare站点-概述-右侧的列表中的"API"分块中查看
     */
    public final String zoneIdentifier;

    /**
     * 帐户ID
     * 可在CloudFlare站点-概述-右侧的列表中的"API"分块中查看
     */
    public final String identifier;
    public CloudflareV4UpdateRequest(String domain, String record, boolean ipv6, @NotNull String zoneIdentifier, @NotNull String identifier) {
        super(domain, record, ipv6);
        this.zoneIdentifier = zoneIdentifier;
        this.identifier = identifier;
    }

    @Override
    public void serialize(Map<String, Object> data) {
        data.put("zone-identifier", this.zoneIdentifier);
        data.put("identifier", this.identifier);
    }

    @Override
    public UpdateRequestType getType() {
        return UpdateRequestType.CLOUDFLARE_V4;
    }

    @Override
    public void doUpdate(String currentValue) throws ClientException {
        // TODO Update action
    }
}
