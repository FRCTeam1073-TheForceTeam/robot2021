package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.ArrayList;

/**
 * Contains collections of paths, markers, obstacles and
 * important locations that commands should know about.
 */
public class Map extends SubsystemBase {



    /**
     * There is an instance of this class for each important map location (markers, goal, etc.)
     */
    public static class MapLocation {
      
      public MapLocation() {

      }
    }




    private final ArrayList<MapLocation> m_markerLocations = new ArrayList<MapLocation>();
    private final ArrayList<MapLocation> m_goalLocations = new ArrayList<MapLocation>();

    public Map() {

    }


    // Tell the map to load the mapping data.
    public boolean loadData() {
      // TODO: Read map data in from a file.
      return false;
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
    
}
