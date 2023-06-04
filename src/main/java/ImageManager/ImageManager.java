package ImageManager;

import com.example.GraphicalUserInterface.Main;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.scene.image.Image;

public class ImageManager {
    private Image logoImage, insertIconImage, deleteIconImage, updateIconImage, refreshIconImage;
    public ImageManager() {
        this.logoImage = new Image("https://docs.google.com/uc?id=1F2pXOLfvuynr9JcURTR5Syg7N1YdPJXK");
        this.insertIconImage = new Image("file:" + ManagementMain.class.getResource("assets/images/add-icon.png").getPath().substring(1));
        this.updateIconImage = new Image("file:" + ManagementMain.class.getResource("assets/images/edit-square-icon.png").getPath().substring(1));
        this.deleteIconImage = new Image("file:" + ManagementMain.class.getResource("assets/images/delete-icon.png").getPath().substring(1));
        this.refreshIconImage = new Image("file:" + ManagementMain.class.getResource("assets/images/redo-circle-icon.png").getPath().substring(1));
    }
    public Image getLogoImage() {
        return this.logoImage;
    }
    public Image getInsertIconImage() {
        return this.insertIconImage;
    }

    public Image getDeleteIconImage() {
        return deleteIconImage;
    }
    public Image getUpdateIconImage() {
        return updateIconImage;
    }
    public Image getRefreshIconImage() {
        return refreshIconImage;
    }
}
