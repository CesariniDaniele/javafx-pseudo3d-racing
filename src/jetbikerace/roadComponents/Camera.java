package jetbikerace.roadComponents;

public class Camera {
    private final double DIST_TO_ROAD = 1000;
    
    private double cameraX, cameraY, cameraZ;
    private double distToPlane;
    private double zMovement;
    private double currentSpeed;
    
    private Road road;
    
    public Camera(Road road){
        cameraX = 0;
        cameraY = 3000;
        cameraZ = - DIST_TO_ROAD;
        
        distToPlane = 1/(cameraY/DIST_TO_ROAD);
        
        this.road = road;
    }
    
    public void update(double elapsedTime){
        double roadLength = road.getSectionLength();
        
        zMovement += currentSpeed*elapsedTime;
        if(zMovement >= roadLength){
            zMovement -= roadLength;
        }
        cameraZ = zMovement - DIST_TO_ROAD; //per aggiornare la posizione ad ogni frame
        if(cameraZ<0){
            cameraZ += roadLength;
        }
    }
    
    public void restart(){
        zMovement = 0;
        currentSpeed = 0;
        cameraZ = - DIST_TO_ROAD;
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public double getCameraZ() {
        return cameraZ;
    }

    public double getDistToPlane() {
        return distToPlane;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getDIST_TO_ROAD() {
        return DIST_TO_ROAD;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setCameraZ(double cameraZ) {
        this.cameraZ = cameraZ;
    }
}
