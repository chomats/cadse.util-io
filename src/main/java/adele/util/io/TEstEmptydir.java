package adele.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipOutputStream;

public class TEstEmptydir {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String pf = args[0];
		ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(pf));
		HashMap<File, String> files = new HashMap<File, String>();
		files.put(new File(args[1]), "/");
		ZipUtil.zip(files, outputStream);
		outputStream.close();
	}
}
