/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
package adele.util.io;

/*------------------------------\
 | Liste des packages a importer |
 \------------------------------*/
// *** Packages de la librairie
// ** Packages util
// ** Packages I/O
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*-----------------------------------\
 |  Declaration de la classe ZipUtil  |
 \-----------------------------------*/
/**
 * It is an utility class for the manipulation of zip files
 * 
 * @author Remy Sanlaville
 * @since 23 janvier 2003
 */
public class ZipUtil {
	// Constante(s) globale(s)

	// Variable(s) globale(s)

	/*-----------------------------------------\
	|       Constructeur(s) de la classe       |
	\-----------------------------------------*/
	/*---------------------------------------\
	|         Interface de la classe         |
	\---------------------------------------*/
	/**
	 * Util: gets information about the given zip file name
	 * 
	 * @param zipFileName
	 *            file name of the zip file to get information
	 * @exception Exception
	 *                an exception occurs
	 */
	public static void getInfo(String zipFileName) throws Exception {
		// Create a ZIP file.
		ZipFile zipfile = new ZipFile(zipFileName);

		System.out.println("\n---------------------------------------------------------");
		System.out.println("Information about the zip file: " + zipFileName);
		System.out.println("---------------------------------------------------------");

		// Get all of the ZIP entries.
		Enumeration zipEntries = zipfile.entries();

		// Print out information.
		ZipEntry currentZipEntry = null;
		while (zipEntries.hasMoreElements()) {
			currentZipEntry = (ZipEntry) zipEntries.nextElement();

			System.out.println();
			if (currentZipEntry.isDirectory()) {
				System.out.println("Directory = " + currentZipEntry.getName());
			} else {
				System.out.println("File = " + currentZipEntry.getName());
			}
			System.out.println("\tcomment = " + currentZipEntry.getComment());
			System.out.println("\ttime = " + new Date(currentZipEntry.getTime()));
			System.out.println("\tsize = " + currentZipEntry.getSize());
			System.out.println("\tcompress size = " + currentZipEntry.getCompressedSize());
			System.out.println("\tcrc = " + currentZipEntry.getCrc());
		}

		zipfile.close();
	}

	/**
	 * Util: gets the name of the entries from the given zip file name
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @return a tabular of the entries name or null if the zip file is empty.
	 * @exception Exception
	 *                an exception occurs
	 */
	public static String[] getEntriesName(String zipFileName) throws Exception {
		// Create a Zip file.
		ZipFile zipFile = new ZipFile(zipFileName);

		String[] tabEntriesName = null;
		int nbEntries = zipFile.size();

		if (nbEntries > 0) {
			// Get all of the Zip entries.
			Enumeration zipEntries = zipFile.entries();

			tabEntriesName = new String[nbEntries];
			ZipEntry currentZipEntry = null;
			int i = 0;

			while (zipEntries.hasMoreElements()) {
				currentZipEntry = (ZipEntry) zipEntries.nextElement();

				// Ajouter le nom de la nouvelle entree
				tabEntriesName[i] = currentZipEntry.getName();

				// Incrementer l'indice
				i++;
			}
		}

		// Fermeture du fichier zip
		zipFile.close();

		return tabEntriesName;
	}

	/**
	 * Util : test if the given file is an entry of the given zip file
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param fileName
	 *            the file name to test
	 * @return true if the given file is an entry of the given zip file, false
	 *         otherwise
	 * @exception IOException
	 *                Description of Exception
	 */
	public static boolean isEntry(String fileName, String zipFileName) throws IOException {
		boolean res = false;
		ZipFile zipFile = null;

		try {
			// Create a Zip file.
			zipFile = new ZipFile(zipFileName);

			// Il y a des problemes avec les separateurs de repertoire.
			// Je ne comprends pas pourquoi. Donc pour assurer, on va tester
			// les deux types de separateurs de fichier.
			// A changer si une solution est trouvee !!!
			String fileNameWindows = fileName.replace('/', '\\');
			String fileNameUnix = fileName.replace('\\', '/');

			// Get the entry zip for the specific file
			ZipEntry zipEntry = zipFile.getEntry(fileNameWindows);

			if (zipEntry != null) {
				res = true;
			} else {
				zipEntry = zipFile.getEntry(fileNameUnix);

				if (zipEntry != null) {
					res = true;
				}
			}
		} finally {
			// Fermeture du fichier zip
			if (zipFile != null) {
				zipFile.close();
			}
		}

		return res;
	}

	/**
	 * Util : unzip a Zip archive file to a given directory
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param destDir
	 *            dest directory
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 * @deprecated
	 */
	@Deprecated
	public static void unzip(String zipFileName, String destDir) throws FileNotFoundException, IOException {
		// Verifier que le repertoire destinataire existe
		// sinon le creer
		File dirDest = new File(destDir);
		if (!dirDest.exists()) {
			// Creation du repertoire d'archive
			dirDest.mkdirs();
		}

		ZipFile zipFile = null;
		InputStream input = null;
		FileOutputStream output = null;

		try {
			// Create a Zip file.
			zipFile = new ZipFile(zipFileName);

			// Get all of the Zip entries.
			Enumeration zipEntries = zipFile.entries();

			ZipEntry currentZipEntry = null;
			File destZipFile = null;

			while (zipEntries.hasMoreElements()) {
				currentZipEntry = (ZipEntry) zipEntries.nextElement();

				// Test if the current entry is a directory
				if (currentZipEntry.isDirectory()) {
					// Create the new directory
					destZipFile = new File(FileUtil.getFullPath(destDir, currentZipEntry.getName()));
					destZipFile.mkdirs();
				} else {
					// Create the new file
					input = zipFile.getInputStream(currentZipEntry);

					destZipFile = new File(destDir, currentZipEntry.getName());
					FileUtil.createNewFile(destZipFile);
					output = new FileOutputStream(destZipFile);

					FileUtil.copy(input, output);
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.flush();
				output.close();
			}

			// Fermeture du fichier zip
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	/**
	 * Util : unzip a specific file in a zip archive file to a given directory
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param destDir
	 *            dest directory
	 * @param fileName
	 *            the file to unzip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void unzip(String fileName, String zipFileName, String destDir) throws FileNotFoundException,
			IOException {
		// Verifier que le repertoire destinataire existe
		// sinon le creer
		File dirDest = new File(destDir);
		if (!dirDest.exists()) {
			// Creation du repertoire d'archive
			dirDest.mkdirs();
		}

		ZipFile zipFile = null;
		InputStream input = null;
		FileOutputStream output = null;

		try {
			// Create a Zip file.
			zipFile = new ZipFile(zipFileName);

			// Il y a des problemes avec les separateurs de repertoire.
			// Je ne comprends pas pourquoi. Donc pour assurer, on va tester
			// les deux types de separateurs de fichier.
			// A changer si une solution est trouvee !!!
			String fileNameWindows = fileName.replace('/', '\\');
			String fileNameUnix = fileName.replace('\\', '/');

			// Get the entry zip for the specific file
			ZipEntry zipEntry = zipFile.getEntry(fileNameWindows);

			if (zipEntry == null) {
				zipEntry = zipFile.getEntry(fileNameUnix);
				if (zipEntry == null) {
					throw new FileNotFoundException("The zip entry " + fileName + " does not exist in the zip file "
							+ zipFileName);
				}
			}

			// Test if the zip entry is a directory
			if (zipEntry.isDirectory()) {
				// Create the new directory
				File destZipDir = new File(FileUtil.getFullPath(destDir, zipEntry.getName()));
				destZipDir.mkdirs();
			} else {
				input = zipFile.getInputStream(zipEntry);

				File destFile = new File(FileUtil.getFullPath(destDir, zipEntry.getName()));
				FileUtil.createNewFile(destFile);
				output = new FileOutputStream(destFile);

				FileUtil.copy(input, output);
			}
		} finally {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.flush();
				output.close();
			}

			// Fermeture du fichier zip
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A vector of String objects for the files and/or directories
	 *            name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(Vector entryFileNames, String zipFileName) throws FileNotFoundException, IOException {
		zip(entryFileNames, zipFileName, new File(""));
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A vector of String objects for the files and/or directories
	 *            name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entries to zip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(Vector entryFileNames, String zipFileName, File baseDir) throws FileNotFoundException,
			IOException {
		// Creation de stream de sortie pour le fichier zip
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileName));

		try {
			// Creation de stream de sortie pour le fichier zip
			outZip = new ZipOutputStream(new FileOutputStream(zipFileName));

			String[] tabEntryFileNames = new String[entryFileNames.size()];
			entryFileNames.toArray(tabEntryFileNames);

			zip(tabEntryFileNames, outZip, baseDir);
		} finally {
			// Fermeture du fichier zip
			if (outZip != null) {
				outZip.close();
			}
		}
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A collection of String objects for the files and/or
	 *            directories name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(Collection entryFileNames, String zipFileName) throws FileNotFoundException, IOException {
		zip(entryFileNames, zipFileName, new File(""));
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A collection of String objects for the files and/or
	 *            directories name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entries to zip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(Collection entryFileNames, String zipFileName, File baseDir) throws FileNotFoundException,
			IOException {
		// Creation de stream de sortie pour le fichier zip
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileName));

		try {
			// Creation de stream de sortie pour le fichier zip
			outZip = new ZipOutputStream(new FileOutputStream(zipFileName));

			String[] tabEntryFileNames = new String[entryFileNames.size()];
			entryFileNames.toArray(tabEntryFileNames);

			zip(tabEntryFileNames, outZip, baseDir);
		} finally {
			// Fermeture du fichier zip
			if (outZip != null) {
				outZip.close();
			}
		}
	}

	/**
	 * Util : create a Zip archive file for the given file or directory.
	 * 
	 * @param entryFileName
	 *            files or directory name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(String entryFileName, String zipFileName) throws FileNotFoundException, IOException {
		String[] entryFileNames = new String[1];
		entryFileNames[0] = entryFileName;

		zip(entryFileNames, zipFileName);
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            An enumeration of the files and/or directories name to add in
	 *            the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(String[] entryFileNames, String zipFileName) throws FileNotFoundException, IOException {
		zip(entryFileNames, zipFileName, new File(""));
	}

	/**
	 * Util : create a Zip archive file for the given file or directory.
	 * 
	 * @param entryFileName
	 *            files or directory name to add in the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entries to zip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(String entryFileName, String zipFileName, File baseDir) throws FileNotFoundException,
			IOException {
		String[] entryFileNames = new String[1];
		entryFileNames[0] = entryFileName;

		zip(entryFileNames, zipFileName, baseDir);
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            An enumeration of the files and/or directories name to add in
	 *            the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entries to zip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(String[] entryFileNames, String zipFileName, File baseDir) throws FileNotFoundException,
			IOException {

		ZipOutputStream outZip = null;

		try {
			// Creation de stream de sortie pour le fichier zip
			outZip = new ZipOutputStream(new FileOutputStream(zipFileName));

			zip(entryFileNames, outZip, baseDir);
		} finally {
			// Fermeture du fichier zip
			if (outZip != null) {
				outZip.close();
			}
		}
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            An enumeration of the files and/or directories name to add in
	 *            the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entryZip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(String[] entryFileNames, ZipOutputStream zipFileName, File baseDir)
			throws FileNotFoundException, IOException {
		String currentFileName = null;
		File currentFile = null;

		// Ajout des fichiers et/ou repertoires dans le fichier zip
		for (int i = 0; i < entryFileNames.length; i++) {
			// Recuperer le nom du fichier courant
			currentFileName = entryFileNames[i];

			// Lecture des informations du fichier courant
			currentFile = new File(currentFileName);

			addEntryZip(currentFile, zipFileName, baseDir, "");
		}
	}

	/**
	 * Util : create a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            An enumeration of the files and/or directories name to add in
	 *            the Zip file
	 * @param zipFileName
	 *            the zip file name to create
	 * @param baseDir
	 *            the base directory for the entryZip
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void zip(Map<File, String> entryFileNames, ZipOutputStream zipFileName) throws FileNotFoundException,
			IOException {
		// Ajout des fichiers et/ou repertoires dans le fichier zip
		for (Map.Entry<File, String> e : entryFileNames.entrySet()) {
			File f = e.getKey();
			if (!f.exists()) {
				continue;
			}

			if (f.isFile()) {
				f = f.getParentFile();
			}
			addEntryZip(e.getKey(), zipFileName, f, e.getValue());
		}
	}

	/**
	 * Util : update a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A collection of String objects for the files and/or
	 *            directories name to update in the Zip file
	 * @param zipFileName
	 *            the zip file name to update
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void updateZip(Collection entryFileNames, String zipFileName) throws FileNotFoundException,
			IOException {
		updateZip(entryFileNames, zipFileName, new File(""));
	}

	/**
	 * Util : update a Zip archive file for the given files and/or directories.
	 * 
	 * @param entryFileNames
	 *            A collection of String objects for the files and/or
	 *            directories name to update in the Zip file
	 * @param zipFileName
	 *            the zip file name to update
	 * @param baseDir
	 *            the base directory for the entries to update
	 * @exception FileNotFoundException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 */
	public static void updateZip(Collection entryFileNames, String zipFileName, File baseDir)
			throws FileNotFoundException, IOException {
		// La mise a jour se fait en trois temps.
		// 1. Unzipper le fichier zip dans un repertoire temporaire
		// 2. Recopier les fichiers et/ou repertoires a mettre a jour
		// 3. Recreer le fichier zip et supprimer le repertoire temporaire
		// Important: il faut tout de meme gerer le cas des repertoires

		// *** Etape 1 : Unzipper le fichier zip dans un repertoire temporaire
		// ** Creation du repertoire temporaire
		TmpUtil tmpDir = new TmpUtil();
		String pathTmpDir = tmpDir.getPath();
		unzip(zipFileName, pathTmpDir);

		// Etape 2 : Recopier les fichiers et/ou repertoires a mettre a jour
		Iterator itEntryFilesNames = entryFileNames.iterator();
		String currentFile = null;
		File srcDir = null;
		File destDir = null;
		String endDestDir = null;
		String baseDirName = null;
		while (itEntryFilesNames.hasNext()) {
			// Recuperer le fichier ou repertoire courant
			currentFile = (String) itEntryFilesNames.next();

			// Gestion du repertoire source
			srcDir = new File(currentFile);
			if (srcDir.isDirectory()) {
				// Cas d'un repertoire

				// Gestion du repertoire destination
				baseDirName = baseDir.getCanonicalPath();
				if (currentFile.startsWith(baseDirName)) {
					endDestDir = currentFile.substring(baseDirName.length() + 1);
					destDir = new File(tmpDir.getFile(), endDestDir);
				} else {
					destDir = tmpDir.getFile();
				}

				FileUtil.copyDir(srcDir, destDir);
			} else {
				// Cas d'un fichier

				// Gestion du repertoire destination
				baseDirName = baseDir.getCanonicalPath();
				if (currentFile.startsWith(baseDirName)) {
					endDestDir = FileUtil.getPath(currentFile.substring(baseDirName.length() + 1));
					destDir = new File(tmpDir.getFile(), endDestDir);
				} else {
					destDir = tmpDir.getFile();
				}

				// Recopier le fichier dans le repertoire temporaire
				// destinataire
				FileUtil.copyToDir(currentFile, destDir.getCanonicalPath());
			}
		}

		// Etape 3 : Recreer le fichier zip
		zip(pathTmpDir, zipFileName, tmpDir.getFile());
		tmpDir.delete();
	}

	/*-------------------------------------\
	|         Methode(s) Privee(s)         |
	\-------------------------------------*/
	/**
	 * Util: Writes an entry in the given zip file. If the entry is a directory
	 * it adds the entries recursively. Entries are named relative to the given
	 * base directory.
	 * 
	 * @param outZip
	 *            the output stream file zip
	 * @param entryZip
	 *            The file or directory to zip
	 * @param baseDir
	 *            the base directory for the entryZip
	 * @exception IOException
	 *                an IOExecption occurs during the zip process
	 */
	private static void addEntryZip(File entryZip, ZipOutputStream outZip, File baseDir, String baseDir2)
			throws IOException {
		// Gestion des fichiers

		String entryZipName = entryZip.getCanonicalPath();
		String baseDirName = baseDir.getCanonicalPath();
		if (entryZipName.startsWith(baseDirName)) {
			if (entryZipName.length() == baseDirName.length()) {
				entryZipName = "";
			} else {
				entryZipName = entryZipName.substring(baseDirName.length() + 1);
			}
		}

		// Imposer que les separateurs de fichiers soient '/', norme des
		// fichiers zips, sinon on ne peut pas retrouver les fichiers
		// classes par le classpath...
		entryZipName = baseDir2 + entryZipName.replace('\\', '/');
		if (entryZip.isDirectory()) {
			// Gestion des repertoires
			File files[] = entryZip.listFiles();
			for (int i = 0; i < files.length; i++) {
				addEntryZip(files[i], outZip, baseDir, baseDir2);
			}
			if (files == null || files.length == 0) {
				addEmptyDirectoryEntryZip(outZip, entryZipName);
			}
		} else {

			// Ajouter la nouvelle entree dans le fichier zip
			addEntryZip(outZip, new FileInputStream(entryZip), entryZipName, entryZip.lastModified());

		}
	}

	/**
	 * Util: Writes an entry in the given zip file. If the entry is a directory
	 * it adds the entries recursively. Entries are named relative to the given
	 * base directory.
	 * 
	 * @param outZip
	 *            the output stream file zip
	 * @param input
	 *            the input stream
	 * @param entryZipName
	 *            the base directory for the entryZip
	 * @exception IOException
	 *                an IOExecption occurs during the zip process
	 */
	public static void addEntryZip(ZipOutputStream outZip, InputStream input, String entryZipName, long time)
			throws IOException, IllegalArgumentException, NullPointerException {
		if (entryZipName == null) {
			throw new NullPointerException();
		}
		if (entryZipName.length() > 0xFFFF) {
			throw new IllegalArgumentException("entry name too long");
		}
		try {
			ZipEntry zipEntry = new ZipEntry(entryZipName);
			if (time != -1) {
				zipEntry.setTime(time);
			}
			outZip.putNextEntry(zipEntry);

			// Copier le fichier a compresser dans le fichier zip
			FileUtil.copy(input, outZip);
		} finally {
			if (input != null) {
				input.close();
			}
			outZip.closeEntry();
		}
	}

	/**
	 * Util: Writes an entry in the given zip file. If the entry is a directory
	 * it adds the entries recursively. Entries are named relative to the given
	 * base directory.
	 * 
	 * @param outZip
	 *            the output stream file zip
	 * @param input
	 *            the input stream
	 * @param entryZipName
	 *            the base directory for the entryZip
	 * @exception IOException
	 *                an IOExecption occurs during the zip process
	 */
	public static void addEmptyDirectoryEntryZip(ZipOutputStream outZip, String entryZipName) throws IOException,
			IllegalArgumentException, NullPointerException {
		if (entryZipName == null) {
			throw new NullPointerException();
		}
		if (entryZipName.length() > 0xFFFF) {
			throw new IllegalArgumentException("entry name too long");
		}
		try {
			ZipEntry zipEntry = new ZipEntry(entryZipName + "/");
			outZip.putNextEntry(zipEntry);
		} finally {
			outZip.closeEntry();
		}
	}

	/*-------------------------------------\
	|                 Main                 |
	\-------------------------------------*/
	/**
	 * Main of the zip utility class
	 * 
	 * @param args
	 *            parameters need to execute the commands
	 */
	public static void main(String args[]) {
		int nbArgs = args.length;

		// Gestion des arguments
		if (nbArgs > 1) {
			if (args[0].equals("-i")) {
				// Gets the information of the zip file
				try {
					getInfo(args[1]);
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}

			if (args[0].equals("-x")) {
				// Extract the zip file
				switch (nbArgs) {
					case 2:
						try {
							unzip(args[1], ".");
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(-1);
						}
						break;
					case 3:
						try {
							unzip(args[1], args[2]);
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(-1);
						}
						break;
					default:
						try {
							for (int i = 3; i < nbArgs; i++) {
								unzip(args[i], args[1], args[2]);
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(-1);
						}
				}
				System.exit(0);
			}

			if (args[0].equals("-z")) {
				// Extract the zip file
				switch (nbArgs) {
					case 2:
						// Missing argument
						System.out.println("[Error] Missing base directory");
						System.out.println("Help ZipUtil");
						System.out.println("\tjava adele.util.io.ZipUtil -i zipFileName");
						System.out.println("\tjava adele.util.io.ZipUtil -x zipFileName [DestDir] [FileToExtract]*");
						System.out.println("\tjava adele.util.io.ZipUtil -z zipFileName baseDir FileDirToZip*");
						System.exit(-1);
						break;
					case 3:
						// Missing argument
						System.out.println("[Error] Missing file(s) or directory(ies) to zip");
						System.out.println("Help ZipUtil");
						System.out.println("\tjava adele.util.io.ZipUtil -i zipFileName");
						System.out.println("\tjava adele.util.io.ZipUtil -x zipFileName [DestDir] [FileToExtract]*");
						System.out.println("\tjava adele.util.io.ZipUtil -z zipFileName baseDir FileDirToZip*");
						System.exit(-1);
						break;
					default:
						try {
							Collection vFiles = new HashSet();
							for (int i = 3; i < nbArgs; i++) {
								vFiles.add(args[i]);
							}
							zip(vFiles, args[1], new File(args[2]));
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(-1);
						}
				}
				System.exit(0);
			}

			// else Bad option
			System.out.println("[Error] option " + args[0] + " unknown");
			System.out.println("Help ZipUtil");
			System.out.println("\tjava adele.util.io.ZipUtil -i zipFileName");
			System.out.println("\tjava adele.util.io.ZipUtil -x zipFileName [DestDir] [FileToExtract]*");
			System.out.println("\tjava adele.util.io.ZipUtil -z zipFileName baseDir FileDirToZip*");
			System.exit(-1);
		} else {
			System.out.println("[Error] Bad command line");
			System.out.println("Help ZipUtil");
			System.out.println("\tjava adele.util.io.ZipUtil -i zipFileName");
			System.out.println("\tjava adele.util.io.ZipUtil -x zipFileName [DestDir] [FileToExtract]*");
			System.out.println("\tjava adele.util.io.ZipUtil -z zipFileName baseDir FileDirToZip*");
			System.exit(-1);
		}
	}

	private static final int	BUFFER_SIZE	= 2048;

	/**
	 * Utility method to zip a directory hierarchy recursively into a single
	 * compressed file.
	 * 
	 * @param exclusion
	 *            can be null
	 * 
	 */
	public static void zipDirectory(File directory, File zipFile, Pattern exclusion) {
		zipDirectory(directory, zipFile, new byte[BUFFER_SIZE], exclusion);
	}

	/**
	 * Utility method to zip a directory hierarchy recursively into a single
	 * compressed file.
	 * 
	 * This variant allows the caller to speciy the buffer to be used, so that
	 * the same buffer can be reused for different calls.
	 * 
	 * @param exclusion
	 *            can be null
	 */
	public static void zipDirectory(File directory, File zipFile, byte buffer[], Pattern exclusion) {

		try {
			zipFile.getParentFile().mkdirs();

			FileOutputStream out = new FileOutputStream(zipFile);
			ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(out));

			zipDirectory(directory, zip, buffer, exclusion);

			zip.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO: rewrite doc Writes an entry in the ZipFile, If the entry is a
	 * directory it adds the entries recursively. Entries are named relative to
	 * the base directory specified.
	 * 
	 * A single data buffer is used to avoid recursive creation of buffer
	 * objects, so concurrent execution of this method is disallowed using the
	 * same buffer.
	 * 
	 * @param exclusion
	 *            can be null
	 */
	public static void zipDirectory(File base, ZipOutputStream zip, byte buffer[], Pattern exclusion) {
		List stack = new ArrayList();
		String baseName = base.getAbsolutePath();
		ZipEntry zipEntry;
		String entryName;

		File entry = base;

		int i = 0;

		stack.add(new File[] { base.getAbsoluteFile() });
		synchronized (buffer) {
			while (stack.size() > i) {

				File[] files = (File[]) stack.get(i);
				stack.set(i, null);
				i++;
				for (int j = 0; j < files.length; j++) {
					try {
						entry = files[j];
						entryName = entry.getAbsolutePath();
						if (entryName.length() == baseName.length()) {
							entryName = "";
						} else {
							entryName = entryName.substring(baseName.length() + 1);
						}

						entryName = entryName.replace(File.separatorChar, '/');
						if (entry.isDirectory()) {
							entryName = entryName + "/";
						}

						if ((exclusion != null) && exclusion.matcher(entryName).matches()) {
							continue;
						}

						zipEntry = new ZipEntry(entryName);
						zipEntry.setTime(entry.lastModified());
						zip.putNextEntry(zipEntry);

						if (entry.isDirectory()) { /*
													 * Add a new directory entry
													 * to the zip file
													 */

							/*
							 * Recursively transverse the hierarchy (deep
							 * transversal)
							 */
							stack.add(entry.listFiles());
						} else {
							/* Add a new entry to the zip file */
							FileInputStream file = new FileInputStream(entry);

							int count;
							while ((count = file.read(buffer, 0, buffer.length)) != -1) {
								zip.write(buffer, 0, count);
							}
							file.close();

						}
						zip.closeEntry();
					} catch (Exception io) {
						System.out.println("Add Entry " + base.getAbsolutePath() + " " + entry.getAbsolutePath());
						io.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Utility method to unzip a compressed file into a directory hierarchy.
	 * 
	 * @throws IOException
	 * 
	 */
	public static void unzipFile(File zipFile, File directory) throws IOException {
		if (directory.isFile()) {
			return;
		}

		

		if (!directory.exists()) {
			directory.mkdirs();
		}

		FileInputStream input = new FileInputStream(zipFile);
		unzip( input, directory);

	}

	/**
	 * Utility method to unzip a compressed file into a directory hierarchy.
	 * 
	 * @throws IOException
	 * 
	 */
	public static void unzip(InputStream input, File directory) throws IOException {
		
		Map set_date = new HashMap();
		byte buffer[] = new byte[BUFFER_SIZE];
		ZipInputStream zip = new ZipInputStream(new BufferedInputStream(input));

		ZipEntry zipEntry = null;

		while ((zipEntry = zip.getNextEntry()) != null) {

			try {
				File fileEntry = new File(directory, zipEntry.getName());

				if (zipEntry.isDirectory()) {
					fileEntry.mkdirs();
					fileEntry.setLastModified(zipEntry.getTime());
					set_date.put(fileEntry, new Long(zipEntry.getTime()));
				} else {
					/*
					 * Create the file if it doesn't exist
					 */
					if (!fileEntry.exists()) {
						fileEntry.getParentFile().mkdirs();
						fileEntry.createNewFile();
					}
					FileOutputStream file = new FileOutputStream(fileEntry);

					int count;
					while ((count = zip.read(buffer, 0, BUFFER_SIZE)) != -1) {
						file.write(buffer, 0, count);
					}
					file.flush();
					file.close();
					fileEntry.setLastModified(zipEntry.getTime());
				}
			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				zip.closeEntry();
			}
		}

		for (Iterator iter = set_date.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			((File) e.getKey()).setLastModified(((Long) e.getValue()).longValue());
		}

		zip.close();
	}
}
