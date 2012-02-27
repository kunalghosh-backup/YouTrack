package ar.edu.itba.it.paw.web.converter;

import org.apache.wicket.util.convert.ConverterLocator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;

@Component
public class TrackerConverterLocator extends ConverterLocator {

	@Autowired
	public TrackerConverterLocator(UserRepository users) {
		super();
		set(DateTime.class, new DateConverter());
		set(Duration.class, new DurationConverter());
		set(ProjectVersion.class, new ProjectVersionConverter());
		set(Task.class, new TaskConverter());
		set(User.class, new MemberConverter(users));

		EnumConverter enumConverter = new EnumConverter();
		set(Project.Status.class, enumConverter);
		set(Task.Priority.class, enumConverter);
		set(Task.Solution.class, enumConverter);
		set(Task.Status.class, enumConverter);
		set(Task.TType.class, enumConverter);
		set(User.Status.class, enumConverter);
	}


}
