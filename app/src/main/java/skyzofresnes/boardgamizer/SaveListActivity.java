package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveListActivity extends AppCompatActivity {
    final List<CharacterModel> characterModels = new ArrayList<>();  // Where we track the selected items
    final List<CharacterModel> mSelectedCharacters = new ArrayList<>();  // Where we track the selected items
    private ListView saveListViewCharacter;

    private Button buttonsave;
    private String boardgameMini;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelist_activity);

        setFinishOnTouchOutside(true);

        //set the listview
        saveListViewCharacter = (ListView) findViewById(R.id.savelistview);

        //set the button save
        buttonsave = (Button) findViewById(R.id.button_save);
        buttonsave.setEnabled(false);
        buttonsave.setVisibility(View.INVISIBLE);

        //get extra declared in previous activity
        boardgameMini = getIntent().getStringExtra(Constantes.BOARDGAME_MINI);
        filename = getIntent().getStringExtra(Constantes.FILENAME);

        //Nom de la box
        final String alert_dialog_add_title = getString(R.string.alert_dialog_add_title);
        String characters = null;
        if (filename.toLowerCase().contains(Constantes.MONSTERS)) {
            characters = getString(getResources().getIdentifier(Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.MONSTERS, Constantes.VALUE, getPackageName()));
        } else {
            characters = getString(getResources().getIdentifier(Constantes.VALUE_STRING + boardgameMini + Constantes.UNDERSCORE + Constantes.CHARACTERS, Constantes.VALUE, getPackageName()));
        }
        final String of = getString(getResources().getIdentifier(Constantes.VALUE_STRING + Constantes.OF, Constantes.VALUE, getPackageName()));
        this.setTitle(alert_dialog_add_title + Constantes.SPACE + of + Constantes.SPACE + characters);

        new GetCharacters().execute();
    }

    public void clickButtonCancel(View v) {
        finish();
    }

    public void clickButtonSave(View v) {
        //Activity activity = FilenameListActivity.this;
        final AlertDialog.Builder builderSetListname = new AlertDialog.Builder(this, R.style.MyDialogAlert);
        // Get the layout inflater
        final LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View inflate = inflater.inflate(R.layout.alertdialog_save_input_filename, null);
        builderSetListname.setView(inflate)
                .setTitle(R.string.alert_dialog_save_title)
                // Add action buttons
                /*.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int id) {
                        EditText inputFilename = (EditText) inflate.findViewById(R.id.inputFilename);
                        String filenameChosen = inputFilename.getText().toString();
                        if (!TextUtils.isEmpty(filenameChosen)) {
                            File rootDir = new File(getFilesDir(), boardgameMini);
                            rootDir.mkdirs();

                            if (filename.toLowerCase().contains(Constantes.MONSTERS))
                                filenameChosen = Constantes.AROBASE + Constantes.MONSTERS + filenameChosen;

                            File file = new File(rootDir, filenameChosen);

                            try {
                                FileUtils.writeJsonStream(new FileOutputStream(file), mSelectedCharacters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("filenameChosen", filenameChosen);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })*/;

        final AlertDialog alertDialogSaveInputFilename = builderSetListname.create();
        // Initially disable the button
        alertDialogSaveInputFilename.show();
        final Button buttonPositive = alertDialogSaveInputFilename.getButton(AlertDialog.BUTTON_POSITIVE);
        buttonPositive.setEnabled(false);
        buttonPositive.setVisibility(View.INVISIBLE);

        EditText inputFilename = (EditText) inflate.findViewById(R.id.inputFilename);
        inputFilename.setTextColor(Color.BLACK);
        inputFilename.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    buttonPositive.setEnabled(false);
                    buttonPositive.setVisibility(View.INVISIBLE);
                } else {
                    // Something into edit text. Enable the button.
                    buttonPositive.setEnabled(true);
                    buttonPositive.setVisibility(View.VISIBLE);
                }
            }
        });

        //inflate.getMeasuredHeight();
        ImageButton btnAdd1 = (ImageButton) inflate.findViewById(R.id.button_cancelfilename);
        btnAdd1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                findViewById(R.id.alertDialog_save_input_filename);
                ((AlertDialog) v.getParent().getParent()).dismiss();
            }
        });

        ImageButton btnAdd2 = (ImageButton) inflate.findViewById(R.id.button_okfilename);
        btnAdd2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText inputFilename = (EditText) inflate.findViewById(R.id.inputFilename);
                String filenameChosen = inputFilename.getText().toString();
                if (!TextUtils.isEmpty(filenameChosen)) {
                    File rootDir = new File(getFilesDir(), boardgameMini);
                    rootDir.mkdirs();

                    if (filename.toLowerCase().contains(Constantes.MONSTERS))
                        filenameChosen = Constantes.AROBASE + Constantes.MONSTERS + filenameChosen;

                    File file = new File(rootDir, filenameChosen);

                    try {
                        FileUtils.writeJsonStream(new FileOutputStream(file), mSelectedCharacters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("filenameChosen", filenameChosen);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private class GetCharacters extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(CharacterListActivity.this,"Parsing file",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //int resourceId = getResources().getIdentifier(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL, Constantes.RAW, getPackageName());
            int resourceId = getResources().getIdentifier(filename, Constantes.RAW, getPackageName());

            InputStream iStream = getResources().openRawResource(resourceId);

            try {
                characterModels.addAll(FileUtils.readJsonStream(iStream));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error reading file : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            final SaveListAdapter saveListViewCharacterAdapter = new SaveListAdapter(SaveListActivity.this, characterModels);
            saveListViewCharacter.setAdapter(saveListViewCharacterAdapter);

            saveListViewCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long lg) {
                    CharacterModel model = characterModels.get(position);

                    if (model.isSelected()) {
                        model.setSelected(false);
                        mSelectedCharacters.remove(model);
                    } else {
                        model.setSelected(true);
                        mSelectedCharacters.add(model);
                    }

                    if (mSelectedCharacters.isEmpty()) {
                        //disable button
                        buttonsave.setEnabled(false);
                        buttonsave.setVisibility(View.INVISIBLE);
                    } else {
                        buttonsave.setEnabled(true);
                        buttonsave.setVisibility(View.VISIBLE);
                    }

                    characterModels.set(position, model);

                    //now update adapter
                    saveListViewCharacterAdapter.updateRecords(characterModels);
                }
            });
        }
    }
}
