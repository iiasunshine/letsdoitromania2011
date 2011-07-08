/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import com.sun.jersey.core.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.SingleFile;

/**
 *
 * @author dan.grigore
 */
public class AppUtils {

    /**
     * 
     * @param loginMail
     * @param loginPassword
     * @return
     */
    public static String generateCredentials(String loginMail, String loginPassword) {
        String credentials = "Basic " + new String(Base64.encode(loginMail + ":" + loginPassword), Charset.forName("ASCII"));
        return credentials;
    }

    public static int parseToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static File saveToTempFile(UploadedFile uploadedFile, String fileName) throws Exception {
        SingleFile clientFileSF = new SingleFile(uploadedFile.getName());

        StringBuilder tempFilePath = new StringBuilder();
        tempFilePath.append(JsfUtils.getServletContextAttr("javax.servlet.context.tempdir"));
        tempFilePath.append(File.pathSeparator);
        tempFilePath.append("temp");
        tempFilePath.append(File.pathSeparator);
        tempFilePath.append(fileName).append(".").append(clientFileSF.getFileExtension());
        File targetFile = new File(tempFilePath.toString());

        /* virificare existenta director upload */
        File targetFolder = targetFile.getParentFile();
        if (!targetFolder.exists() || !targetFolder.isDirectory()) {
            if (!targetFolder.mkdirs()) {
                throw new Exception("Nu a putut fi creat directorul destinatie: " + targetFolder.getAbsolutePath());
            }
        }

        /* scriere pe disc */
        writeStreamToFile(uploadedFile.getInputStream(), targetFile);
        /*ImageInfo originalImageInfo = ImageInfo.getImageInfo(uploadFileSF.toString(), false);
        SingleFile tempFileSF = new SingleFile(uploadFileSF.getFilePath(),
        uploadFileSF.getFileName() + "_temp",
        uploadFileSF.getFileExtension());
        SingleFile resizeResultSF = MyAppUtils.generateResizedImg(uploadFileSF,
        tempFileSF,
        MyAppUtils.generateImgWidth(originalImageInfo.getWidth(), originalImageInfo.getHeight(), 100),
        100,
        true);
        File tempFile = new File(tempFileSF.toString());
        uploadFile.delete();
        tempFile.renameTo(uploadFile);

        selectedPageItem.setImage(relativeStoreFolderPath + uploadFileSF.getFileName() + "." + uploadFileSF.getFileExtension());
        ImageInfo imageInfo = ImageInfo.getImageInfo(uploadFileSF.toString(), false);
        selectedPageItem.setWidth(imageInfo.getWidth());
        selectedPageItem.setHeight(imageInfo.getHeight());*/

        return targetFile;
    }

    public static String printStackTrace(Exception ex) {
        StringBuilder err = new StringBuilder(ex.toString());

        StackTraceElement[] traceElements = ex.getStackTrace();
        for (int i = 0; i < traceElements.length; i++) {
            err.append("\n").append(traceElements[i].toString());
        }

        return err.toString();
    }

    public static void writeStreamToFile(InputStream in, File file) throws Exception {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            long size = 2048;
            int read = 0;
            byte[] bytes = new byte[(int) size];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } finally {
            in.close();
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
