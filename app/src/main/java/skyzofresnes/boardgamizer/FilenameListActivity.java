package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class FilenameListActivity extends AppCompatActivity {
    private List<String> filenameList = new ArrayList<>();
    private ListView listViewFilenameList;
    private String boardgame = null;
    private String boardgameMini = null;
    private String listChosen = null;
    private FilenameListAdapter listViewFilenameAdapter = null;
    private static final int SAVE_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filenamelist_activity);

        listViewFilenameList = (ListView) findViewById(R.id.listview_filename_list);

        boardgame = getIntent().getStringExtra(Constantes.BOARDGAME);
        boardgameMini = getString(getResources().getIdentifier(Constantes.VALUE_STRING + boardgame, Constantes.VALUE, getPackageName()));

        //récupération du drawable selon le jeu choisi
        int bgImageId = getResources().getIdentifier(boardgameMini + Constantes.BG, Constantes.DRAWABLE, getPackageName());
        //set du background selon bgImageId
        findViewById(R.id.filenamelist_activity).setBackgroundResource(bgImageId);

        //récupération du textView et set du texte
        TextView textBoardgameChosen = (TextView) findViewById(R.id.boardgame_chosen_filename_list);
        textBoardgameChosen.setText(boardgame.replaceAll(Constantes.UNDERSCORE, Constantes.SPACE));

        new GetFilenameList().execute();
    }

    public String getBoardgameMini() {
        return boardgameMini;
    }

    public FilenameListAdapter getListViewFilenameAdapter() {
        return listViewFilenameAdapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SAVE_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //update la liste des fichiers
                String filename = data.getStringExtra("filenameChosen");
                filenameList.add(filename);
                ((FilenameListAdapter) listViewFilenameList.getAdapter()).updateRecords(filenameList);
            } /*else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }*/
        }
    }

    private class GetFilenameList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(FilenameListActivity.this, "Getting different lists of characters", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Field[] fields = R.raw.class.getFields();

            for(int count=0; count < fields.length; count++){
                String filename = fields[count].getName();
                if (filename.contains(boardgameMini)) {
                    filenameList.add(filename);
                }
            }

            File rootDir=new File(getFilesDir(), boardgameMini);
            File[] listFile = rootDir.listFiles();

            if (null != listFile) {
                for (int count = 0; count < listFile.length; count++) {
                    String filename = listFile[count].getName();
                    filenameList.add(filename);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listViewFilenameAdapter = new FilenameListAdapter(FilenameListActivity.this, filenameList);
            listViewFilenameList.setAdapter(listViewFilenameAdapter);

            //Sur click sur la liste on ouvre l'activité de choix aléatoire
            listViewFilenameList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long lg) {
                    listChosen = filenameList.get(position);
                    Intent nextIntent = new Intent(FilenameListActivity.this, CharacterListActivity.class);
                    nextIntent.putExtra(Constantes.FILENAME, listChosen);
                    nextIntent.putExtra(Constantes.BOARDGAME, boardgame);
                    nextIntent.putExtra(Constantes.BOARDGAME_MINI, boardgameMini);
                    startActivity(nextIntent);
                }
            });

            //Sur click long sur la lsite soit on crée une liste perso soit on supprime une liste perso
            listViewFilenameList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long lg) {
                    //final int itemClicked = position;
                    final String filename = filenameList.get(position);
                    //Si click sur liste perso on supprime la liste
                    if (!filename.toLowerCase().equals(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL)
                            && !filename.toLowerCase().equals(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL_MONSTERS)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FilenameListActivity.this);

                        // Get the layout inflater
                        final LayoutInflater inflater = getLayoutInflater();
                        // Inflate and set the layout for the dialog
                        // Pass null as the parent view because its going in the dialog layout
                        final View inflate = inflater.inflate(R.layout.alertdialog_save_input_filename, null);

                        // Don't display the editText
                        ((EditText) inflate.findViewById(R.id.inputFilename)).setVisibility(View.GONE);
                        builder.setView(inflate)
                                .setTitle(R.string.alert_dialog_delete_title)
                                .setMessage(R.string.alert_dialog_delete_message)
                                .setCancelable(true);

                        // Create the AlertDialog
                        final AlertDialog dialog = builder.create();

                        // Add the buttons
                        ImageButton btnCancel = (ImageButton) inflate.findViewById(R.id.button_cancel);
                        btnCancel.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        ImageButton btnOk = (ImageButton) inflate.findViewById(R.id.button_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                File file = new File(getFilesDir() + File.separator + boardgameMini + File.separator, filename);
                                boolean deleted = file.delete();

                                if (deleted && filenameList.contains(filename)) {
                                    filenameList.remove(filenameList.indexOf(filename));
                                }else{
                                    //Toast.makeText(FilenameListActivity.this, "Deleting file " + filename + " impossible.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(FilenameListActivity.this, MessageFormat.format(getString(R.string.deleting_file_impossible), filename), Toast.LENGTH_SHORT).show();
                                }

                                ((FilenameListAdapter) listViewFilenameList.getAdapter()).updateRecords(filenameList);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        //Toast.makeText(FilenameListActivity.this, "long click detected", Toast.LENGTH_SHORT).show();
                    }else{// Si click sur liste de l'appli on crée une liste perso
                        //Ouverture de l'activité création liste
                        Intent nextIntent = new Intent(FilenameListActivity.this, SaveListActivity.class);
                        nextIntent.putExtra(Constantes.FILENAME, filename);
                        nextIntent.putExtra(Constantes.BOARDGAME_MINI, boardgameMini);
                        startActivityForResult(nextIntent, SAVE_FILE_REQUEST);
                    }
                    return true;
                }
            });
        }
    }
}
