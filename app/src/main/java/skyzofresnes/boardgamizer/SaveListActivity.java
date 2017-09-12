package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveListActivity extends AppCompatActivity {
    final List<CharacterModel> characterModels = new ArrayList<>();  // Where we track the selected items
    final List<CharacterModel> mSelectedCharacters = new ArrayList<>();  // Where we track the selected items
    final FiltersModel filtersModel = new FiltersModel();   // where we track the different string for the filter
    private ListView saveListViewCharacter;
    private SaveListAdapter saveListViewCharacterAdapter = null;

    //private Button buttonsave;
    private ImageButton buttonsave;
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
        buttonsave = (ImageButton) findViewById(R.id.button_save);
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

    public void clickButtonFilters(View v){
        classifiedCharactersList();

        final AlertDialog.Builder builderFilters = new AlertDialog.Builder(this, R.style.MyDialogAlert);
        //final AlertDialog.Builder builderFilters = new AlertDialog.Builder(this);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflate = getLayoutInflater().inflate(R.layout.alertdialog_filters, null);

        //Get all objects in inflate
        final EditText inputName = (EditText) inflate.findViewById(R.id.inputName);
        final ExpandableListView expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandableListViewFilters);
/*
        final LinearLayout radioGroupGender = (LinearLayout) inflate.findViewById(R.id.layout_gender);
        final LinearLayout radioGroupOrigin = (LinearLayout) inflate.findViewById(R.id.layout_origin);
        final LinearLayout scrollViewOrigin = (LinearLayout) inflate.findViewById(R.id.scrollView_layout_origin);
        final LinearLayout scrollViewType = (LinearLayout) inflate.findViewById(R.id.scrollView_layout_type);
*/
        final ImageButton btnEraseAll = (ImageButton) inflate.findViewById(R.id.button_erase_all);
        final ImageButton btnCancel = (ImageButton) inflate.findViewById(R.id.button_cancel);
        final ImageButton btnOk = (ImageButton) inflate.findViewById(R.id.button_ok);

        final ImageView imgGroupFilters = (ImageView) inflate.findViewById(R.id.imageView_groupFilters);

/*        Resources resources = getResources();
        String packageName = getPackageName();

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {

                        Color.GRAY //disabled
                        ,Color.BLACK //enabled

                }
        );



        //Add checkBoxes in Gender, Origin and Type Layout

        for (String gender : filtersModel.getGender()){
            CheckBox chkBox = new CheckBox(this);
            chkBox.setButtonTintList(colorStateList);
            chkBox.setTextColor(Color.BLACK);
            chkBox.setText(resources.getIdentifier(Constantes.VALUE_STRING + gender.substring(1), Constantes.VALUE, packageName));
            chkBox.setTag(gender);
            radioGroupGender.addView(chkBox);
        }

        for (String origin : filtersModel.getOrigin()){
            CheckBox chkBox = new CheckBox(this);
            chkBox.setButtonTintList(colorStateList);
            chkBox.setTextColor(Color.BLACK);
            chkBox.setText(resources.getIdentifier(Constantes.VALUE_STRING + origin.substring(1), Constantes.VALUE, packageName));
            chkBox.setTag(origin);
            scrollViewOrigin.addView(chkBox);
        }

        for (String type : filtersModel.getType()){
            CheckBox chkBox = new CheckBox(this);
            chkBox.setButtonTintList(colorStateList);
            chkBox.setTextColor(Color.BLACK);
            chkBox.setText(resources.getIdentifier(Constantes.VALUE_STRING + type.substring(1), Constantes.VALUE, packageName));
            chkBox.setTag(type);
            scrollViewType.addView(chkBox);
        }
*/

        final HashMap<String, List<String>> expandableListDetail = filtersModel.getFilters();
        final List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        final ArrayList<ArrayList<Integer>> check_states = new ArrayList<>();

        for(int i = 0; i < expandableListTitle.size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            for(int j = 0; j < expandableListDetail.get(expandableListTitle.get(i)).size(); j++) {
                tmp.add(0);
            }
            check_states.add(tmp);
        }

        final CustomExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, check_states);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();

                //imgGroupFilters.setBackgroundResource(R.drawable.collapse);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

                //imgGroupFilters.setBackgroundResource(R.drawable.expand);
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int groupPosition, long id) {
                ImageView icon=(ImageView)paramView.findViewById(R.id.imageView_groupFilters);
                for (int i = 0; i < expandableListTitle.size(); i++) {
                    if (i == groupPosition) {
                        if (paramExpandableListView.isGroupExpanded(i)) {
                            paramExpandableListView.collapseGroup(i);
                            icon.setImageResource(R.drawable.collapse);
                        } else {
                            paramExpandableListView.expandGroup(i);
                            icon.setImageResource(R.drawable.expand);
                        }
                    } else {
                        paramExpandableListView.collapseGroup(i);
                        icon.setImageResource(R.drawable.collapse);
                    }
                }
                paramExpandableListView.invalidate();
                return true;            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //tick.setVisibility(View.VISIBLE);
                //v.setChecked(Boolean.TRUE);

                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                if(check_states.get(groupPosition).get(childPosition) == 1) {
                    check_states.get(groupPosition).set(childPosition, 0);

                }else {
                    check_states.get(groupPosition).set(childPosition, 1);
                }

                expandableListAdapter.setCheck_states(check_states);
                expandableListAdapter.notifyDataSetChanged();

                return false;
            }
        });

        //inflate to the dialog builder + title
        builderFilters.setView(inflate)
                .setTitle(R.string.alert_dialog_save_title); //TODO : change title

        //Create the alert dialog + show
        final AlertDialog alertDialogFilters = builderFilters.create();
        alertDialogFilters.show();

        // Add action buttons
        btnEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : ajouter code erase all filters
                inputName.setText(null);

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
                //List<String> genderConstraints = expandableListDetail.get(getString(R.string.radioGroupGender));
                //List<String> Constraints1 = new ArrayList<>();
                //for(String str : genderConstraints){

//                }
/*
                List<String> genderConstraints = new ArrayList<>();
                for (int i=1; i < radioGroupGender.getChildCount(); i++){
                    CheckBox chkBox = (CheckBox) radioGroupGender.getChildAt(i);
                    if (chkBox.isChecked()) {
                        genderConstraints.add(chkBox.getTag().toString());
                    }
                }

                List<String> originConstraints = new ArrayList<>();
                for (int i=1; i < radioGroupOrigin.getChildCount(); i++){
                    CheckBox chkBox = (CheckBox) radioGroupOrigin.getChildAt(i);
                    if (chkBox.isChecked()) {
                        originConstraints.add(chkBox.getTag().toString());
                    }
                }

                List<String> typeConstraints = new ArrayList<>();
                for (int i=1; i < scrollViewType.getChildCount(); i++){
                    CheckBox chkBox = (CheckBox) scrollViewType.getChildAt(i);
                    if (chkBox.isChecked()) {
                        typeConstraints.add(chkBox.getTag().toString());
                    }
                }
*/
                //saveListViewCharacterAdapter.getFilter().filter(nameChosen);
                saveListViewCharacterAdapter.filter(nameChosen, kMap);
                //saveListViewCharacterAdapter.getFilter().filter(constraints);
                alertDialogFilters.dismiss();
            }
        });
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
            //int resourceId = getResources().getIdentifier(boardgameMini + Constantes.UNDERSCORE + Constantes.ALL, Constantes.RAW, getPackageName());
            int resourceId = getResources().getIdentifier(filename, Constantes.RAW, getPackageName());

            InputStream iStream = getResources().openRawResource(resourceId);

            try {
                characterModels.addAll(FileUtils.readJsonStream(iStream));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), MessageFormat.format(getString(R.string.error_reading_file), filename, e.getMessage()), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Error reading file : " + e.getMessage(), Toast.LENGTH_LONG).show();
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