package skyzofresnes.boardgamizer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class CharacterListActivity extends AppCompatActivity {
    final List<CharacterModel> characterList = new ArrayList<>();
    final List<CharacterModel> charactersSelected = new ArrayList<>();
    private ListView listViewCharacter;
    private String boardgame;
    private String boardgameMini;
    private String filename;
    Boolean isMonsters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characterlist_activity2);

        //set the listview
        listViewCharacter = (ListView) findViewById(R.id.listview);

        //get extra declared in previous activity
        boardgame = getIntent().getStringExtra(Constantes.BOARDGAME);
        boardgameMini = getIntent().getStringExtra(Constantes.BOARDGAME_MINI);
        filename = getIntent().getStringExtra(Constantes.FILENAME);

        //set isMonsters
        if (filename.toLowerCase().startsWith(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL_MONSTERS) || filename.toLowerCase().startsWith(Constantes.AROBASE + Constantes.MONSTERS)) {
            isMonsters = Boolean.TRUE;
            ((ImageButton) findViewById(R.id.button_pick)).setImageResource(R.drawable.pick_again_monster1);
            ((ImageButton) findViewById(R.id.button_pick_next)).setImageResource(R.drawable.pick_next_monster1);
        }

        //récupération du drawable selon le jeu choisi
        int bgImageId = getResources().getIdentifier(boardgameMini + Constantes.BG, Constantes.DRAWABLE, getPackageName());
        //set du background selon bgImageId
        findViewById(R.id.characterlist_activity).setBackgroundResource(bgImageId);

        //récupération du textView et set du texte
        TextView textBoardgameChosen = (TextView) findViewById(R.id.boardgame_chosen);
        textBoardgameChosen.setText(boardgame.replaceAll(Constantes.UNDERSCORE, Constantes.SPACE));

        new GetCharacters().execute();
    }

    public void clickButtonPick(View v) {
        int lastCharacter = charactersSelected.size() - 1;
        //choix du character
        randomCharacter();

        if (!isEndList(findViewById(R.id.button_pick_next))) {
            if (v.getTag().equals(getString(R.string.button_draw))){
            //if (((TextView) v).getText().equals(getString(R.string.button_draw))) {
                //button pick next and reset visible
                findViewById(R.id.button_pick_next).setVisibility(View.VISIBLE);
                findViewById(R.id.button_reset).setVisibility(View.VISIBLE);
                //((TextView) v).setText(R.string.button_draw_again);
                v.setTag(R.string.button_draw_again);
            } else {
                charactersSelected.remove(lastCharacter);
            }
        }

        ((CharacterListAdapter) listViewCharacter.getAdapter()).updateRecords(charactersSelected);
    }

    public void clickButtonPickNext(View v) {
        randomCharacter();
        ((CharacterListAdapter) listViewCharacter.getAdapter()).updateRecords(charactersSelected);

        isEndList(v);

        //Display the last element in list
        listViewCharacter.setSelection(charactersSelected.size() - 1);
    }

    public void clickButtonReset(View v) {
        //button pick next and reset gone
        findViewById(R.id.button_pick_next).setVisibility(View.GONE);
        findViewById(R.id.button_reset).setVisibility(View.GONE);

        //set text button pick "choose"
        //((TextView) findViewById(R.id.button_pick)).setText(R.string.button_draw);
        findViewById(R.id.button_pick).setTag(getString(R.string.button_draw));

        //Display button pick
        findViewById(R.id.button_pick).setVisibility(View.VISIBLE);

        //clear charactersSelected
        charactersSelected.clear();

        //update characterList from adapter
        ((CharacterListAdapter) listViewCharacter.getAdapter()).updateRecords(charactersSelected);
    }

    private void randomCharacter() {
        int position = new Random().nextInt(characterList.size());
        CharacterModel characterModel = characterList.get(position);
        if (charactersSelected.contains(characterModel)) {
            randomCharacter();
        } else {
            charactersSelected.add(characterModel);
        }
    }

    private Boolean isEndList(View v) {
        if (characterList.size() == charactersSelected.size()) {
            v.setVisibility(View.GONE);
            findViewById(R.id.button_pick).setVisibility(View.GONE);
            int idCharacters = 0;
            if (isMonsters){
                idCharacters = getResources().getIdentifier(Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.MONSTERS, Constantes.VALUE, getPackageName());
            }else {
                idCharacters = getResources().getIdentifier(Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.CHARACTERS, Constantes.VALUE, getPackageName());
            }
            Toast.makeText(this, getString(R.string.no_more_characters, getString(idCharacters)), Toast.LENGTH_SHORT).show();
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    private class GetCharacters extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(CharacterListActivity.this,"Parsing file",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int resourceId = getResources().getIdentifier(filename, Constantes.RAW, getPackageName());

            InputStream iStream = null;
            if (resourceId ==0){
                try {
                    iStream = new FileInputStream(getFilesDir() + File.separator + boardgameMini + File.separator + filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                iStream = getResources().openRawResource(resourceId);
            }

            try {
                characterList.addAll(FileUtils.readJsonStream(iStream));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error reading file : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            randomCharacter();
            isEndList(findViewById(R.id.button_pick_next));
            final CharacterListAdapter listViewCharacterAdapter = new CharacterListAdapter(CharacterListActivity.this, charactersSelected);
            listViewCharacter.setAdapter(listViewCharacterAdapter);

            listViewCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long lg) {
                    Log.i("MINE", String.valueOf(position));
                    CharacterModel characterModel = charactersSelected.get(position);
                    //listViewCharacterAdapter.updateRecords(charactersSelected);

                    Toast.makeText(CharacterListActivity.this, characterModel.getName() + " clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}