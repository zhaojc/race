package thomasb.race.web.dispatch;

import java.util.UUID;

import thomasb.web.handler.RequestHandler;


public interface HandlerRegistry {

	boolean containsKey(Object id);
	
	RequestHandler get(Object id);
	
	RequestHandler putIfAbsent(UUID id, RequestHandler handler);
	
	boolean replace(UUID id, RequestHandler oldHandler, RequestHandler newHandler);
	
	RequestHandler remove(Object id);
}
