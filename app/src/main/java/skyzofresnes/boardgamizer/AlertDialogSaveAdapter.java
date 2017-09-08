package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class AlertDialogSaveAdapter extends BaseAdapter {

    Activity activity;
    List<CharacterModel> characters;
    LayoutInflater inflater;

    public AlertDialogSaveAdapter(Activity activity) {
        this.activity = activity;
    }

    public AlertDialogSaveAdapter(Activity activity, List<CharacterModel> characters) {
        this.activity   = activity;
        this.characters = characters;
        inflater        = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return characters.size();
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

        CharacterModel characterModel = characters.get(position);

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
        this.characters = characters;

        notifyDataSetChanged();
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