package thomasb.web.clocking;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A {@code ClockedRequestHandler} synchronizes between request of its participants.
 * 
 * The participants are supposed to send requests in fixed time intervals. For
 * each time interval the responses to the participants are delayed until the
 * request of the last participant arrives or a timeout limit is reached.
 * 
 * Participants must specify the count of the intended time interval in their
 * request and must not send a request to the succeeding interval before they
 * received the response from the server or a given timeout is reached.
 */
public final class ClockedRequestHandler {
	public static final String TIME_PARAMETER = ClockedRequest.TIME_PARAMETER;
	
	private final ClockedSubmission<?> clockedSubmission;
	private final CountDownLatch startLatch;
	private boolean init = true;
	
	public ClockedRequestHandler(int participants,
			int interval,
			ClockedRequestProcessor<?> requestProcessor) {
		this.clockedSubmission = new ClockedSubmission<>(participants, interval, requestProcessor);
		this.startLatch = new CountDownLatch(participants);
		this.clockedSubmission.init();
	}
	
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (init) {
			try {
				awaitParticipants();
			} catch (InterruptedException e) {
				return;
			}
			init = false;
		}
		
		AsyncContext async = request.startAsync(request, response);
		clockedSubmission.addRequest(async);
	}

	private void awaitParticipants() throws InterruptedException {
		startLatch.countDown();
		startLatch.await();
		clockedSubmission.launch();
	}
}