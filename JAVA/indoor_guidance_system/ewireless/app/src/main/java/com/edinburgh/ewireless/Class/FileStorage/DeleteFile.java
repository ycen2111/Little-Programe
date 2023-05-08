package com.edinburgh.ewireless.Class.FileStorage;

import java.io.File;

/**
 A class for deleting a file by providing either a File object or the file address as a string.
 */
public class DeleteFile {
    /**
     Deletes a file by using a File object.
     @param file the File object to be deleted
     */
    public DeleteFile(File file){
        file.delete();
    }

    /**
     Deletes a file by providing the file address as a string.
     @param fileAdd the file address as a string
     */
    public DeleteFile(String fileAdd){
        File file = new File(fileAdd);
        file.delete();
    }
}
