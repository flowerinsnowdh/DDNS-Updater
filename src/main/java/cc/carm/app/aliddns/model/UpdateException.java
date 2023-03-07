package cc.carm.app.aliddns.model;

/**
 * 当更新记录时出现非运行时异常时抛出
 */
public class UpdateException extends Exception {
    public UpdateException() {
        super();
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }
}
