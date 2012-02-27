package ar.edu.itba.it.paw.domain.utils;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;

import com.google.common.collect.Iterables;


public class ValidationUtils {

	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	public static boolean hasNull(Object ... objs) {
		for(Object o : objs) {
			if(o == null) {
				return true;
			}
		}
		return false;
 	}
	
	public static boolean hasAllNull(Object ... objs) {
		for(Object o : objs) {
			if(o != null) {
				return false;
			}
		}
		return true;
 	}
	
	public static boolean hasEmptyValues(String  ... strs) {
		for(String s : strs) {
			if(s.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static void checkNotNull(Object o, String message) {
		if(o == null) {
			throw new DomainException(message);
		}
	}

	public static boolean hasText(String text) {
		if (!isNull(text) && !text.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean between(String text, int min, int max) {
		int aux;
		if (!isNull(text) && (aux = text.length()) >= min && aux <= max) {
			return true;
		}
		return false;
	}

	public static boolean minLength(String text, int minLength) {
		if (!isNull(text) && text.length() >= minLength) {
			return true;
		}
		return false;
	}

	public static boolean maxLength(String text, int maxLength) {
		if (!isNull(text) && text.length() <= maxLength) {
			return true;
		}
		return false;
	}

	public static boolean isAdminUser(User user) {
		return user != null && !user.isBanned() && user.isAdmin();
	}

	public static void checkAdminUser(User user) {
		if (!isAdminUser(user)) {
			throw new PrivilegeException("The user is not an admin, or is banned");
		}
	}

	public static boolean isAdminOrLeader(User user, Project project) {
		return !ValidationUtils.isNull(user) && !user.isBanned() && ((!ValidationUtils.isNull(project) && project.isLeader(user)) || user.isAdmin());
	}
	
	public static void checkAdminOrLeader(User user, Project project) {
		if (!isAdminOrLeader(user, project)) {
			throw new PrivilegeException("The user must be the leader or an admin");
		}
	}
	
	public static boolean isAdminOrMember(User user, Project project) {
		return !ValidationUtils.isNull(user) && !user.isBanned() && (project.isMember(user) || user.isAdmin());
	}
	
	public static void checkAdminOrMember(User user, Project project) {
		if(!isAdminOrMember(user, project)) {
			throw new PrivilegeException("The user must be a member or an admin");
		}
	}
	
	public static<T> boolean isEmpty(Collection<T> collection){
		return collection.isEmpty();
	}

	public static boolean isAnEmail(String email) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
		CharSequence inputStr = email;  
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(inputStr);  
		if(matcher.matches()){  
			return true;  
		}  
		return false;  
	}
	
	public static <T> boolean areDifferent(T value1, T value2) {
		if(isNull(value1) && isNull(value2)) {
			return false;
		}else if(!isNull(value1)) {
			return !value1.equals(value2);
		}else{
			return !value2.equals(value1);
		}
	}
	
	public static <T> boolean areEquals(Iterable<T> c1, Iterable<T> c2) {
		if((ValidationUtils.isNull(c1) || Iterables.isEmpty(c1)) && (ValidationUtils.isNull(c2) || Iterables.isEmpty(c2))) {
			return true;
		}else if(ValidationUtils.isNull(c1) || ValidationUtils.isNull(c2)){
			return false;
		}else{
			return _areEquals(c1, c2);
		}
	}
	
	private static <T> boolean _areEquals(Iterable<T> c1, Iterable<T> c2) {
		for(T a1 : c1) {
			if(!Iterables.contains(c2, a1)) {
				return false;
			}
		}
		for(T a2 : c2) {
			if(!Iterables.contains(c1, a2)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isMemberOrIsPublicProject(Project p, User u) {
		return p.isPublic() ? true : p.isMember(u);
	}

	public static <T> boolean emptyList(List<T> list) {
		return list == null || list.isEmpty();
	}
} 
