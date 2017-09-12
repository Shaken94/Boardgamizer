package skyzofresnes.boardgamizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LEROYJ on 08/09/2017.
 */

public class FiltersModel {
    private HashMap<String, List<String>> filters = new HashMap<>();
    private List<String> gender = new ArrayList<>();
    private List<String> origin = new ArrayList<>();
    private List<String> type = new ArrayList<>();
    private String strGender;
    private String strOrigin;
    private String strType;

    public FiltersModel(List<String> gender, String strGender, List<String> origin, String strOrigin, List<String> type, String strType) {
        this.gender = gender;
        this.strGender = strGender;
        this.origin = origin;
        this.strOrigin = strOrigin;
        this.type = type;
        this.strType = strType;

        filters.put(this.strType, this.type);
        filters.put(this.strOrigin, this.origin);
        filters.put(this.strGender, this.gender);
    }

    public FiltersModel() {
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    public String getStrOrigin() {
        return strOrigin;
    }

    public void setStrOrigin(String strOrigin) {
        this.strOrigin = strOrigin;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
        filters.put(strGender, this.gender);
    }

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        this.origin = origin;
        filters.put(strOrigin, this.origin);
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
        filters.put(strType, this.type);
    }

    public HashMap<String, List<String>> getFilters() {
        filters.put(strType, this.type);
        filters.put(strOrigin, this.origin);
        filters.put(strGender, this.gender);
        return filters;
    }
}
