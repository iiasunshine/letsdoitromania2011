package ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleFile {

    org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(this.getClass());
    private char fileSeparator;
    private String filePath;
    private String fileName;
    private String fileExtension;

    //------------------------------------------------------------------------------------------------------------------
    public SingleFile(String filePath, String fileName, String fileExtension) {
        this(filePath, fileName, fileExtension, System.getProperty("file.separator").charAt(0));
    }

    //------------------------------------------------------------------------------------------------------------------
    public SingleFile(String filePath, String fileName, String fileExtension, char fileSeparator) {
        this.fileSeparator = fileSeparator;
        setFilePath(filePath);
        setFileName(fileName);
        setFileExtension(fileExtension);
    }

    public SingleFile(String fileFullPath) {
        this(fileFullPath, System.getProperty("file.separator").charAt(0));
    }

    //------------------------------------------------------------------------------------------------------------------
    public SingleFile(String fileFullPath, char fileSeparator) {
        this.fileSeparator = fileSeparator;
        fileFullPath = verifyPathSyntax(fileFullPath, false, fileSeparator);
        int index1 = 0;
        if ((index1 = fileFullPath.lastIndexOf("" + fileSeparator)) >= 0) {
            setFilePath(fileFullPath.substring(0, index1));
            int index2 = 0;
            if ((index2 = fileFullPath.lastIndexOf(".")) >= 0 && index2 > index1) {
                //log4j.info("--- fileFullPath="+fileFullPath);
                //log4j.info("--- index1="+index1);
                //log4j.info("--- index2="+index2);
                setFileName(fileFullPath.substring(index1 + 1, index2));
                setFileExtension(fileFullPath.substring(index2 + 1));
            } else {
                setFileName(fileFullPath.substring(index1 + 1));
                setFileExtension("");
            }
        } else {
            setFilePath("");
            int index2 = 0;
            if ((index2 = fileFullPath.lastIndexOf(".")) >= 0) {
                setFileName(fileFullPath.substring(0, index2));
                setFileExtension(fileFullPath.substring(index2 + 1));
            } else {
                setFileName(fileFullPath);
                setFileExtension("");
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public String toString() {
        return filePath + fileName + "." + fileExtension;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getFilePath() {
        return filePath;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setFilePath(String filePath) {
        this.filePath = verifyPathSyntax(filePath, true, getFileSeparator());
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getFileName() {
        return fileName;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getFileExtension() {
        return fileExtension;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * @return the fileSeparator
     */
    public char getFileSeparator() {
        return fileSeparator;
    }

    /**
     * set a new file separator and replace al the '/' or '\' characters from the filePath
     * @param fileSeparator the fileSeparator to set
     */
    public void setFileSeparator(char fileSeparator) {
        this.fileSeparator = fileSeparator;
        if (fileSeparator != '/') {
            this.filePath = this.filePath.replace('/', fileSeparator);
        } else if (fileSeparator != '\\') {
            this.filePath = this.filePath.replace('\\', fileSeparator);
        }
    }

    public static String verifyPathSyntax(String path, boolean isFolder, char fileSeparator) {
        if (path != null) {
            if (path.length() > 0) {
                path.replace('\\', '/');
                Pattern p = Pattern.compile("(\\/\\/+)");
                Matcher m = p.matcher(path);
                boolean result = m.find();
                path = m.replaceAll("/");

                if (fileSeparator != '/') {
                    path = path.replace('/', fileSeparator);
                } else if (fileSeparator != '\\') {
                    path = path.replace('\\', fileSeparator);
                }

                if (isFolder && !path.endsWith("" + fileSeparator)) {
                    path += fileSeparator;
                }

                if (!isFolder && path.endsWith("" + fileSeparator)) {
                    path = path.substring(0, path.length() - 1);
                }

                /*if (path.startsWith("" + fileSeparator)) {
                path = path.substring(1);
                }*/
            }
        } else {
            path = "";
        }

        return path;
    }

    /**
     * Verify if the given path syntax is corect. It must match the following rules:<br>
     * - it uses the system file separator (the method replace al '/' or '\')<br>
     * - a path don't start with the file separator character<br>
     * - if is a folder path (<b>isFolder</b> is 'true') it ends with the file separator character<br>
     *
     * @param path The given path for syntax validation
     * @param isFolder Specifies if the given path is a folder path or a file path
     * @return the rectifyed path
     */
    public static String verifyPathSyntax(String path, boolean isFolder) {
        char fileSeparator = System.getProperty("file.separator").charAt(0);
        return verifyPathSyntax(path, isFolder, fileSeparator);
    }
}
