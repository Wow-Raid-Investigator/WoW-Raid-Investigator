import listeners.FileUpload;

public class Main {

	public static void main(String[] args) {
		System.out.println("Starting File Upload Socket");
		new FileUpload(7777).start();
		System.out.println("File Upload Socket Started");
	}
}
