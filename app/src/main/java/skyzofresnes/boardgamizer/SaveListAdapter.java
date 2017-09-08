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
import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class SaveListAdapter extends BaseAdapter {

    Activity activity;
    List<CharacterModel> filterCharacters;
    List<CharacterModel> originalCharacters;
    LayoutInflater inflater;
    private CharacterModelFilter filter;

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

    public Filter getFilter() {
        if (filter == null){
            filter  = new CharacterModelFilter();
        }
        return filter;
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
    public void filter(String constraint) {
        List<CharacterModel> listFiltered = new ArrayList<>();

        if(!TextUtils.isEmpty(constraint)) {
            for (CharacterModel characterModel : originalCharacters)
            {
                if (characterModel.toString().toLowerCase().contains(constraint)){
                    listFiltered.add(characterModel);
                }
            }
        }
        else{
            listFiltered.addAll(originalCharacters);
        }

        updateRecords(listFiltered);
    }

    class ViewHolder {
        ImageView checked;
        TextView name;
        TextView gender;
        TextView origin;
        TextView type;
        ImageView imgCharacter;
    }

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

                   /* if (characterModel.toString().toLowerCase().contains(constraint)){
                        filteredItems.add(characterModel);
                    }*/
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
    }
}