package Backend;

import Enums.DriftyConfig;
import Enums.OS;

import Utils.MessageBroker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class CopyYtDlp {
    /**
     * This is the message broker service instance that sends messages to the CLI or GUI.
     */
    static MessageBroker messageBroker = Drifty.getMessageBrokerInstance();

    /**
     * This method copies the yt-dlp (the program used for downloading YouTube videos) to the temporary folder of the Operating System
     * and sets it as executable.
     *
     * @throws IOException when the file has not been successfully copied.
     */
    public boolean copyToTemp() throws IOException {
        Path filePath = Paths.get(DriftyConfig.getConfig(DriftyConfig.YT_DLP_COMMAND));
        if (!filePath.toFile().exists()) {
            try (InputStream inputStream = CopyYtDlp.class.getClassLoader().getResourceAsStream(DriftyConfig.getConfig(DriftyConfig.NAME));
                 OutputStream outputStream = Files.newOutputStream(filePath)) {
                System.out.println("Copying file to: " + filePath);
                if (inputStream != null) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                else {
                    System.out.println("Resource not found: " + DriftyConfig.getConfig(DriftyConfig.NAME));
                }
                if (!OS.isWindows()) {
                    if (Files.exists(filePath)) {
                        Set<PosixFilePermission> permissions = new HashSet<>();
                        permissions.add(PosixFilePermission.OWNER_READ);
                        permissions.add(PosixFilePermission.OWNER_WRITE);
                        permissions.add(PosixFilePermission.OWNER_EXECUTE);
                        permissions.add(PosixFilePermission.GROUP_READ);
                        permissions.add(PosixFilePermission.GROUP_WRITE);
                        permissions.add(PosixFilePermission.GROUP_EXECUTE);
                        permissions.add(PosixFilePermission.OTHERS_READ);
                        permissions.add(PosixFilePermission.OTHERS_WRITE);
                        permissions.add(PosixFilePermission.OTHERS_EXECUTE);
                        Files.setPosixFilePermissions(filePath,permissions);
                    }
                }
            }
        }
        return !filePath.toFile().exists();
    }


}
