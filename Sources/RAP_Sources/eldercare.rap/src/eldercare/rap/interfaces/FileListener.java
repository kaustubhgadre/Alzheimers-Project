package eldercare.rap.interfaces;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileListener {

	public void fileChanged(File file) throws InterruptedException, FileNotFoundException, IOException;
}
