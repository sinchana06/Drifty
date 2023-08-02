package Backend;

import Enums.MessageCategory;
import Enums.MessageType;
import Utils.MessageBroker;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static Utils.DriftyConstants.*;

/**
 * This class checks if a directory exists or not. if it doesn't, the directory is created.
 */
class CheckDirectory {
    private static final MessageBroker message = Drifty.getMessageBrokerInstance();
    /**
     * This constructor creates the directory if it does not exist.
     * @param dir string of the folder where the user wants to download the file.
     * @throws IOException when creating the directory fails.
     */
    CheckDirectory(String dir) throws IOException {
        if (!(checkIfFolderExists(dir))) {
            Path directory = FileSystems.getDefault().getPath(dir);
            Files.createDirectory(directory);
            message.sendMessage(DIRECTORY_CREATED, MessageType.INFORMATION, MessageCategory.DIRECTORY);
        } else {
            message.sendMessage("Directory is valid !", MessageType.INFORMATION, MessageCategory.DIRECTORY);
        }
    }

    /**
     * This function checks if a folder exists or not.
     * @param folderName string of the folder where the user wants to download the file.
     * @return true if the folder exists and false if the folder is missing.
     */
    private static boolean checkIfFolderExists(String folderName) {
        boolean found = false;
        try {
            File file = new File(folderName);
            if (file.exists() && file.isDirectory()) {
                found = true;
            }
        } catch (Exception e) {
            message.sendMessage(ERROR_WHILE_CHECKING_FOR_DIRECTORY, MessageType.ERROR, MessageCategory.DIRECTORY);
        }
        return found;
    }
}
