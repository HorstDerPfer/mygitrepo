/**
 * 
 */
package db.training.web.administration;

/**
 * @author michels
 *
 */
public class EditableDisplaytagItem<T> {

    private T id;
    
    private String text;
    
    private boolean readonly;
    
    public EditableDisplaytagItem() {
	setId(null);
	setText(null);
	setReadonly(false);
    }

    public String toString() {
	StringBuilder sb = new StringBuilder(50);
	sb.append("id = ");
	sb.append(getId());
	sb.append("; text = ");
	sb.append(getText());
	sb.append("; isReadonly = ");
	sb.append(isReadonly());
	return sb.toString();
    }
    
    public void setId(T id) {
	this.id = id;
    }

    public T getId() {
	return id;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }

    public void setReadonly(boolean readonly) {
	this.readonly = readonly;
    }

    public boolean isReadonly() {
	return readonly;
    }
}
