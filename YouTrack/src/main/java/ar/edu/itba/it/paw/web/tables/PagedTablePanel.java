package ar.edu.itba.it.paw.web.tables;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.web.base.BasePanel;

public abstract class PagedTablePanel<T> extends BasePanel {

	private static final int itemsPerPage = 10;
	
	public PagedTablePanel(String id, IModel<?> model) {
		super(id, model);
	}

	public PagedTablePanel(String id) {
		super(id);
	}
	
	private void addCommon() {
		final DataView<T> dv = new DataView<T>("elements", getDataProvider(), itemsPerPage) {
			
			@Override
			protected void populateItem(Item<T> item) {
				PagedTablePanel.this.populateItem(item);
			}

			@Override
			public boolean isVisible() {
				return this.getItemCount() > 0;
			}
			
		};
		
		add(new PagingNavigator("paginator", dv) {
			@Override
			public boolean isVisible() {
				return dv.getPageCount() > 1;
			}
		});
		
		add(new Label("emptyMessage", getErrorMessage()) {
			@Override
			public boolean isVisible() {
				return dv.size() == 0;
			}
		});
		
		add(dv);
	}
	
	public PagedTablePanel<T> create() {
		addCommon();
		return this;
	}
	
	public abstract IDataProvider<T> getDataProvider();

	public abstract void populateItem(Item<T> item);
	
	public abstract String getErrorMessage();
	
}
