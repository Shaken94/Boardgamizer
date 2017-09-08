package skyzofresnes.boardgamizer;

/**
 * Created by LEROYJ on 19/07/2017.
 */

public class CharacterModel {
    private String name = null;
    private String gender = null;
    private String origin = null;
    private String type = null;
    private String image = null;
    private boolean selected = false;

    public CharacterModel() {
    }

    public CharacterModel(String name, String gender, String origin, String type, String image) {
        this.name = name;
        this.gender = gender;
        this.origin = origin;
        this.type = type;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name + " "
                + gender + " "
                + origin + " " + type;
    }
}
