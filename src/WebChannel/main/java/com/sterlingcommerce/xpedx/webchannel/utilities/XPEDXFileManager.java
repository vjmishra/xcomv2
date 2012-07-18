package com.sterlingcommerce.xpedx.webchannel.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;

import com.sterlingcommerce.webchannel.core.IWCContext;
import com.yantra.yfc.util.YFCCommon;

/**
 * XPEDXFileManager class facilitates IO related processes like creating a directory if it
 * does not exists. Copying a directory and its contents or the only the file that has changed.
 */
public class XPEDXFileManager implements Serializable{

	private static final long serialVersionUID = 2946388974868820775L;
	private static String PATH_SEPARATOR = System.getProperty("file.separator");

	private static final Logger LOG = Logger.getLogger(XPEDXFileManager.class);
	private static final boolean DEBUG_TRACE = false;

	
	/**
	 * This method checks if a shared Windows remote path is given for a directory.
	 * @param dirPath is the name of the directory.
	 * @return true if the path is remote shared location; otherwise false.
	 */
	public static boolean isDirectoryRemote(String dirPath){
		boolean rtn = false;
		if (dirPath.startsWith("\\\\") || dirPath.startsWith("//")){
			rtn = true;
		}
		return rtn;
	}
	
	/**
	 * This method creates the directory specified. If the directory exists it logs it.
	 * If the directory is a path then it creates all the directories in the path. If any
	 * of the directories exists it logs it.
	 * 
	 * @param dir is of type String which need to be created.
	 */
	public static void createDirectory(String dirName){
		createDirectory(new File(dirName));
	}

	/**
	 * This method creates the directory specified. If the directory exists it logs it.
	 * If the directory is a path then it creates all the directories in the path. If any
	 * of the directories exists it logs it.
	 * 
	 * @param dir is of type File which need to be created.
	 */
	public static void createDirectory(File dir){
		if (!dir.getPath().contains("/")){
			if (!dir.exists()){
				dir.mkdir();
			}
			else {
				LOG.debug(dir.getName() + "already exists");
			}
		}
		else {
			String[] dirNames = dir.getPath().split("/");
			
			for (int i=0; i < dirNames.length; i++){
				File sDir = new File (dirNames[i]);
				if (!sDir.exists()){
					sDir.mkdir();
				}
				else {
					LOG.debug(sDir.getName() + "already exists");
				}
			}
		}
	}
	
	/**
	 * This method copies files from the source directory to the destination directory. If the destination
	 * directory does not exists it creates them.
	 *  
	 * @param srcDirPath is the path of the source directory.
	 * @param destDirPath is the path of the destination directory.
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @throws IOException
	 */
	public static void copyDirectory(String srcDirPath, String destDirPath, boolean forceCopy) throws IOException{
		copyDirectory(new File(srcDirPath), new File(destDirPath), forceCopy);
	}

	/**
	 * This method copies files from the source directory to the destination directory. If the destination
	 * directory does not exists it creates them. The destination directory is fetched from the ServletContext's
	 * real path.
	 * 
	 * @param srcDirPath is the path of the source directory.
	 * @param wcContext is of the type IWCContext. 
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @throws IOException
	 */
	public static void copyDirectory(String srcDirName, IWCContext wcContext, boolean forceCopy) throws IOException{
		copyDirectory(srcDirName, wcContext.getSCUIContext().getServletContext(), forceCopy);
	}
	
	/**
	 * This method copies files from the source directory to the destination directory. If the destination
	 * directory does not exists it creates them. The destination directory is fetched from the ServletContext's
	 * real path.
	 * 
	 * @param srcDirPath is the path of the source directory.
	 * @param context is of the type ServletContext. 
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @throws IOException
	 */
	public static void copyDirectory(String srcDirName, ServletContext context, boolean forceCopy) throws IOException{
		// any filename would be fine, it is just to get the actual path of the context.
		String destFilePath = context.getRealPath("test.xml");
		if (YFCCommon.isVoid(destFilePath)){
			logMessage("-DYN-PROMO- (copyDirectory) 10 WebLogic Servlet real path came back as Empty or Null");
			return;
		}	
		
		String destDirName = destFilePath.substring(0, destFilePath.lastIndexOf(PATH_SEPARATOR));
		
		logMessage("-DYN-PROMO- (copyDirectory) 20 srcDirName : " + srcDirName);
		logMessage("-DYN-PROMO- (copyDirectory) 30 Valiadated WebLogic Servlet) destDirName : " + destDirName);
		
		copyDirectory(new File(srcDirName), new File(destDirName), forceCopy);
	}

	/**
	 * This method copies files from the source directory to the destination directory. If the destination
	 * directory does not exists it creates them.
	 *  
	 * @param srcDirPath is the path of the source directory.
	 * @param destDirPath is the path of the destination directory.
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir, boolean forceCopy) throws IOException{
		if (!srcDir.exists()){
			//should not copy source directory that does not exist
			logMessage("-DYN-PROMO- (copyDirectory) 40 Source Dir not exists copy Failed.");
			return;			
		}

		String srcDirPath = srcDir.getPath();

		// if the source dir is inside context path and forceCopy is not true do not copy
		if (srcDirPath.startsWith(destDir.getPath()) && !forceCopy){
			logMessage("-DYN-PROMO- (copyDirectory) 50 source dir is inside context path and forceCopy is false, So DONOT copy.");
			return;
		}
		
		boolean isDriveGiven = false;
		// For windows if the file path starts with drive and :, then remove them before appending to the destination
		if (srcDirPath.contains(":")){
			srcDirPath = srcDirPath.substring(srcDirPath.indexOf(PATH_SEPARATOR), srcDirPath.length());
			isDriveGiven = true;
			
		}

		String destDirName = destDir.getPath() + srcDirPath;
		destDirName = destDirName.replace("\\", "/");
		destDirName = destDirName.replace("//", "/");
		File destDirectory = new File(destDirName);
		
	    /*
	     // this is commented because the folder is modified only when files are added or removed but
	     // not when they are modified.
	     if (destDirectory.exists()){
			if (!(srcDir.lastModified() > destDirectory.lastModified())){
				return;
			}
		}*/
		
		if (!isDirectoryRemote(srcDir.getPath()) &&  !isDriveGiven){
			
			//should not copy source directory that is not remote or if the path is within a webapp context
			logMessage("-DYN-PROMO- 60 .. (copyDirectory) should not copy source directory that is not remote or if the path is within a webapp context " + srcDir );
			return;		
		}
		// this apache.io.commons.FileUtils copies source to destination and preserves the timestamp on the files and folders
		FileUtils.copyDirectory(srcDir, destDirectory, true);
		logMessage("-DYN-PROMO- (copyDirectory) 70 srcDir : " + srcDir );
		logMessage("-DYN-PROMO- (copyDirectory) 80 destDirectory : " + destDirectory );
	}
	
	/**
	 * This method checks if the file specified by the given path exists in the given search directory.
	 * If exists and it is a remote file, it checks for modification. If the file is modified then it
	 * copies the latest file into the search directory and returns true.
	 * If the file does not exist it returns false.  
	 * The search directory is fetched from the ServletContext's real path. 
	 * 
	 * @param filePath is the requested filePath which could be relative to the context.
	 * @param wcContext is of the type IWCContext. 
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @return true if the file exists; otherwise false.
	 * @throws IOException
	 */
	public static boolean checkFile(String filePath, IWCContext wcContext, boolean forceCopy) throws IOException{
		// any filename would be fine, it is just to get the actual path of the context.
		String searchDir = wcContext.getSCUIContext().getServletContext().getRealPath("test.xml");
		logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 210 searchDir " + searchDir);
		logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 220 filePath " + filePath);
		if (YFCCommon.isVoid(searchDir)){
			return false;
		}
		searchDir = searchDir.substring(0, searchDir.lastIndexOf(PATH_SEPARATOR));
		return checkFile(filePath, searchDir, forceCopy);
	}
	
	/**
	 * This method checks if the file specified by the given path exists in the given search directory.
	 * If exists and it is a remote file, it checks for modification. If the file is modified then 
	 * it copies the latest file into the search directory and returns true.
	 * If the file does not exist it returns false.
	 * 
	 *   
	 * @param filePath is the requested filePath which could be relative to the context.
	 * @param searchDir is the directory where the file need to be located.
	 * @param forceCopy if true will copy the directory even if the path or same; otherwise will ignore.
	 * @return true if the file exists; otherwise false.
	 * @throws IOException
	 */
	public static boolean checkFile(String filePath, String searchDir, boolean forceCopy) throws IOException{
		File givenFile = new File(filePath);
		
		logMessage("-DYN-PROMO- (XPEDXFileManager)  ");
		
		// if the source dir is inside context path and forceCopy is not true do not copy
		if (filePath.startsWith(searchDir) && !forceCopy){
			logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 230.. dir is inside context path and forceCopy is not true do not copy");
			return givenFile.exists();
		}
		else
		{
			logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 240 .. source dir is not inside .. promotions");
		}
		
		boolean isDriveGiven = false;
		// For windows if the file path starts with drive and :, then remove them before appending to the destination
		if (filePath.contains(":")){
			isDriveGiven = true;
		}
		
		if (isDirectoryRemote(filePath) || isDriveGiven){
			logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 250 .. Copying the File/Dir to promotions");
			return copyFile(filePath,searchDir);
		}
		
		if (!filePath.startsWith("/")){
			filePath = "/" + filePath;
		}
		File file = new File(searchDir + filePath);
		logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 260 .. (searchDir + filePath)" +file );
		if (!file.exists()){
			logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 270 .. (searchDir + filePath) Promo file Not exists.: " );
			return false;
		}
		logMessage("-DYN-PROMO- (XPEDXFileManager-checkFile) 280 .. (searchDir + filePath) Promo FILE EXISTS. " );
		return true;
	}

	/**
	/**
	 * This method copies the file specified by the given path in the given search directory.
	 * If the file exists and there is no checks for modification then it returns true. 
	 * If the file is modified then it copies the latest file into the search directory 
	 * and returns true.
	 * If the file does not exist it returns false.
	 * 
	 *   
	 * @param filePath is the requested filePath which could be relative to the context.
	 * @param searchDir is the directory where the file need to be located.
	 * @return true if the file exists; otherwise false.
	 * @throws IOException
	 */
	private static boolean copyFile(String filePath, String searchDir) throws IOException{		

		// if the source file does not exist return false.
		File srcFile = new File(filePath);
		if (!srcFile.exists()){
			return false;
		}

		// For windows if the file path starts with drive and :, then remove them before appending to the destination
		if (filePath.contains(":")){
			filePath = filePath.substring(filePath.indexOf(PATH_SEPARATOR), filePath.length());
		}

		// check for the destination file.
		if (!filePath.startsWith(PATH_SEPARATOR)){
			filePath = PATH_SEPARATOR + filePath;
		}
		String destFilePath = searchDir + filePath;
		destFilePath = destFilePath.replace("\\", "/");
		destFilePath = destFilePath.replace("//", "/");
		File destFile = new File(destFilePath);
		String strMsg = "";
		// if exists check if the source is more recent than the destination file
		if (destFile.exists()){
			if (srcFile.lastModified() > destFile.lastModified()){
				FileUtils.copyFile(srcFile, destFile, true);
				// may be throw an event to copy the directory
				return true;
			}
			 strMsg = "The source file " + filePath + "is not more recent than the destination file " 
			+ destFile + ". Not copying the source file.";
			
			LOG.debug(strMsg);
			logMessage(" -DYN-PROMO- (XPEDXFileManager-checkFile) .. " + strMsg);
			return true;
		}
		else {
			strMsg = "The destination file " + destFile + " does not exist. Copying the source file.";
			LOG.debug(strMsg);
			logMessage(" -DYN-PROMO- (XPEDXFileManager-checkFile) .. " + strMsg);
			FileUtils.copyFile(srcFile, destFile, true);
			return true;
		}		
	}
	
	/**
	 * 
	 * @param message
	 */
	private static void logMessage(String message) {
		if(DEBUG_TRACE == true ){
		if(LOG.isDebugEnabled()){
		LOG.debug( message );
		}
		}
	}
	
	/**
	 * Used for testing
	 */
    public static void main(String[] args){
		//String remotePath ="\\vijay\\MyDocs\\test\\htmls\\promotions\\";
		String remotePath ="D:\\xpedx\\htmls\\promotions";
		String destDir = "D:\\Sterling9.0\\Foundation\\builds\\buildswc";
		//String fileName ="\\\\vijay\\MyDocs\\test\\htmls\\promotions\\Catalog_Promo.html";
		//String fileName ="D:\\xpedx\\htmls\\promotions\\Catalog_Promo.html";
		try {
			copyDirectory(remotePath, destDir, false);
			if(LOG.isDebugEnabled()){
			LOG.debug("copied directory");
			}
			//checkFile(fileName, destDir,false);
			
		} catch (Exception e) {
			LOG.error("Exception");
			e.printStackTrace();
		}
	}
}
