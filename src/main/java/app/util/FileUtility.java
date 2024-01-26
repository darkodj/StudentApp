package app.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtility{
    public static void createFile() throws IOException {
        File f = new File("object");
        if (!f.exists()) {
            boolean t = f.createNewFile();
        }
    }

    public static void delete() {
        File file = getFile();
        boolean t = file.delete();
    }

    public static void doe() {
        File file = getFile();
        file.deleteOnExit();
    }

    private static File getFile() {
        Path path = Path.of("object");
        return new File(path.toUri());
    }


}
