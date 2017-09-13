package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveListActivity extends AppCompatActivity {
    final List<CharacterModel> characterModels = new ArrayList<>();  // Where we track the selected items
    final List<CharacterModel> mSelectedCharacters = new ArrayList<>();  // Where we track the selected items
    final FiltersModel filtersModel = new FiltersModel();   // where we track the different string for the filter
    private ListView saveListViewCharacter;
    private SaveListAdapter saveListViewCharacterAdapter = null;

    //private Button buttonSave;
    private ImageButton buttonSave;
    private ImageButton buttonSelect;
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
        buttonSave = (ImageButton) findViewById(R.id.button_save);
        buttonSave.setEnabled(false);
        buttonSave.setVisibility(View.INVISIBLE);

        //set the button select
        buttonSelect = (ImageButton) findViewById(R.id.button_select);

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

    private void classifiedCharactersList(){
        for (CharacterModel characterModel : characterModels){
            filtersModel.setStrType(getString(R.string.radioGroupType));
            String type = characterModel.getType();
            if (!filtersModel.getType().contains(type)){
                filtersModel.getType().add(type);
            }
            filtersModel.setStrOrigin(getString(R.string.radioGroupOrigin));
            String origin = characterModel.getOrigin();
            if (!filtersModel.getOrigin().contains(origin)){
                filtersModel.getOrigin().add(origin);
            }
            filtersModel.setStrGender(getString(R.string.radioGroupGender));
            String gender = characterModel.getGender();
            if (!filtersModel.getGender().contains(gender)){
                filtersModel.getGender().add(gender);
            }
        }
    }

    private void initializeCheckStates(List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail, ArrayList<ArrayList<Integer>> check_states) {
        for(int i = 0; i < expandableListTitle.size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            for(int j = 0; j < expandableListDetail.get(expandableListTitle.get(i)).size(); j++) {
                tmp.add(0);
            }
            check_states.add(tmp);
        }
    }

    private void checkSelectedCharacter(ImageButton buttonSelect) {
        if (mSelectedCharacters.isEmpty()) {
            //disable button
            buttonSave.setEnabled(false);
            buttonSave.setVisibility(View.INVISIBLE);
            buttonSelect.setTag("selectAll");
            buttonSelect.setImageResource(R.drawable.select_all1);
        } else {
            buttonSave.setEnabled(true);
            buttonSave.setVisibility(View.VISIBLE);
            buttonSelect.setTag("deselectAll");
            buttonSelect.setImageResource(R.drawable.deselect_all1);
        }
    }

    public void clickButtonFilters(View v){
        classifiedCharactersList();

        final AlertDialog.Builder builderFilters = new AlertDialog.Builder(this, R.style.MyDialogAlert);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflate = getLayoutInflater().inflate(R.layout.alertdialog_filters, null);

        //Get all objects in inflate
        final EditText inputName = (EditText) inflate.findViewById(R.id.inputName);
        final ExpandableListView expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandableListViewFilters);
        final ImageButton btnEraseAll = (ImageButton) inflate.findViewById(R.id.button_erase_all);
        final ImageButton btnCancel = (ImageButton) inflate.findViewById(R.id.button_cancel);
        final ImageButton btnOk = (ImageButton) inflate.findViewById(R.id.button_ok);

        final HashMap<String, List<String>> expandableListDetail = filtersModel.getFilters();
        final List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        final ArrayList<ArrayList<Integer>> check_states = new ArrayList<>();

        initializeCheckStates(expandableListTitle, expandableListDetail, check_states);

        final CustomExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, check_states);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int groupPosition, long id) {
                for (int i = 0; i < expandableListTitle.size(); i++) {
                    if (i == groupPosition) {
                        if (paramExpandableListView.isGroupExpanded(i)) {
                            paramExpandableListView.collapseGroup(i);
                        } else {
                            paramExpandableListView.expandGroup(i);
                        }
                    } else {
                        paramExpandableListView.collapseGroup(i);
                    }
                }

                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(check_states.get(groupPosition).get(childPosition) == 1) {
                    check_states.get(groupPosition).set(childPosition, 0);

                }else {
                    check_states.get(groupPosition).set(childPosition, 1);
                }

                //update records
                expandableListAdapter.updateRecords(check_states);

                return true;
            }
        });

        //inflate to the dialog builder + title
        builderFilters.setView(inflate)
                .setTitle(R.string.alert_dialog_filters_title);

        //Create the alert dialog + show
        final AlertDialog alertDialogFilters = builderFilters.create();
        alertDialogFilters.show();

        // Add action buttons
        btnEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName.setText(null);
                check_states.clear();
                initializeCheckStates(expandableListTitle, expandableListDetail, check_states);

                for(int i=0; i < expandableListTitle.size(); i++){
                    expandableListView.collapseGroup(i);
                }

                //update records
                expandableListAdapter.updateRecords(check_states);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                alertDialogFilters.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nameChosen = inputName.getText().toString();

                HashMap<String, List<String>> kMap = new HashMap<>();
                for(int i = 0; i < expandableListTitle.size(); i++) {
                    List<String> tmp = new ArrayList<>();
                    String key = expandableListTitle.get(i);
                    for (int j = 0; j < expandableListDetail.get(key).size(); j++){
                        if (check_states.get(i).get(j) == 1){
                            tmp.add(expandableListDetail.get(key).get(j));
                        }
                    }
                    if (!tmp.isEmpty()) {
                        kMap.put(key, tmp);
                    }
                }

                saveListViewCharacterAdapter.filter(nameChosen, kMap);
                alertDialogFilters.dismiss();
            }
        });
    }

    public void clickButtonSelect(View v){
        ImageButton buttonSelect = (ImageButton) v;
        String tagSelect = buttonSelect.getTag().toString();

        if (tagSelect.equals("selectAll")){
            for (int i = 0; i < characterModels.size(); i++){
                CharacterModel characterModel = characterModels.get(i);
                characterModel.setSelected(true);
                mSelectedCharacters.add(characterModel);
                characterModels.set(i, characterModel);
            }
        }else{
            for (int i = 0; i < characterModels.size(); i++){
                CharacterModel characterModel = characterModels.get(i);
                characterModel.setSelected(false);
                mSelectedCharacters.remove(characterModel);
                characterModels.set(i, characterModel);
            }
        }

        checkSelectedCharacter(buttonSelect);

        //now update adapter
        saveListViewCharacterAdapter.updateRecords(characterModels);
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
                .setTitle(R.string.alert_dialog_save_title);

        final AlertDialog alertDialogSaveInputFilename = builderSetListname.create();
        alertDialogSaveInputFilename.show();

        //open the keyboard automatically
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        // Add action buttons
        ImageButton btnCancel = (ImageButton) inflate.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //close the keyboard automatically
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                alertDialogSaveInputFilename.dismiss();
            }
        });

        final ImageButton btnOk = (ImageButton) inflate.findViewById(R.id.button_ok);
        // Initially disable the button
        btnOk.setEnabled(false);
        btnOk.setVisibility(View.INVISIBLE);

        btnOk.setOnClickListener(new View.OnClickListener() {
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

                    alertDialogSaveInputFilename.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("filenameChosen", filenameChosen);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        EditText inputFilename = (EditText) inflate.findViewById(R.id.inputFilename);
        inputFilename.requestFocus();
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
                    btnOk.setEnabled(false);
                    btnOk.setVisibility(View.INVISIBLE);
                } else {
                    // Something into edit text. Enable the button.
                    btnOk.setEnabled(true);
                    btnOk.setVisibility(View.VISIBLE);
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
            int resourceId = getResources().getIdentifier(filename, Constantes.RAW, getPackageName());

            InputStream iStream = getResources().openRawResource(resourceId);

            try {
                characterModels.addAll(FileUtils.readJsonStream(iStream));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), MessageFormat.format(getString(R.string.error_reading_file), filename, e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            saveListViewCharacterAdapter = new SaveListAdapter(SaveListActivity.this, characterModels);
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

                    checkSelectedCharacter(buttonSelect);

                    characterModels.set(position, model);

                    //now update adapter
                    saveListViewCharacterAdapter.updateRecords(characterModels);
                }
            });
        }
    }
}