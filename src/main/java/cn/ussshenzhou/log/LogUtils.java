package cn.ussshenzhou.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USS_Shenzhou
 */
public class LogUtils {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * Caller sensitive, DO NOT WRAP
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
    }
}
