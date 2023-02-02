package drudge.data;





public class EmailURLException extends InvalidURLException {


	public EmailURLException(Object o) {
	super(o);
	message = UselessMessages.EMAIL.name();
	}

	public EmailURLException(Object o, Object s) {
	super(o, s);
	message = UselessMessages.EMAIL.name();
	}

}
