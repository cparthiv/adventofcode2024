import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReaderUtil {

    /**
     * Reads all lines from the specified file path and returns them as a list of strings.
     *
     * @param filePath the path to the file
     * @return a list of lines read from the file
     * @throws IOException if an I/O error occurs reading from the file
     */
    public static List<String> readAllLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}
