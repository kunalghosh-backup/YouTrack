package ar.edu.itba.it.paw.web.common.label;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

public class DataLabel extends Label {

	private IModel<?> emptyTextModel;
	
	public DataLabel(final String id) {
		this(id, (IModel<String>) null);
	}
	
	public DataLabel(final String id, String label) {
		this(id, new Model<String>(label));
	}
	
	public DataLabel(final String id, IModel<?> model) {
		super(id, model);
	}
	
	public DataLabel setEmptyText(IModel<?> emptyTextModel) {
		this.emptyTextModel = emptyTextModel;
		return this;
	}
	
	public DataLabel setEmptyText(String emptyText) {
		return setEmptyText(Model.of(emptyText));
	}
	
	public boolean isEmpty() {
		String dmo = getDefaultModelObjectAsString();
		return dmo == null ? false : dmo.isEmpty();
	}
	
	public String getCssClass() {
		return isEmpty() ? "emptyDataLabel" : null;
	}

	@Override
	protected void onDetach() {
		if(null != this.emptyTextModel) {
			this.emptyTextModel.detach();
		}
		super.onDetach();
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		String str = getDefaultModelObjectAsString();
		if(this.emptyTextModel != null && !ValidationUtils.hasText(str)) {
			str = getDefaultModelObjectAsString(this.emptyTextModel.getObject());
		}
		replaceComponentTagBody(markupStream, openTag, str);
	}
	
}
