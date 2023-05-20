package it.discovery.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

@Component
public class DebugHandlerMapping extends AbstractHandlerMapping {
    private final DebugHandler debugHandler = new DebugHandler();

    private final String debugLogPath;

    public DebugHandlerMapping(Environment env) {
        this.debugLogPath = env.getProperty("log.path", "/debug");
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        if (request.getRequestURI().startsWith(debugLogPath)) {
            return debugHandler;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
