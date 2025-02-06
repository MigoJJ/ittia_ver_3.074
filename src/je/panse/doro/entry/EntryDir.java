package je.panse.doro.entry;

import java.io.File;		
import java.util.Arrays;

public class EntryDir {
    public static final String currentDir = System.getProperty("user.dir");
    public static final String homeDir;
    public static final String backupDir;

    static {
        String env = System.getProperty("app.env", "dev");
        String[] pathParts = {"je", "panse", "doro"};
//        String basePath = "prod".equals(env) ? currentDir : currentDir + File.separator + "src";
        String basePath = "prod".equals(env) ? currentDir : currentDir + File.separator;
        homeDir = buildPath(basePath, pathParts);
        backupDir = buildPath(homeDir, "tripikata", "rescue");
    }

    public static void main(String[] args) {
        Arrays.asList(homeDir, backupDir).forEach(EntryDir::createDirectoryIfNotExists);
        System.out.println("Current user's directory: " + currentDir);
        System.out.println("Home Directory: " + homeDir);
        System.out.println("Backup Directory: " + backupDir);
    }

    private static String buildPath(String base, String... parts) {
        StringBuilder path = new StringBuilder(base);
        for (String part : parts) {
            path.append(File.separator).append(part);
        }
        return path.toString();
    }

    private static void createDirectoryIfNotExists(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            boolean wasSuccessful = dir.mkdirs();
            System.out.println((wasSuccessful ? "Successfully created" : "Failed to create") + 
                               " directory: " + directoryPath);
        } else {
            System.out.println("Directory already exists: " + directoryPath);
        }
    }
}