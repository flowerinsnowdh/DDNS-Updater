package cc.carm.app.aliddns.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class QueryConfig extends ConfigurationRoot {

    @HeaderComment({"IPv4地址获取链接"})
    public static final ConfigValue<String> V4 = ConfiguredValue.of(String.class, "http://ifconfig.me/ip");

    @HeaderComment({"IPv6地址获取链接 (可选)", "如不需要IPV6，则可以直接将地址留空。", "优先于v6-interface"})
    public static final ConfigValue<String> V6 = ConfiguredValue.of(String.class, "https://v6.ip.zxinc.org/getip");


    @HeaderComment({"直接通过物理或虚拟网络接入点获取IPv6地址", "如不需要IPV6，则可以直接全部留空"})
    public static class V6Interface extends ConfigurationRoot {
        @HeaderComment({"网络接入点"})
        public static final ConfigValue<String> INTERFACE = ConfiguredValue.of(String.class, "eth5");
        @HeaderComment({"网络接入点可以有多个IP地址", "这里特指第几个IP地址", "下标从0开始"})
        public static final ConfigValue<Integer> INDEX = ConfiguredValue.of(Integer.class, 2);
    }
}
