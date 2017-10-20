package pt.example.bootstrap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class conf {

	public static String filePath = "jdbc:datadirect:openedge://192.168.40.112:20612;DatabaseName=silv-exp;User=SYSPROGRESS;Password=SYSPROGRESS;";

	private static final String FILENAME = "C:\\sgiid_dev\\conf\\filename.txt";

	/*public static void teste() {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			// br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] parts = sCurrentLine.split(":");
				System.out.println(parts[0]);
				System.out.println(parts[1]);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}*/

}
