package skyzofresnes.boardgamizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public class BoardgameChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardgamechoose_activity);
    }

    public void boardgameChosen(View view){
        //récupére le tag du bouton image
        String tag = view.getTag().toString();
        Intent nextIntent = new Intent(this, FilenameListActivity.class);
        nextIntent.putExtra(Constantes.BOARDGAME, tag);
        startActivity(nextIntent);
    }
}
