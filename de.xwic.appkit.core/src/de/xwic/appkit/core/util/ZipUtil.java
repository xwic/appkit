/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.platform.zip.ZipUtil
 * Created on Jun 12, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Usefull class for zipping/unzipping operations on a given input.
 * @author Aron Cotrau
 */
public class ZipUtil {

	private static int BUFFER_SIZE = 1024;
	private static byte[] BUFFER = new byte[BUFFER_SIZE];

	private static final Log log = LogFactory.getLog(ZipUtil.class);

	/**
	 * Zips the files array into a file that has the name given as parameter.
	 * 
	 * @param files
	 *            the files array
	 * @param zipFileName
	 *            the name for the zip file
	 * @return the new zipped file
	 * @throws IOException
	 */
	public static File zip(File[] files, String zipFileName) throws IOException {

		FileOutputStream stream = null;
		ZipOutputStream out = null;
		File archiveFile = null;

		try {

			if (!zipFileName.endsWith(".zip")) {
				zipFileName = zipFileName + ".zip";
			}
			
			archiveFile = new File(zipFileName);
			byte buffer[] = new byte[BUFFER_SIZE];

			// Open archive file
			stream = new FileOutputStream(archiveFile);
			out = new ZipOutputStream(stream);

			for (int i = 0; i < files.length; i++) {

				if (null == files[i] || !files[i].exists() || files[i].isDirectory()) {
					continue;
				}

				log.info("Zipping " + files[i].getName());

				// Add archive entry
				ZipEntry zipAdd = new ZipEntry(files[i].getName());
				zipAdd.setTime(files[i].lastModified());
				out.putNextEntry(zipAdd);

				// Read input & write to output
				FileInputStream in = new FileInputStream(files[i]);
				while (true) {

					int nRead = in.read(buffer, 0, buffer.length);

					if (nRead <= 0) {
						break;
					}

					out.write(buffer, 0, nRead);
				}

				in.close();
			}

		} catch (IOException e) {
			
			log.error("Error: " + e.getMessage(), e);
			throw e;

		} finally {
			
			try {
				
				if (null != out) {
					out.close();
				}

				if (null != stream) {
					stream.close();
				}
			} catch (IOException e) {
				log.error("Error: " + e.getMessage(), e);
				throw e;
			}
			
		}

		return archiveFile;

	}

	/**
	 * Unzips the files from the zipped file into the destination folder.
	 * 
	 * @param zippedFile
	 *            the files array
	 * @param destinationFolder
	 *            the folder in which the zip archive will be unzipped
	 * @return the file array which consists into the files which were zipped in
	 *         the zippedFile
	 * @throws IOException
	 */
	public static File[] unzip(File zippedFile, String destinationFolder) throws IOException {

		ZipFile zipFile = null;
		List<File> files = new ArrayList<File>();
		
		try {

			zipFile = new ZipFile(zippedFile);
			Enumeration<?> entries = zipFile.entries();

			while (entries.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (!entry.isDirectory()) {

					String filePath = destinationFolder + System.getProperty("file.separator") + entry.getName();
					FileOutputStream stream = new FileOutputStream(filePath);

					InputStream is = zipFile.getInputStream(entry);

					log.info("Unzipping " + entry.getName());

					int n = 0;
					while ((n = is.read(BUFFER)) > 0) {
						stream.write(BUFFER, 0, n);
					}

					is.close();
					stream.close();

					files.add(new File(filePath));
					
				}

			}

			zipFile.close();
		} catch (IOException e) {

			log.error("Error: " + e.getMessage(), e);
			throw e;

		} finally {

			try {

				if (null != zipFile) {
					zipFile.close();
				}

			} catch (IOException e) {
				log.error("Error: " + e.getMessage(), e);
				throw e;
			}
			
		}

		File[] array = files.toArray(new File[files.size()]);
		return array;
	}

}
