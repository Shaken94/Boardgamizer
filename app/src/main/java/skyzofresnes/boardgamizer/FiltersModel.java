package skyzofresnes.boardgamizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEROYJ on 08/09/2017.
 */

public class FiltersModel {
    List<String> gender = new ArrayList<>();
    List<String> origin = new ArrayList<>();
    List<String> type = new ArrayList<>();

    public FiltersModel(List<String> gender, List<String> origin, List<String> type) {
        this.gender = gender;
        this.origin = origin;
        this.type = type;
    }

    public FiltersModel() {
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        this.origin = origin;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }
}
