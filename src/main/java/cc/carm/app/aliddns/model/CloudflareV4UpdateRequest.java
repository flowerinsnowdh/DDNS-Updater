package cc.carm.app.aliddns.model;

import cc.carm.app.aliddns.Main;
import cc.carm.app.aliddns.model.json.JsonCloudFlareV4DNSUpdate;
import cc.carm.app.aliddns.model.json.cloudflare.JsonListDnsRecordsResponse;
import com.alibaba.fastjson.JSON;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * <p>代表一个Cloudflare API v4更新请求</p>
 * <p>Cloudflare更新请求有三个特殊字段</p>
 * <ul>
 *     <li>zone-identifier</li>
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
     * 单位：秒，值为1时表示自动
     */
    public final long ttl;
    public final boolean proxied;
    public final String comments;
    public final String authEmail;
    public final String authKey;

    public CloudflareV4UpdateRequest(String domain, String record, boolean ipv6, @NotNull String zoneIdentifier, @NotNull String authEmail, @NotNull String authKey) {
        this(domain, record, ipv6, zoneIdentifier, authEmail, authKey, 1L, false, null);
    }

    public CloudflareV4UpdateRequest(String domain, String record, boolean ipv6, @NotNull String zoneIdentifier, @NotNull String authEmail, @NotNull String authKey,
                                     long ttl, boolean proxied, String comments
    ) {
        super(domain, record, ipv6);
        this.zoneIdentifier = zoneIdentifier;
        this.authEmail = authEmail;
        this.authKey = authKey;
        this.ttl = ttl;
        this.proxied = proxied;
        this.comments = comments;
    }

    @Override
    public void serialize(Map<String, Object> data) {
        data.put("zone-identifier", this.zoneIdentifier);
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
        final String[] id = {null};
        final String[] oldContent = {null};
        // 查询当前状态
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet req = new HttpGet("https://api.cloudflare.com/client/v4/zones/" + zoneIdentifier + "/dns_records");
            req.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            req.setHeader("X-Auth-Email", authEmail);
            req.setHeader("X-Auth-Key", authKey);
            httpClient.execute(req, resp -> {
                int status = resp.getCode();
                if (status != 200) {
                    Main.severe("    记录 “" + getFullDomain() + "” 查询失败,服务器返回以下内容：");
                    Main.severe("    " + resp);
                    Main.severe("    " + EntityUtils.toString(resp.getEntity()));
                    return null;
                }
                JsonListDnsRecordsResponse json = JSON.parseObject(EntityUtils.toString(resp.getEntity()), JsonListDnsRecordsResponse.class);
                // 寻找同名的记录
                Optional<JsonListDnsRecordsResponse.Result> optResult = json.result.stream().filter(res ->
                    CloudflareV4UpdateRequest.this.getFullDomain().equals(res.name) && res.type.equals(getRecordType())
                ).findAny();
                if (optResult.isPresent()) {
                    id[0] = optResult.get().id;
                    oldContent[0] = optResult.get().content;
                } else {
                    Main.severe("    域名“" + getDomain() + "”下无" + getRecordType() + "记录 “" + getRecord() + "” ，请检查Cloudflare仪表盘。");
                }
                return null;
            });
        } catch (IOException e) {
            throw new UpdateException(e);
        }
        if (id[0] == null) {
            return;
        }
        if (currentValue.equals(oldContent[0])) {
            Main.info("     记录 “" + getFullDomain() + "” 无需更新，跳过。");
            return;
        }

        // 需要更新，更新
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPut req = new HttpPut("https://api.cloudflare.com/client/v4/zones/" + zoneIdentifier + "/dns_records/" + id[0]);
            req.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            req.setHeader("X-Auth-Email", authEmail);
            req.setHeader("X-Auth-Key", authKey);
            req.setEntity(new StringEntity(JSON.toJSONString(
                    new JsonCloudFlareV4DNSUpdate(getRecordType(), getRecord(), currentValue, ttl, proxied, comments, null)
            )));
            httpClient.execute(req, resp -> {
                int status = resp.getCode();
                if (status != 200) {
                    Main.severe("    记录 “" + getFullDomain() + "” 更新失败,服务器返回以下内容：");
                    Main.severe("    " + resp);
                    Main.severe("    " + EntityUtils.toString(resp.getEntity()));
                    return null;
                }
                Main.info("     记录 “" + getFullDomain() + "” 成功更新为 " + currentValue + " 。");
                return null;
            });
        } catch (IOException e) {
            throw new UpdateException(e);
        }
    }
}
