// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.components;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Simple class that can write data to a file to be 'scp'-ed over to a computer afterwards. */
public class DataRecorder {
    private File file;
    private DataOutputStream outputStream;
    public String fileName;
    public boolean isOpen;
    private String dashboardPrefix;
    public DataRecorder(String fileName_) {
        fileName = fileName_;
        dashboardPrefix="[DataRecorder (file: '" + fileName + "')]";
        isOpen = false;
        try {
            file = new File(fileName);
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("YEAH.txt")));
            SmartDashboard.putString(dashboardPrefix + " Status", "Successfully opened/created file");
            SmartDashboard.putString(dashboardPrefix + " Command to copy over", "scp admin@10.10.73.2:" + file.getAbsolutePath() + " Downloads\\" + fileName);
            isOpen = true;
        } catch (IOException e) {
            SmartDashboard.putString(dashboardPrefix + " INIT IO ERROR", "ERROR: Unable to open file");
            System.out.println("DATA RECORDER ERROR:\n\t" + e.getStackTrace());
        }
    }

    /**
     * Writes a single string to the file.
     */
    public void write(String data) {
        try {
            outputStream.writeBytes(data + "\r\n");
        } catch (IOException e) {
            SmartDashboard.putString(dashboardPrefix + " WRITE IO ERROR", "ERROR: Unable to write to file");
            System.out.println("DATA RECORDER ERROR:\n\t" + e.getStackTrace());
        }
    }
    
    /**
     * Writes a list of strings separated by tabs.
     */
    public void writeArray(String... array) {
        String data = "";
        for (String str : array) {
            //Don't add trailing tab.
            if (data.length() != 0) {
                data += "\t";
            }
            data += str;
        }
        write(data);
    }

    /**
     * Writes a list of strings separated by tabs.
     */
    public void writeMap(Map<String,Object> map) {
        String data = "";
        for (String key : map.keySet()) {
            //Don't add trailing tab.
            if (data.length() != 0) {
                data += "\t";
            }
            data += key + ":" + map.get(key).toString();
        }
        write(data);
    }
}
