package drudge.data;





public class EmailURLException extends InvalidURLException {


	public EmailURLException(Object o) {
	super(o);
	message = UselessMessages.EMAIL.mes;
	}

	public EmailURLException(Object o, Object s) {
	super(o, s);
	message = UselessMessages.EMAIL.mes;
	}

}
