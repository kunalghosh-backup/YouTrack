package ar.edu.itba.it.paw.web.common.progressbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ProgressBar extends Panel {

	public ProgressBar(String id, final ProgressModel progressModel) {
		super(id, progressModel);
		
		add(new WebMarkupContainer("bar").add(new AttributeModifier("style", true, new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				ProgressModel pm = (ProgressModel) getDefaultModel();
				return "width: " + pm.getObject().getProgress() + "%;";
			}
			
		})));
		
	}

	
	
}
