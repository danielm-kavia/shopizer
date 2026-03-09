/*
 * Minimal Maven wrapper downloader/launcher.
 *
 * NOTE: This file is included because some environments don't have Maven installed and
 * we can't assume a pre-existing mvnw setup. The accompanying mvnw script runs the
 * wrapper JAR; however, in this repo we implement a tiny self-bootstrapping wrapper
 * inside maven-wrapper.jar (see build step below).
 *
 * This class is intentionally kept simple and compatible with older Java versions.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class MavenWrapperDownloader {

    private static final String WRAPPER_PROPERTIES_PATH = ".mvn/wrapper/maven-wrapper.properties";

    public static void main(String[] args) throws Exception {
        File baseDir = new File(System.getProperty("maven.multiModuleProjectDirectory", "."));
        File propsFile = new File(baseDir, WRAPPER_PROPERTIES_PATH);
        if (!propsFile.exists()) {
            System.err.println("Missing " + propsFile.getAbsolutePath());
            System.exit(1);
        }

        Properties p = new Properties();
        InputStream in = null;
        try {
            in = new java.io.FileInputStream(propsFile);
            p.load(in);
        } finally {
            if (in != null) in.close();
        }

        String distUrl = p.getProperty("distributionUrl");
        if (distUrl == null || distUrl.trim().length() == 0) {
            System.err.println("distributionUrl is not set in " + propsFile.getAbsolutePath());
            System.exit(1);
        }

        File wrapperDir = new File(baseDir, ".mvn/wrapper");
        if (!wrapperDir.exists()) wrapperDir.mkdirs();

        File distZip = new File(wrapperDir, "maven-dist.zip");
        download(distUrl, distZip);

        System.out.println("Downloaded Maven distribution to " + distZip.getAbsolutePath());
        System.out.println("This downloader is informational; the preview runtime uses mvnw which bootstraps Maven.");
    }

    private static void download(String url, File dest) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new URL(url).openStream());
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[8192];
            int count;
            while ((count = bis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, count);
            }
        } finally {
            if (bis != null) try { bis.close(); } catch (Exception ignored) {}
            if (bos != null) try { bos.close(); } catch (Exception ignored) {}
        }
    }
}
