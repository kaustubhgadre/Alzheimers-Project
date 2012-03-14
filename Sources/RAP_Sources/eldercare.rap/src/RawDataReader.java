import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import eldercare.rap.interfaces.FileListener;

public class RawDataReader implements FileListener {

	final static File rawFile = new File("C:\\temp\\signal.txt");

	public static void main(String[] args) throws IOException,
			InterruptedException {
		RandomAccessFile ra = new RandomAccessFile(rawFile, "r");
		byte[] readBuff = new byte[38];
		while (true) {
			while (!((ra.read(readBuff)) == -1)) {
				System.out.println(new String(readBuff));
				ra.seek(ra.getFilePointer());
				//ra.close();
				Thread.sleep(1000);
			}
		}
	}

	private long old;

	@Override
	public void fileChanged(File file) throws InterruptedException, IOException {
		RandomAccessFile ra = new RandomAccessFile(file, "r");
		ra.seek(old);
		byte[] readBuff = new byte[38];
		while (!((ra.read(readBuff)) == -1)) {
			System.out.println(new String(readBuff));
			ra.seek(ra.getFilePointer());
			Thread.sleep(1000);
		}
		old = ra.length();
		ra.close();
	}
}
