package cc.carm.app.aliddns.model;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * 可创建的请求类型
 *
 * @param <T> 请求类型
 */
public interface ICreatableRequestType<T extends UpdateRequest> {
    /**
     * 从配置文件创建
     *
     * @param config 配置文件
     * @return 创建的对象
     * @throws IllegalArgumentException 当通过配置文件无法创建时抛出
     */
    @NotNull T create(@NotNull ConfigurationWrapper<?> config) throws IllegalArgumentException;
}
