package cc.idiary.nuclear.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Function;

/**
 * Created by Neo on 2017/6/1.
 */
public class UploadTools {
    private static final Logger logger = LoggerFactory.getLogger(UploadTools.class);
    private static String taxPath = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(UploadTools.class.getResourceAsStream("/properties/upload.properties"));
        } catch (IOException e) {
            logger.error("", e);
        }
        taxPath = properties.getProperty("upload.basedir");
    }

    /**
     *
     * @param is
     * @param type
     * @param fileName
     * @param getPath return the abs path
     * @return the file path if success
     * @throws IOException
     */
    public static File save(InputStream is, String type, String fileName, Function<String, String> getPath) throws IOException{
        String pathStr = getPath(type);
        if (getPath != null){
            pathStr = getPath.apply(pathStr);
        }
        File path = new File(pathStr);
        if (!path.exists()){
            if (!path.mkdirs()){
                logger.error("创建路径失败");
                throw new IOException("创建路径失败");
            }
        }
        File finalFile = new File(pathStr + File.separator + fileName);
        if (finalFile.exists()){
            logger.error("文件已经存在");
            throw new IOException("文件已经存在");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(finalFile);
            int i = 0;
            byte[] bs = new byte[1024];
            while ((i = is.read(bs)) != -1) {
                fos.write(bs, 0, i);
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new IOException(e.getMessage());
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }

            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }

        }
        return finalFile;
    }

    public static boolean del(String fileName) throws IOException {
        File finalFile = new File(fileName);
        return !finalFile.exists() || finalFile.delete();
    }



    private static String getPath (String type){
        if (UPLOAD_TYPE.TAX.equals(type)){
            return taxPath;
        } else {
            return "";
        }
    }



    public static class UPLOAD_TYPE {
        public static final String TAX = "TAX";
    }
}
