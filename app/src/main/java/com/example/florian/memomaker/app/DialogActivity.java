package com.example.florian.memomaker.app;

/**
 * Created by Studium on 19.03.2016.
 */


        import android.app.Activity;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.Toast;

public class DialogActivity extends Activity implements OnClickListener {

    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";
    SQLiteDatabase mydb;

    Button ok_btn , cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);

        ok_btn = (Button) findViewById(R.id.ok_btn_id);
        cancel_btn = (Button) findViewById(R.id.cancel_btn_id);

        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        setFinishOnTouchOutside(false);

    }

    @Override
    public void onBackPressed() {
        // prevent "back" from leaving this activity }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ok_btn_id:

                //showToastMessage("Ok Button Clicked");
                checkDeleteDataFromDB(true);
                this.finish();

                break;

            case R.id.cancel_btn_id:

                //showToastMessage("Cancel Button Clicked");
                checkDeleteDataFromDB(false);
                this.finish();

                break;
        }

    }

    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public void checkDeleteDataFromDB(boolean b) {
        if (b) {
            if (PlaceholderFragment.getDelDatasetID() != -1) {
                deleteDatasetFromDB(PlaceholderFragment.getDelDatasetID());
            }
        }
    }

    public void deleteDatasetFromDB(int id) {
        try {
            mydb = getApplicationContext().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("DELETE FROM " + TABLE + " WHERE ID = " + id);
            mydb.close();

            Toast.makeText(getApplicationContext(), "Datensatz wurde gelöscht", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Löschen aus der Datenbank", Toast.LENGTH_LONG).show();
        }
        // Destroy DeleteDialog

    }//Ende deleteDataset

}