package me.bloodarowman.bardlike;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.CRC32;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 8/11/13
 * Time: 5:39 AM
 */
public class Bootstrap {
    public static void main(String[] args) {
        try {
            URL url = Misc.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println(url.getFile());
            if (url.getFile().matches(".*\\.jar")) {
                JarFile jar = new JarFile(new File(url.getFile()));
                if (jar == null) { return; }
                for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
                    JarEntry entry = enums.nextElement();
                    if (!entry.getName().matches("scripts/.*") && !entry.getName().matches("lib/.*")) { continue; }

                    String fileName = "." + File.separator + entry.getName();
                    File f = new File(fileName);
                    if (f.exists()) { continue; }

                    if (fileName.endsWith("/")) {
                        f.mkdirs();
                    } else {
                        BufferedInputStream is = new BufferedInputStream(jar.getInputStream(entry));
                      /*  if (f.exists() && f.isFile()) {
                            System.out.println("Checking if f: " + f.getPath() + " is different.");
                            BufferedInputStream bis = new BufferedInputStream(is);
                            CRC32 crc = new CRC32();
                            while (is.available() > 0) { crc.update(bis.read()); }
                            long jarCrc = crc.getValue();
                            crc.reset();
                            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(f.getPath()));
                            while (inputStream.available() > 0) { crc.update(inputStream.read()); }
                            long newCrc = crc.getValue();
                            System.out.println("f: " + f.getPath() + " " + newCrc + " " + jarCrc);
                            if (jarCrc == newCrc) { continue; }
                        } */
                        FileOutputStream fos = new FileOutputStream(f);
                        while(is.available() > 0) {
                            fos.write(is.read());
                        }
                        fos.close();
                        is.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* System.setProperty("java.library.path", "lib/native/" + System.getProperty("os.name").toLowerCase());
        System.out.println(System.getProperty("java.library.path"));
        Field fieldSysPath = null;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        fieldSysPath.setAccessible( true );
        try {
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } */

        try {
            Main.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
