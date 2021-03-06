package com.example.florian.memomaker.app;

/**
 * Created by Studium on 09.03.2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAdapter extends ArrayAdapter<ListViewItem> {

    // declaring our ArrayList of items
    private ArrayList<ListViewItem> objects;
   // private static boolean checkedItemExists = false;
    //private static boolean actionModeActive = false;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<ListViewItem> objects,
    * because it is the list of objects we want to display.
    */
    public ItemAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_row, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current ListViewItem object.
		 */
        ListViewItem i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tid = (TextView) v.findViewById(R.id.id_lvr1);
            TextView tkatdat = (TextView) v.findViewById(R.id.lb_katdat);
            TextView tiname = (TextView) v.findViewById(R.id.lb_text1);
            TextView tat = (TextView) v.findViewById(R.id.at_cbox_value);
            CheckBox bt = (CheckBox) v.findViewById(R.id.checkBox1);



            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tid != null){
                tid.setText(i.getIdToString());
            }
            if (tkatdat != null){
                tkatdat.setText(i.getKatDat());
            }
            if (tiname != null){
                tiname.setText(i.getName());
            }
            if (tat != null){
                tat.setText(i.getArchiveTagToString());
            }
            if (bt != null) {
                if (i.getCheckbox()) {
                    bt.setChecked(true);
                }
                else {
                    bt.setChecked(false);
                }
/*
                bt.setOnClickListener( new View.OnClickListener() {

                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        //country.setSelected(cb.isChecked());
                    }

                });*/
            }


        }

        // the view must be returned to our activity
        return v;

    }


/*
    //Additions
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();


    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
    }

    public static boolean getActionModeActive() {
        return ItemAdapter.actionModeActive;
    }
    public static void setActionModeActive(boolean actionMode) {
        ItemAdapter.actionModeActive = actionMode;
    }
    public static boolean getCheckedItemExists() {
        return ItemAdapter.checkedItemExists;
    }
    public static void setCheckedItemExists(boolean cie) {
        ItemAdapter.checkedItemExists = cie;
    }
    */
}