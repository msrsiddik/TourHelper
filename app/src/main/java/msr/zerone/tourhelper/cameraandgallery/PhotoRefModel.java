package msr.zerone.tourhelper.cameraandgallery;

public class PhotoRefModel {
    private String photoName;

    public PhotoRefModel() {
    }

    public PhotoRefModel(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
