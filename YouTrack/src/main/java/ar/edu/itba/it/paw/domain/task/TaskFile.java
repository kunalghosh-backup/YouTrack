package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Entity;
import javax.persistence.Lob;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;

@Entity
public class TaskFile extends PersistentEntity {
	
	@Lob
	private byte[] bytes;
	private String filename;
	private String contentType;
	
	TaskFile() {
	}
	
	public TaskFile( byte[] bytes, String filename, String contentType) {
		this.bytes = bytes;
		this.filename = filename;
		this.contentType = contentType;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getBytes(){
		return this.bytes;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskFile other = (TaskFile) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		return true;
	}


	
	
}
