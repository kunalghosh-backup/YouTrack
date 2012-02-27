package ar.edu.itba.it.paw.web;

import java.util.Locale;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.it.paw.web.auth.LoginPage;
import ar.edu.itba.it.paw.web.common.HibernateWebRequestCycle;
import ar.edu.itba.it.paw.web.converter.TrackerConverterLocator;
import ar.edu.itba.it.paw.web.errorpages.Error404;
import ar.edu.itba.it.paw.web.errorpages.Error500;
import ar.edu.itba.it.paw.web.project.create.CreateEditProject;
import ar.edu.itba.it.paw.web.project.list.ProjectList;

public class TrackerApp extends WebApplication {

	private SessionFactory sessionFactory;

	public static int itemsPerPage = 10;

	private TrackerConverterLocator converterLocator;

	@Autowired
	public TrackerApp(SessionFactory sessionFactory,
			TrackerConverterLocator converterLocator) {
		this.sessionFactory = sessionFactory;
		this.converterLocator = converterLocator;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return ProjectList.class;
	}

	@Override
	protected void init() {
		super.init();
		Locale.setDefault(new Locale("es"));
		
		addComponentInstantiationListener(new SpringComponentInjector(this));

		mountBookmarkablePage("/login", LoginPage.class);
		mountBookmarkablePage("/project/list", ProjectList.class);
		mountBookmarkablePage("/project/create", CreateEditProject.class);
		mountBookmarkablePage("/error404", Error404.class);
		
		getMarkupSettings().setCompressWhitespace(true);
		getApplicationSettings().setAccessDeniedPage(Error404.class);
		getApplicationSettings().setInternalErrorPage(Error500.class);
		getApplicationSettings().setPageExpiredErrorPage(ProjectList.class);
		
	}

	protected IRequestCycleProcessor newRequestCycleProcessor() {
		return new WebRequestCycleProcessor() {
			protected IRequestCodingStrategy newRequestCodingStrategy() {
				return new CryptedUrlWebRequestCodingStrategy(
						new WebRequestCodingStrategy());
			}
		};
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new TrackerSession(request);
	}

	@Override
	public RequestCycle newRequestCycle(Request request, Response response) {
		return new HibernateWebRequestCycle(this, (WebRequest) request,
				response, sessionFactory);
	}

	@Override
	protected IConverterLocator newConverterLocator() {
		return converterLocator;
	}

}
