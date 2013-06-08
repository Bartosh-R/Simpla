import java.io.InputStream;


final public class ResLoader {

	/**
	 * @param args
	 */
	public static InputStream load(String path) {
		InputStream input = ResLoader.class.getResourceAsStream(path);
		if(input == null)
		{
			input = ResLoader.class.getResourceAsStream("/"+path);
		}
		return input;
	}

}
