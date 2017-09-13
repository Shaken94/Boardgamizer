package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class SaveListAdapter extends BaseAdapter {
    Activity activity;
    List<CharacterModel> filterCharacters;
    List<CharacterModel> originalCharacters;
    LayoutInflater inflater;

    public SaveListAdapter(Activity activity, List<CharacterModel> characters) {
        this.activity = activity;
        this.filterCharacters = new ArrayList<>();
        this.filterCharacters.addAll(characters);
        this.originalCharacters = new ArrayList<>();
        this.originalCharacters.addAll(characters);
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return filterCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null){
            view = inflater.inflate(R.layout.savelist_listview_item, viewGroup, false);

            holder = new ViewHolder();

            holder.checked = (ImageView) view.findViewById(R.id.check_box);
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.gender = (TextView) view.findViewById(R.id.gender);
            holder.origin = (TextView) view.findViewById(R.id.origin);
            holder.type = (TextView) view.findViewById(R.id.type);
            holder.imgCharacter = (ImageView) view.findViewById(R.id.imgCharacter);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        CharacterModel characterModel = filterCharacters.get(position);

        if (characterModel.isSelected()) {
            holder.checked.setBackgroundResource(R.drawable.checked);
        }
        else {
            holder.checked.setBackgroundResource(R.drawable.check);
        }

        Resources resources = activity.getResources();
        String packageName = activity.getPackageName();

        final String name = characterModel.getName();
        if (name.startsWith("@")) {
            holder.name.setText(resources.getIdentifier(Constantes.VALUE_STRING + name.substring(1), Constantes.VALUE, packageName));
        }else{
            holder.name.setText(name);
        }

        final String gender = characterModel.getGender();
        if (gender.startsWith("@")){
            holder.gender.setText(resources.getIdentifier(Constantes.VALUE_STRING + gender.substring(1), Constantes.VALUE, packageName));
        }else {
            holder.gender.setText(gender);
        }

        final String origin = characterModel.getOrigin();
        if (origin.startsWith("@")){
            holder.origin.setText(resources.getIdentifier(Constantes.VALUE_STRING + origin.substring(1), Constantes.VALUE, packageName));
        }else {
            holder.origin.setText(origin);
        }

        final String type = characterModel.getType();
        if (type.startsWith("@")){
            holder.type.setText(resources.getIdentifier(Constantes.VALUE_STRING + type.substring(1), Constantes.VALUE, packageName));
        }else {
            holder.type.setText(type);
        }

        int imgCharacterId = resources.getIdentifier(characterModel.getImage() + Constantes.PORTRAIT, Constantes.DRAWABLE, packageName);
        holder.imgCharacter.setBackgroundResource(imgCharacterId);

        return view;
    }

    public void updateRecords(List<CharacterModel> characters){
        this.filterCharacters = characters;

        notifyDataSetChanged();
    }

    // Filter Class
    public void filter(String name, HashMap<String, List<String>> kMap) {
        List<CharacterModel> listAfterName = new ArrayList<>();
        if (!TextUtils.isEmpty(name)){
            for (CharacterModel characterModel : originalCharacters) {
                if (characterModel.getName().toLowerCase().contains(name)) {
                    listAfterName.add(characterModel);
                }
            }
        }
        else{
            listAfterName.addAll(originalCharacters);
        }

        List<CharacterModel> listIn = new ArrayList<>(listAfterName);
        for (String str : kMap.keySet()){
            List<CharacterModel> listOut = new ArrayList<>();
                for (String constraint : kMap.get(str)) {
                    addFilterConstraint(listIn, listOut, str, constraint);
                }
            listIn = listOut;
        }
        updateRecords(listIn);
    }

    private void addFilterConstraint(List<CharacterModel> listIn, List<CharacterModel> listOut, String str, String constraint) {
        for(CharacterModel characterModel : listIn){
            if (str.equals(activity.getString(R.string.radioGroupGender)) && characterModel.getGender().equals(constraint)) {
                listOut.add(characterModel);
            }
            if (str.equals(activity.getString(R.string.radioGroupOrigin)) && characterModel.getOrigin().equals(constraint)) {
                listOut.add(characterModel);
            }
            if (str.equals(activity.getString(R.string.radioGroupType)) && characterModel.getType().equals(constraint)) {
                listOut.add(characterModel);
            }
        }
    }

    class ViewHolder {
        ImageView checked;
        TextView name;
        TextView gender;
        TextView origin;
        TextView type;
        ImageView imgCharacter;
    }
}