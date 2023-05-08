package com.edinburgh.ewireless.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import com.edinburgh.ewireless.Class.FileStorage.DeleteFile;
import com.edinburgh.ewireless.Class.FileStorage.FileManager;
import com.edinburgh.ewireless.Class.FileStorage.OpenFile;
import com.edinburgh.ewireless.Class.Trajectory.Trajectory;
import com.edinburgh.ewireless.Class.Trajectory.TrajectoryManager;
import com.edinburgh.ewireless.Class.Trajectory.TrajectoryOuterClass;
import com.edinburgh.ewireless.Class.Trajectory.timeStampToStringTime;
import com.edinburgh.ewireless.method.System.ToastShow;
import com.google.gson.JsonArray;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.File;

/**

 This class provides methods to display alert dialogs for the user to save a recording as a file
 in the application. It contains methods for entering a file name, displaying warnings, and saving
 the recording without wifi. The class uses objects from the Trajectory package and FileManager class
 to manage and save trajectory recordings. The class also uses methods from the timeStampToStringTime
 and ToastShow classes to display messages to the user.
 */
public class UserEnterEvent {
    UserEnterEvent userEnterEvent = this;

    /**
     * Displays an alert dialog to the user to enter a file name, and checks for wifi state
     * before calling the saveRecording function of TrajectoryManager.
     *
     * @param context           The context of the calling activity
     * @param trajectoryManager The TrajectoryManager object used to save the trajectory recording
     * @param trajectory        The Trajectory object containing the recording data
     * @param stopTime          The stop time of the recording
     */
    public void enterFileNameEvent(Context context, TrajectoryManager trajectoryManager, Trajectory trajectory, long stopTime) {
        // create an alert dialog with an edit text
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TrajectoryOuterClass.Trajectory.Builder trajectoryBuilder = trajectory.getTrajectoryBuilder();
        builder.setTitle("Create time: "+ timeStampToStringTime.yyyymmddhhmmss(stopTime)+
                "\nRecord length: "+ timeStampToStringTime.mmssmmm(stopTime-trajectoryBuilder.getStartTimestamp()));

        final EditText input = new EditText(context);
        input.setHint("Please enter file's name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // add buttons to the alert dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {
                    trajectoryManager.checkWifiState(context, input.getText().toString(), userEnterEvent);
                }
                else {
                    noFileNameWarningMessage(context, trajectoryManager, trajectory, stopTime);
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelWarningMessageWithCheck(context, trajectoryManager, trajectory,stopTime);
                dialog.cancel();
            }
        });

        // display the alert dialog
        builder.show();
    }

    /**
     * Displays an alert dialog to the user to ask if they want to save the recording without wifi,
     * and calls the saveRecording function of TrajectoryManager if the user selects "YES".
     *
     * @param context           The context of the calling activity
     * @param name              The name of the file to save the recording as
     * @param trajectoryManager The TrajectoryManager object used to save the recording
     */
    public void noWifiWarningCheckMessage(Context context, String name, TrajectoryManager trajectoryManager){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning:");
        builder.setMessage("No wifi detected" +
                "\nDo you want save it and upload it later?");
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastShow toastShow = new ToastShow(context);
                toastShow.toastShow("file saving cancelled");
                dialog.cancel();
            }
        });
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                trajectoryManager.saveRecording(context, name, false);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays an alert dialog to the user to inform them that they need to enter a file name.
     *
     * @param context           The context of the calling activity
     * @param trajectoryManager The TrajectoryManager object used to save the recording
     * @param trajectory        The Trajectory object containing the recording data
     * @param stopTime          The stop time of the recording
     */
    public void noFileNameWarningMessage(Context context, TrajectoryManager trajectoryManager, Trajectory trajectory, long stopTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning:");
        builder.setMessage("Please enter file name");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterFileNameEvent(context,trajectoryManager,trajectory,stopTime);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a warning message with an OK and Cancel button.
     * If OK is clicked, the file saving process is cancelled and a toast message is displayed.
     * If Cancel is clicked, the user is prompted to enter a filename for the trajectory to be saved.
     *
     * @param context          the Context object for the current Android application
     * @param trajectoryManager the TrajectoryManager object for managing trajectory data
     * @param trajectory       the Trajectory object representing the trajectory being saved
     * @param stopTime         the time when the trajectory saving process was stopped
     */
    public void cancelWarningMessageWithCheck(Context context, TrajectoryManager trajectoryManager, Trajectory trajectory, long stopTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning:");
        builder.setMessage("You will lost your trajectory" +
                "\nPress OK if you want remove this trajectory");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastShow toastShow = new ToastShow(context);
                toastShow.toastShow("file saving cancelled");
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterFileNameEvent(context,trajectoryManager,trajectory,stopTime);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a list of items to select from.
     * When an item is selected, the fileEditTypeSelect() method is called to prompt the user to delete or load the selected file.
     *
     * @param context      the Context object for the current Android application
     * @param items        an array of strings representing the list of items to display
     * @param fileManager  the FileManager object for managing file data
     * @param openFile     the OpenFile object for opening and reading files
     */
    public void listSelect(Context context, String[] items, FileManager fileManager, OpenFile openFile){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an File");

        // set the list of items
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                fileEditTypeSelect(context,items,fileManager,openFile,which);
            }
        });

        builder.show();
    }

    /**
     * Displays a warning message with Load and Delete buttons.
     * If Load is clicked, the selected file is loaded using the OpenFile object.
     * If Delete is clicked, the user is prompted to confirm the deletion with another warning message.
     *
     * @param context     the Context object for the current Android application
     * @param items       an array of strings representing the list of items to display
     * @param fileManager the FileManager object for managing file data
     * @param openFile    the OpenFile object for opening and reading files
     * @param num         the number of the selected file in the list of items
     */
    public void fileEditTypeSelect(Context context, String[] items, FileManager fileManager, OpenFile openFile, int num){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit type");
        builder.setMessage("Do you want delete or load this file?");
        builder.setPositiveButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    openFile.setFile(fileManager.getTargetFileByNumber(num));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWarningMessageWithCheck(context,items,fileManager,openFile,num);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a warning message with Cancel and Delete buttons.
     * If Cancel is clicked, the user is taken back to the fileEditTypeSelect() method.
     * If Delete is clicked, the selected file is deleted using the DeleteFile object and a toast message is displayed.
     *
     * @param context     the Context object for the current Android application
     * @param items       an array of strings representing the list of items to display
     * @param fileManager the FileManager object for managing file data
     * @param openFile    the OpenFile object for opening and reading files
     * @param num         the number of the selected file in the list of items
     */
    public void deleteWarningMessageWithCheck(Context context, String[] items, FileManager fileManager, OpenFile openFile, int num){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning:");
        builder.setMessage("Your are in delete process for file:"+
                "\n"+fileManager.getTargetFileByNumber(num).getAbsolutePath()+
                "\nPress OK if you want delete this file"+
                "\nFOREVER~~~");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileEditTypeSelect(context,items,fileManager,openFile,num);
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Nonsense, delete it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeleteFile(fileManager.getTargetFileByNumber(num));
                ToastShow toastShow = new ToastShow(context);
                toastShow.toastShow("file "+fileManager.getTargetFileByNumber(num).getAbsolutePath()+ "deleted");
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a warning message with Cancel and DELETE THEM buttons.
     * If Cancel is clicked, the user is taken back to the fileEditTypeSelect() method.
     * If DELETE THEM is clicked, all files in the fileManager are deleted using the DeleteFile object and a toast message is displayed.
     *
     * @param context     the Context object for the current Android application
     * @param items       an array of strings representing the list of items to display
     * @param fileManager the FileManager object for managing file data
     * @param openFile    the OpenFile object for opening and reading files
     * @param num         the number of the selected file in the list of items
     */
    public void deleteAllWarningMessageWithCheck(Context context, String[] items, FileManager fileManager, OpenFile openFile, int num){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning, Warning!!:");
        builder.setMessage("Your are in process of delete ALL files:"+
                "\nPress OK if you want delete ALL files"+
                "\nAre you sure?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileEditTypeSelect(context,items,fileManager,openFile,num);
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Nonsense, DELETE THEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (File file : fileManager.getFiles())
                    new DeleteFile(file);
                ToastShow toastShow = new ToastShow(context);
                toastShow.toastShow("All files deleted");
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     *Displays an alert dialog containing a list of cloud-saved trajectories.
     *@param context The context of the current state of the application.
     *@param items The list of items to display in the alert dialog.
     *@param jsonArray The JSON array containing the cloud-saved trajectories.
     */
    public void cloudTrajectorySelect(Context context, String[] items, JsonArray jsonArray){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("All saved trajectory:");

        // set the list of items
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cloudTrajectorySelect(context, items, jsonArray);
            }
        });

        builder.show();
    }
}
