package cc.carm.app.aliddns.model;

import com.aliyuncs.exceptions.ClientException;

import java.util.Map;

public interface UpdateRequest {
    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    UpdateRequestType getType();

    /**
     * 获取请求类型序列化名
     *
     * @see UpdateRequestType#id
     * @return 请求类型序列化名
     */
    String getTypeID();

    /**
     * 获取域名
     *
     * @return 域名
     */
    String getDomain();

    /**
     * 获取记录名
     *
     * @return 记录名
     */
    String getRecord();

    /**
     * 获取完整域名
     * default就不写了，为了更方便扩展
     *
     * @return 完整域名
     */
    String getFullDomain();

    /**
     * 获取是否是IPv6
     *
     * @return 是否是IPv6
     */
    boolean isIPv6();

    /**
     * 进行更新操作
     *
     * @param currentValue 当前数值内容
     */
    void doUpdate(String currentValue) throws ClientException;

    /**
     * 获取记录类型
     * IPv4: A
     * IPv6: AAAA
     * default就不写了，为了更方便扩展
     *
     * @return 记录类型
     */
    String getRecordType();

    /**
     * 序列化
     *
     * @return 序列化
     */
    Map<String, Object> serialize();
}
