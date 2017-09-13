package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public final class FilenameListAdapter extends BaseAdapter {

    Activity activity;
    List<String> filenameList;
    LayoutInflater inflater;

    public FilenameListAdapter(Activity activity) {
        this.activity = activity;
    }

    public FilenameListAdapter(Activity activity, List<String> filenameList) {
        this.activity   = activity;
        this.filenameList = filenameList;
        inflater        = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return filenameList.size();
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
        Resources resources = activity.getResources();
        String packageName = activity.getPackageName();
        String boardgameMini = ((FilenameListActivity) activity).getBoardgameMini();

        if (view == null){

            view = inflater.inflate(R.layout.filename_listview_item, viewGroup, false);

            holder = new ViewHolder();

            holder.filename = (TextView)view.findViewById(R.id.filename);
            holder.nbCharacters = (TextView)view.findViewById(R.id.nb_characters);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        //White text
        holder.filename.setTextColor(Color.WHITE);
        holder.nbCharacters.setTextColor(Color.WHITE);

        String filename = filenameList.get(position);

        if (filename.equals(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL) || filename.equals(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL_MONSTERS)) {
            final String identifier = resources.getString(resources.getIdentifier(Constantes.VALUE_STRING + Constantes.ALL, Constantes.VALUE, packageName));

            String characters = null;

            if (filename.contains(Constantes.MONSTERS)) {
                final String name = Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.MONSTERS;
                final int idMonsters = resources.getIdentifier(name, Constantes.VALUE, packageName);
                characters = resources.getString(idMonsters);
            }else {
                final String name = Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.CHARACTERS;
                final int idCharacters = resources.getIdentifier(name, Constantes.VALUE, packageName);
                characters = resources.getString(idCharacters);
            }

            holder.filename.setText(identifier + Constantes.SPACE + characters);
        }else {
            if (filename.toLowerCase().startsWith(Constantes.AROBASE + Constantes.MONSTERS))
                holder.filename.setText(filename.substring((Constantes.AROBASE + Constantes.MONSTERS).length()));
            else
                holder.filename.setText(filename);
        }

        try {
            int resourceId = resources.getIdentifier(filename, Constantes.RAW, packageName);

            InputStream iStream = null;
            if (resourceId ==0){
                try {
                    iStream = new FileInputStream(activity.getFilesDir() + File.separator + boardgameMini + File.separator + filename);
                } catch (FileNotFoundException e) {
                    Toast.makeText(activity, MessageFormat.format(resources.getString(R.string.error_getting_file), filename, e.getMessage()), Toast.LENGTH_LONG).show();
                }
            }else {
                iStream = resources.openRawResource(resourceId);
            }

            String name = null;
            if (filename.toLowerCase().equals(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL_MONSTERS) || filename.toLowerCase().startsWith(Constantes.AROBASE + Constantes.MONSTERS)){
                name = Constantes.VALUE_PLURALS + boardgameMini + Constantes.NB_MONSTERS;
            }else{
                name = Constantes.VALUE_PLURALS + boardgameMini + Constantes.NB_CHARACTERS;
            }
            final int idNbCharacters = resources.getIdentifier(name, Constantes.VALUE, packageName);
            final int count = FileUtils.countInJsonStream(iStream);
            String characters = resources.getQuantityString(idNbCharacters, count, count);
            holder.nbCharacters.setText(characters);
        } catch (IOException e) {
            Toast.makeText(activity, MessageFormat.format(resources.getString(R.string.error_count_characters_file), filename, e.getMessage()), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void updateRecords(List<String> filenameList){
        this.filenameList = filenameList;

        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView filename;
        TextView nbCharacters;
    }
}