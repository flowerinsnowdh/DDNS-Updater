package cc.carm.app.aliddns.model;

import cc.carm.app.aliddns.Main;
import cc.carm.app.aliddns.model.json.JsonCloudFlareV4DNSUpdate;
import com.alibaba.fastjson.JSON;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>代表一个Cloudflare API v4更新请求</p>
 * <p>Cloudflare更新请求有两个特殊字段</p>
 * <ul>
 *     <li>zone-identifier</li>
 *     <li>identifier</li>
 *     <li>auth-email</li>
 *     <li>auth-key</li>
 * </ul>
 * <p>还有三个可选字段</p>
 * <ul>
 *     <li>ttl - 默认1（自动）</li>
 *     <li>proxied - 默认false</li>
 *     <li>comment</li>
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
    /**
     * 单位：秒，值为1时表示自动
     */
    public final long ttl;
    public final boolean proxied;
    public final String comments;
    public final String authEmail;
    public final String authKey;

    public CloudflareV4UpdateRequest(String domain, String record, boolean ipv6, @NotNull String zoneIdentifier, @NotNull String identifier, @NotNull String authEmail, @NotNull String authKey) {
        this(domain, record, ipv6, zoneIdentifier, identifier, authEmail, authKey, 1L, false, null);
    }

    public CloudflareV4UpdateRequest(String domain, String record, boolean ipv6, @NotNull String zoneIdentifier, @NotNull String identifier, @NotNull String authEmail, @NotNull String authKey,
                                     long ttl, boolean proxied, String comments
    ) {
        super(domain, record, ipv6);
        this.zoneIdentifier = zoneIdentifier;
        this.identifier = identifier;
        this.authEmail = authEmail;
        this.authKey = authKey;
        this.ttl = ttl;
        this.proxied = proxied;
        this.comments = comments;
    }

    @Override
    public void serialize(Map<String, Object> data) {
        data.put("zone-identifier", this.zoneIdentifier);
        data.put("identifier", this.identifier);
        data.put("auth-email", this.authEmail);
        data.put("auth-key", this.authKey);
        data.put("ttl", this.ttl);
        data.put("proxied", this.proxied);
        data.put("comments", comments);
    }

    @Override
    public UpdateRequestTypes.UpdateRequestType<CloudflareV4UpdateRequest> getType() {
        return UpdateRequestTypes.CLOUDFLARE_V4;
    }

    @Override
    public void doUpdate(String currentValue) throws UpdateException {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        BufferedReader in = null;
        try {
            URL url = new URL("https://api.cloudflare.com/client/v4/zones/" + zoneIdentifier + "/dns_records/" + identifier);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("X-Auth-Email", authEmail);
            conn.setRequestProperty("X-Auth-Key", authKey);
            (out = conn.getOutputStream()).write(
                    JSON.toJSONString(new JsonCloudFlareV4DNSUpdate(
                            getRecordType(), getFullDomain(), currentValue, ttl, proxied, comments, null
                    )).getBytes(StandardCharsets.UTF_8)
            );
            int status = conn.getResponseCode();
            if (status != 200) {
                Main.severe("    记录 “" + getFullDomain() + "” 更新失败,服务器返回状态码" + status + "。");
                // 获取错误信息
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                Main.severe(sb.toString());
            } else {
                Main.info("     记录 “" + getFullDomain() + "” 成功更新为 " + currentValue + " 。");
            }
        } catch (IOException e) {
            throw new UpdateException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
