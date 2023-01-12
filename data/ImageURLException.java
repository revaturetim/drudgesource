package drudge.data;





public class ImageURLException extends InvalidURLException {


	public ImageURLException(Object o) {
	super(o);
	message = UselessMessages.IMAGE.mes;
	}

	public ImageURLException(Object o, String s) {
	super(o, s);

	}

}
