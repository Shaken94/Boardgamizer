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

public class CharacterListAdapter extends BaseAdapter {

    Activity activity;
    List<CharacterModel> characters;
    LayoutInflater inflater;

    public CharacterListAdapter(Activity activity) {
        this.activity = activity;
    }

    public CharacterListAdapter(Activity activity, List<CharacterModel> characters) {
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
            view = inflater.inflate(R.layout.character_listview_item, viewGroup, false);

            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.origin = (TextView) view.findViewById(R.id.origin);
            holder.type = (TextView) view.findViewById(R.id.type);
            holder.imgCharacter = (ImageView) view.findViewById(R.id.imgCharacter);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        CharacterModel characterModel = characters.get(position);

        holder.name.setTextColor(Color.WHITE);
        holder.origin.setTextColor(Color.WHITE);
        holder.type.setTextColor(Color.WHITE);

        Resources resources = activity.getResources();
        String packageName = activity.getPackageName();

        final String name = characterModel.getName();
        if (name.startsWith("@")) {
            holder.name.setText(resources.getIdentifier(Constantes.VALUE_STRING + name.substring(1), Constantes.VALUE, packageName));
        }else{
            holder.name.setText(name);
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
            holder.origin.setText(origin);
        }

        int imgCharacterId = resources.getIdentifier(characterModel.getImage() + "_portrait", Constantes.DRAWABLE, packageName);
        holder.imgCharacter.setBackgroundResource(imgCharacterId);

        return view;
    }

    public void updateRecords(List<CharacterModel> characters){
        this.characters = characters;

        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView name;
        TextView origin;
        TextView type;
        ImageView imgCharacter;
    }
}