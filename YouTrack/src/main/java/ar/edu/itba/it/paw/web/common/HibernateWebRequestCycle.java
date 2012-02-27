package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.springframework.util.Assert;

/**
 * Web request that opens a session and a transaction for each request and commits it if at request end or makes a rollback if there was a
 * problem.
 */
public class HibernateWebRequestCycle extends WebRequestCycle {

	private final SessionFactory sessionFactory;
	private boolean error;

	public HibernateWebRequestCycle(WebApplication application, WebRequest request, Response response, SessionFactory sessionFactory) {
		super(application, request, response);
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Returns the request cycle associated with this thread
	 */
	public static HibernateWebRequestCycle get() {
		RequestCycle result = RequestCycle.get();
		Assert.state(result instanceof HibernateWebRequestCycle, "Request cycle is not an instance of HibernateWebRequestCycle, but "
				+ result.getClass());
		return (HibernateWebRequestCycle) WebRequestCycle.get();
	}

	@Override
	protected void onBeginRequest() {
		begin();
		super.onBeginRequest();
	}

	@Override
	protected void onEndRequest() {
		if (!error) {
			commit();
		}
		super.onEndRequest();
	}

	@Override
	public Page onRuntimeException(Page page, RuntimeException e) {
		rollback();
		error = true;
		return super.onRuntimeException(page, e);
	}

	/**
	 * Start a new session. Will fail if a session is opened in the context of the current thread
	 */
	public void begin() {
		Assert.state(!ManagedSessionContext.hasBind(sessionFactory), "Session already bound to this thread");

		Session session = sessionFactory.openSession();
		ManagedSessionContext.bind(session);
		session.beginTransaction();
	}

	/**
	 * Commit current session work and close session
	 * <p>
	 * If you need to commit, but keep the session opened, use method <code>checkpoint</code> instead
	 * </p>
	 */
	public void commit() {
		Session session = sessionFactory.getCurrentSession();
		Assert.state(session.isOpen(), "Can't commit a closed session!");
		try {
			commit(session);
		} finally {
			close(session);
		}
	}

	/**
	 * Rollback current session work and close session
	 * <p>
	 * If you need to rollback, but keep the session opened, use method <code>restart</code> instead
	 * </p>
	 */
	public void rollback() {
		Session session = sessionFactory.getCurrentSession();
		Assert.state(session.isOpen(), "Can't rollback a closed session!");
		try {
			rollback(session);
		} finally {
			close(session);
		}
	}

	private void commit(Session session) {
		Transaction tx = session.getTransaction();
		if (tx.isActive()) {
			session.flush();
			tx.commit();
		}
	}

	private void rollback(Session session) {
		Transaction tx = session.getTransaction();
		if (tx.isActive()) {
			tx.rollback();
		}
	}

	private void close(Session session) {
		ManagedSessionContext.unbind(sessionFactory);
		session.close();
	}
}
