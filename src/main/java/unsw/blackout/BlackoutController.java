package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

/**
 * BlackoutController
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class BlackoutController {

    private List<Device> devices = new ArrayList<Device>();
    private List<Satellite> satellites = new ArrayList<Satellite>();

    public void createDevice(String deviceId, String type, Angle position) {

    }

    public void removeDevice(String deviceId) {
        devices.removeIf(element -> element.getId().equals(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {

    }

    public void removeSatellite(String satelliteId) {
        satellites.removeIf(element -> element.getId().equals(satelliteId));
    }

}
