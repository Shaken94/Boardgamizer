package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
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
    //private CharacterModelFilter filter;

    public SaveListAdapter(Activity activity) {
        this.activity = activity;
    }

    public SaveListAdapter(Activity activity, List<CharacterModel> characters) {
        this.activity   = activity;
        this.filterCharacters = new ArrayList<>();
        this.filterCharacters.addAll(characters);
        this.originalCharacters = new ArrayList<>();
        this.originalCharacters.addAll(characters);
        inflater        = activity.getLayoutInflater();
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

/*
    public Filter getFilter() {
        if (filter == null){
            filter  = new CharacterModelFilter();
        }
        return filter;
    }
*/

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
 /*   public void filter(String name, List<String> genderConstraints, List<String> originConstraints, List<String> typeConstraints) {
        List<CharacterModel> listAfterName = new ArrayList<>();
        if (!TextUtils.isEmpty(name)){
            addFilterConstraint(originalCharacters, listAfterName, name);
        }
        else{
            listAfterName.addAll(originalCharacters);
        }

        List<CharacterModel> listAfterGender = new ArrayList<>();
        if (!genderConstraints.isEmpty()){
            for(String genderConstraint : genderConstraints){
                addFilterConstraint(listAfterName, listAfterGender, genderConstraint);
            }
        }
        else{
            listAfterGender.addAll(listAfterName);
        }

        List<CharacterModel> listAfterOrigin = new ArrayList<>();
        if (!originConstraints.isEmpty()){
            for(String originConstraint : originConstraints){
                addFilterConstraint(listAfterGender, listAfterOrigin, originConstraint);
            }
        }
        else{
            listAfterOrigin.addAll(listAfterGender);
        }

        List<CharacterModel> listAfterType = new ArrayList<>();
        if (!typeConstraints.isEmpty()){
            for(String typeConstraint : typeConstraints){
                addFilterConstraint(listAfterOrigin, listAfterType, typeConstraint);
            }
        }
        else{
            listAfterType.addAll(listAfterOrigin);
        }

        updateRecords(listAfterType);
    }
*/

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
        //kMap.keySet();
        for (String str : kMap.keySet()){
        //for(int i=0; i < kMap.size(); i++) {
            List<CharacterModel> listOut = new ArrayList<>();
/*
            List<String> listConstraints;
                listConstraints =  kMap.get(i);

            if (!listConstraints.isEmpty()) {
*/
                for (String constraint : kMap.get(str)) {
                    addFilterConstraint(listIn, listOut, str, constraint);
                }
/*
            } else {
                listOut.addAll(listIn);
            }
*/
            listIn = listOut;
        }

/*
        List<CharacterModel> listAfterGender = new ArrayList<>();
        List<String> genderConstraints = kMap.get(1);
        if (!genderConstraints.isEmpty()){
            for(String genderConstraint : genderConstraints){
                addFilterConstraint(listAfterName, listAfterGender, genderConstraint);
            }
        }
        else{
            listAfterGender.addAll(listAfterName);
        }

        List<CharacterModel> listAfterOrigin = new ArrayList<>();
        List<String> originConstraints = kMap.get(2);
        if (!originConstraints.isEmpty()){
            for(String originConstraint : originConstraints){
                for(CharacterModel characterModel : listAfterGender){
                    if(characterModel.getOrigin().equals(originConstraint)){
                        listAfterOrigin.add(characterModel);
                    }
                }
            }
        }
        else{
            listAfterOrigin.addAll(listAfterGender);
        }

        List<CharacterModel> listAfterType = new ArrayList<>();
        List<String> typeConstraints = kMap.get(0);
        if (!typeConstraints.isEmpty()){
            for(String typeConstraint : typeConstraints){
                for(CharacterModel characterModel : listAfterOrigin){
                    if(characterModel.getType().equals(typeConstraint)){
                        listAfterType.add(characterModel);
                    }
                }
            }
        }
        else{
            listAfterType.addAll(listAfterOrigin);
        }


        updateRecords(listAfterType);
*/
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
/*
    public class CharacterModelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                List<CharacterModel> filteredItems = new ArrayList<>();

                for (CharacterModel characterModel : originalCharacters){

                    if (characterModel.toString().toLowerCase().contains(constraint)){
                        filteredItems.add(characterModel);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalCharacters;
                    result.count = originalCharacters.size();
                }
            }
            return result;
        }

        protected FilterResults performFiltering(List<String> constraint) {
            //constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                List<CharacterModel> filteredItems = new ArrayList<>();

                for (CharacterModel characterModel : originalCharacters){

                   *//* if (characterModel.toString().toLowerCase().contains(constraint)){
                        filteredItems.add(characterModel);
                    }*//*
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalCharacters;
                    result.count = originalCharacters.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            updateRecords((ArrayList<CharacterModel>)results.values);
        }
    }*/
}