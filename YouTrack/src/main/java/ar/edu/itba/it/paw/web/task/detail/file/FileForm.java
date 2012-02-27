package ar.edu.itba.it.paw.web.task.detail.file;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Bytes;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskFile;
import ar.edu.itba.it.paw.web.base.BasePanel;

public class FileForm extends BasePanel {

	private transient FileUpload fileUpload;
	
	public FileForm(String id, final IModel<Task> taskModel) {
		super(id, taskModel);

		Form<FileForm> form = new Form<FileForm>("fileForm", new CompoundPropertyModel<FileForm>(this)) {
			@Override
			protected void onSubmit() {
				TaskFile tf = new TaskFile(fileUpload.getBytes(), fileUpload.getClientFileName(), fileUpload.getContentType());
				taskModel.getObject().addTaskFile(tf);
			}
		};
		
		form.setMultiPart(true);
		form.setMaxSize(Bytes.megabytes(1));
		
		form.add(new FileUploadField("fileUpload").setRequired(true));
		
		add(form);
	}

}
