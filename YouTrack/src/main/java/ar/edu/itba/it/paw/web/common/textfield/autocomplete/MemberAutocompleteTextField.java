package ar.edu.itba.it.paw.web.common.textfield.autocomplete;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.common.textfield.autocomplete.renderer.UserAutoCompleteConverterRenderer;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class MemberAutocompleteTextField extends AutoCompleteTextField<User> {

	
	public MemberAutocompleteTextField(String id) {
		super(id, new UserAutoCompleteConverterRenderer());
		addCommon();
	}
	
	public MemberAutocompleteTextField(String id, IModel<User> selected) {
		super(id, selected, new UserAutoCompleteConverterRenderer());
		addCommon();
	}
	
	private void addCommon() {
		add(CSSPackageResource.getHeaderContribution(MemberAutocompleteTextField.class, "autocomplete.css"));
	}

	@Override
	protected Iterator<User> getChoices(String input) {
		Iterable<User> aux = WebUtils.getCurrentProject().getMembersWithPrefix(input);
		List<User> ret = new LinkedList<User>();
		for(User u : aux) {
			ret.add(u);
		}
		return ret.iterator();
	}

}
