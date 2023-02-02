package drudge.data;





public class ImageURLException extends InvalidURLException {


	public ImageURLException(Object o) {
	super(o);
	message = UselessMessages.IMAGE.name();
	}

	public ImageURLException(Object o, String s) {
	super(o, s);
	message = UselessMessages.IMAGE.name();
	}

}
