package ar.edu.itba.it.paw.web.tables;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;

public class CoolAjaxFallbackDataTable<T> extends DataTable<T>{

	@SuppressWarnings("unchecked")
	public CoolAjaxFallbackDataTable(String id, List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, int rowsPerPage) {
		super(id, columns.toArray((IColumn<T>[])new IColumn[columns.size()]), dataProvider, rowsPerPage);
		setOutputMarkupId(true);
		setVersioned(true);
		addTopToolbar(new AjaxFallbackHeadersToolbar(this, dataProvider));
		addBottomToolbar(new AjaxNavigationToolbar(this));
		addBottomToolbar(new NoRecordsToolbar(this));
	}

}
