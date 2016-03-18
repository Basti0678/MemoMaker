package com.example.florian.memomaker.app;


import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;


public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    //Variablen
    ListView listViewtodo;
    ListView listViewmemo;
    ListView listViewArchivTodo;
    ListView listViewArchivMemo;
    ArrayList<ListViewItem> values;
    ArrayList<ListViewItem> valuesMemo;
    ArrayList<ListViewItem> archiveTodoArray;
    ArrayList<ListViewItem> archiveMemoArray;
    //String[] values;
    //String[] valuesMemo;
    //String[] archiveTodoArray;
    //String[] archiveMemoArray;

    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    private static final String ARG_SECTION_NUMBER = "section_number";

   // private static String[] longMenu1;

    public ActionMode mActionMode;
    //ActionMode.Callback mActionModeCallback;
    //SelectionAdapter mAdapter;
    ItemAdapter adapter1;
    ItemAdapter adapter2;
    ItemAdapter adapter3;
    ItemAdapter adapter4;

    CustomViewPager customViewPager;
    public static boolean actionModeCheckedExists = false;
    public static int itemPosSave = -1;
    // Fuer Section 3
    public static String itemType;



    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_archive, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        customViewPager = (CustomViewPager) getActivity().findViewById(R.id.container);


        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){

            View todoView = inflater.inflate(R.layout.fragment_todo, container, false);

            listViewtodo = (ListView) todoView.findViewById(R.id.listViewTodo);


            //Ladefunktion der Datenbank
            values = loadTodoData();


            adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
            listViewtodo.setAdapter(adapter1);
            registerForContextMenu(listViewtodo);
            listViewtodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listViewtodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);

                        if (lvi.getCheckbox()) {
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                        } else {
                            if (!actionModeCheckedExists) {
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            } else {
                                // Alte Position uncheck
                                ListViewItem lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(false);

                                // Aktuelle Position check
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            }
                        }

                        Toast.makeText(getActivity(), "Kurzer Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                        Log.v("long clicked", "pos: " + pos);
                        // Start the CAB using the ActionMode.Callback defined above
                        listViewtodo.setItemChecked(pos, true);

                    }

                }
            });

            //Erste Version AdapterLongclikc
            listViewtodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub


                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewtodo.getItemAtPosition(pos);
                    if (lvi.getCheckbox()) {
                        lvi.setCheckbox(false);
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                    }
                    else {
                        if(!actionModeCheckedExists) {
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                        else {
                            // Alte Position uncheck
                            ListViewItem lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(false);

                            // Aktuelle Position check
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                    }

                    Toast.makeText(getActivity(), "Laaaaanger Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                    Log.v("long clicked", "pos: " + pos);
                    // Start the CAB using the ActionMode.Callback defined above
                    listViewtodo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;

                }
            });

            return todoView;
        }//Ende If TO-DO List-View


        //Arrays für ARCHIVE
        if(getArguments().getInt(ARG_SECTION_NUMBER)== 2){

            View memoView = inflater.inflate(R.layout.fragment_memo, container, false);
            //ListView initialisieren
            listViewmemo = (ListView) memoView.findViewById(R.id.listViewMemo);

            // Ladefunktion der Datenbank
            valuesMemo = loadMemoData();

            adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
            listViewmemo.setAdapter(adapter2);
            registerForContextMenu(listViewmemo);
            listViewmemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listViewmemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewmemo.getItemAtPosition(pos);

                        if (lvi.getCheckbox()) {
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                        } else {
                            if (!actionModeCheckedExists) {
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            } else {
                                // Alte Position uncheck
                                ListViewItem lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(false);

                                // Aktuelle Position check
                                lvi.setCheckbox(true);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                            }
                        }

                        Toast.makeText(getActivity(), "Kurzer Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                        Log.v("long clicked", "pos: " + pos);
                        // Start the CAB using the ActionMode.Callback defined above
                        listViewmemo.setItemChecked(pos, true);

                    }

                }
            });

            //Erste Version AdapterLongclikc
            listViewmemo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub


                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewmemo.getItemAtPosition(pos);
                    if (lvi.getCheckbox()) {
                        lvi.setCheckbox(false);
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                    } else {
                        if (!actionModeCheckedExists) {
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        } else {
                            // Alte Position uncheck
                            ListViewItem lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(false);

                            // Aktuelle Position check
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                        }
                    }

                    Toast.makeText(getActivity(), "Laaaaanger Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                    Log.v("long clicked", "pos: " + pos);
                    // Start the CAB using the ActionMode.Callback defined above
                    listViewmemo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;

                }
            });


            return memoView;
        }//Ende If MEMO List-View

        if (getArguments().getInt(ARG_SECTION_NUMBER)== 3){

            View archiveView = inflater.inflate(R.layout.fragment_archive, container, false);

            //ListView initialisieren
            listViewArchivTodo = (ListView)archiveView.findViewById(R.id.listViewTodoArchive);
            listViewArchivMemo = (ListView)archiveView.findViewById(R.id.listViewMemoArchiv);

            // Routinen fuer erste Listview
            archiveTodoArray = loadTodoDataArchive();
            adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
            listViewArchivTodo.setAdapter(adapter3);
            registerForContextMenu(listViewArchivTodo);
            listViewArchivTodo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listViewArchivTodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewArchivTodo.getItemAtPosition(pos);

                        if (!lvi.getCheckbox()) {
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                            itemType = null;
                        } else {
                            if (!actionModeCheckedExists) {
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            } else {
                                // Alte Position uncheck
                                ListViewItem lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(true);

                                // Aktuelle Position check
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            }
                        }

                        Toast.makeText(getActivity(), "Kurzer Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                        Log.v("long clicked", "pos: " + pos);
                        // Start the CAB using the ActionMode.Callback defined above
                        listViewArchivTodo.setItemChecked(pos, true);

                    }

                }
            });

            //Erste Version AdapterLongclikc
            listViewArchivTodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub


                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewArchivTodo.getItemAtPosition(pos);
                    if (!lvi.getCheckbox()) {
                        lvi.setCheckbox(true);
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                        itemType = null;
                    } else {
                        if (!actionModeCheckedExists) {
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        } else {
                            // Alte Position uncheck
                            ListViewItem lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(true);

                            // Aktuelle Position check
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        }
                    }

                    Toast.makeText(getActivity(), "Laaaaanger Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                    Log.v("long clicked", "pos: " + pos);
                    // Start the CAB using the ActionMode.Callback defined above
                    listViewArchivTodo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;

                }
            });

            // Routinen fuer zweite Listview
            archiveMemoArray = loadMemoDataArchive();
            adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
            listViewArchivMemo.setAdapter(adapter4);
            registerForContextMenu(listViewArchivMemo);
            listViewArchivMemo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listViewArchivMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long id) {
                    if (mActionMode != null) {
                        ListViewItem lvi = (ListViewItem) listViewArchivMemo.getItemAtPosition(pos);

                        if (!lvi.getCheckbox()) {
                            lvi.setCheckbox(true);
                            actionModeCheckedExists = false;
                            itemPosSave = -1;
                            itemType = null;
                        } else {
                            if (!actionModeCheckedExists) {
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            } else {
                                // Alte Position uncheck
                                ListViewItem lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                                lviOldPos.setCheckbox(true);

                                // Aktuelle Position check
                                lvi.setCheckbox(false);
                                actionModeCheckedExists = true;
                                itemPosSave = pos;
                                itemType = lvi.getItemType();
                            }
                        }

                        Toast.makeText(getActivity(), "Kurzer Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                        Log.v("long clicked", "pos: " + pos);
                        // Start the CAB using the ActionMode.Callback defined above
                        listViewArchivMemo.setItemChecked(pos, true);

                    }

                }
            });

            //Erste Version AdapterLongclikc
            listViewArchivMemo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub


                    if (mActionMode != null) {
                        return false;
                    }

                    ListViewItem lvi = (ListViewItem) listViewArchivMemo.getItemAtPosition(pos);
                    if (!lvi.getCheckbox()) {
                        lvi.setCheckbox(true);
                        actionModeCheckedExists = false;
                        itemPosSave = -1;
                        itemType = null;
                    } else {
                        if (!actionModeCheckedExists) {
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        } else {
                            // Alte Position uncheck
                            ListViewItem lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                            lviOldPos.setCheckbox(true);

                            // Aktuelle Position check
                            lvi.setCheckbox(false);
                            actionModeCheckedExists = true;
                            itemPosSave = pos;
                            itemType = lvi.getItemType();
                        }
                    }

                    Toast.makeText(getActivity(), "Laaaaanger Click :) " + pos + " " + lvi.getName() + " " + itemPosSave, Toast.LENGTH_LONG).show();
                    Log.v("long clicked", "pos: " + pos);
                    // Start the CAB using the ActionMode.Callback defined above
                    listViewArchivMemo.setItemChecked(pos, true);
                    mActionMode = getActivity().startActionMode(mActionModeCallback);

                    return true;

                }
            });


            return archiveView;
        }//Ende If ARCHIVE List-View

        return rootView;
    }//Ende onCreateView


    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
        setMenuVisibility(true);
    }

    // Loadtododata mit ListViewItemObject
    public ArrayList<ListViewItem> loadTodoData() {
        // m_parts.add(new
        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 0 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex0 = allrows.getColumnIndex("TYPE");
            Integer cindex1 = allrows.getColumnIndex("PRIORITY");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");
            //tdData = new ListViewItem[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    //tdData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    tdData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex0), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //tdData = new ListViewItem[0];
        }
        return tdData;
    }//Ende loadTodoData
    // LoadMemoDate mit ArrayList Object
    public ArrayList<ListViewItem> loadMemoData() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 0 " +
                    "order by DATEMEMO, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex0 = allrows.getColumnIndex("TYPE");
            Integer cindex1 = allrows.getColumnIndex("DATEMEMO");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex0), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //mmData = new String[0];
        }
        return mmData;
    }//Ende loadMemoData
    // LoadtododataArchiv mit ListViewItemObject
    public ArrayList<ListViewItem> loadTodoDataArchive() {
        // m_parts.add(new
        ArrayList<ListViewItem> tdData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'todo' and ARCHIVE = 1 " +
                    "order by PRIORITY, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex0 = allrows.getColumnIndex("TYPE");
            Integer cindex1 = allrows.getColumnIndex("PRIORITY");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");
            //tdData = new ListViewItem[allrows.getCount()];

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    //tdData[i] = allrows.getString(cindex) + " " + allrows.getString(cindex1);
                    tdData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex0), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //tdData = new ListViewItem[0];
        }
        return tdData;
    }//Ende loadTodoDataArchiv
    // LoadMemoDataArchiv mit ArrayList Object
    public ArrayList<ListViewItem> loadMemoDataArchive() {

        ArrayList<ListViewItem> mmData = new ArrayList<ListViewItem>();
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("select * from " + TABLE + " where TYPE = 'memo' and ARCHIVE = 1 " +
                    "order by DATEMEMO, DESCRIPTION", null);
            Integer cindex = allrows.getColumnIndex("ID");
            Integer cindex0 = allrows.getColumnIndex("TYPE");
            Integer cindex1 = allrows.getColumnIndex("DATEMEMO");
            Integer cindex2 = allrows.getColumnIndex("DESCRIPTION");
            Integer cindex3 = allrows.getColumnIndex("ARCHIVE");

            if(allrows.moveToFirst()) {
                int i = 0;
                do {
                    mmData.add(new ListViewItem(allrows.getInt(cindex), allrows.getString(cindex0), allrows.getString(cindex1),
                            allrows.getString(cindex2), allrows.getInt(cindex3)));
                    i++;
                } while (allrows.moveToNext());
                //Test
                //Toast.makeText(getActivity().getApplicationContext(), "geladen", Toast.LENGTH_LONG).show();
            }
            allrows.close();
            mydb.close();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Fehler beim Lesen der Datenbank", Toast.LENGTH_LONG).show();
            //mmData = new String[0];
        }
        return mmData;
    }//Ende loadMemoDataArchiv

    @Override
    public void setMenuVisibility (final boolean visible){
        super.setMenuVisibility(visible);

        if(getActivity() != null){

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    values = loadTodoData();
                    //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.listview_row,
                    //        android.R.id.text1, values);
                    adapter1 = new ItemAdapter(getContext(), R.layout.listview_row, values);
                    listViewtodo.setAdapter(adapter1);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    valuesMemo = loadMemoData();
                    /*
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                            android.R.id.text1, valuesMemo);
                            */
                    adapter2 = new ItemAdapter(getContext(), R.layout.listview_row, valuesMemo);
                    listViewmemo.setAdapter(adapter2);
                }
            }

            if (visible) {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

                    archiveTodoArray = loadTodoDataArchive();
                    adapter3 = new ItemAdapter(getContext(), R.layout.listview_row, archiveTodoArray);
                    listViewArchivTodo.setAdapter(adapter3);

                    archiveMemoArray = loadMemoDataArchive();
                    adapter4 = new ItemAdapter(getContext(), R.layout.listview_row, archiveMemoArray);
                    listViewArchivMemo.setAdapter(adapter4);

                }
            }

        }
    }

    /**
     * MENU
     */

    //ActionModeCallback Start
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete1, menu);
            customViewPager.setPagingEnabled(false);
            if ((getArguments().getInt(ARG_SECTION_NUMBER) == 1) || (getArguments().getInt(ARG_SECTION_NUMBER) == 2)){
                menu.findItem(R.id.delete_selected).setVisible(false);
            }
            FloatingActionsMenu fabm = (FloatingActionsMenu) getActivity().findViewById(R.id.left_labels);
            fabm.setVisibility(View.INVISIBLE);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_selected:
                    Toast.makeText(getActivity().getApplicationContext(), "Hier kommt es", Toast.LENGTH_LONG).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.move_selected:
                    //Toast.makeText(getActivity().getApplicationContext(), "Hier kommt es wieder", Toast.LENGTH_LONG).show();
                    if (itemPosSave != -1) {
                        //ListViewItem lviPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                        ListViewItem lviPos = null;
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                            lviPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            }
                            else {
                                lviPos.setArchiveTag(0);
                            }
                        }
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                            lviPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            }
                            else {
                                lviPos.setArchiveTag(0);
                            }
                        }
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                            if (itemType.equals("todo")) {
                                lviPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                                if (lviPos.getCheckbox()) {
                                    lviPos.setArchiveTag(1);
                                }
                                else {
                                    lviPos.setArchiveTag(0);
                                }
                            }
                            else {
                                lviPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                                if (lviPos.getCheckbox()) {
                                    lviPos.setArchiveTag(1);
                                }
                                else {
                                    lviPos.setArchiveTag(0);
                                }
                            }
                            //itemType = null;

                        }
                        if (lviPos != null) {
                            if (lviPos.getCheckbox()) {
                                lviPos.setArchiveTag(1);
                            } else {
                                lviPos.setArchiveTag(0);
                            }
                            updateTable(lviPos.getId(), lviPos.getArchiveTag());
                        }

                    }
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {

            // Alte Position uncheck
            if (itemPosSave != -1) {
                ListViewItem lviOldPos;
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                    lviOldPos = (ListViewItem) listViewtodo.getItemAtPosition(itemPosSave);
                    lviOldPos.setCheckbox(false);
                }
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                    lviOldPos = (ListViewItem) listViewmemo.getItemAtPosition(itemPosSave);
                    lviOldPos.setCheckbox(false);
                }
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                    if (itemType.equals("todo")) {
                        lviOldPos = (ListViewItem) listViewArchivTodo.getItemAtPosition(itemPosSave);
                        lviOldPos.setCheckbox(false);
                    }
                    else {
                        lviOldPos = (ListViewItem) listViewArchivMemo.getItemAtPosition(itemPosSave);
                        lviOldPos.setCheckbox(false);
                    }
                    itemType = null;

                }


            }
            customViewPager.setPagingEnabled(true);
            FloatingActionsMenu fabm = (FloatingActionsMenu) getActivity().findViewById(R.id.left_labels);
            fabm.setVisibility(View.VISIBLE);
            mActionMode = null;
            actionModeCheckedExists = false;
            Toast.makeText(getActivity(), "Var. ItemSavePos) "  + itemPosSave, Toast.LENGTH_LONG).show();
            //ItemAdapter.setActionModeActive(false);
            itemPosSave = -1;
            setMenuVisibility(true);
        }


    };

     //Ende ActionMode

    //public void updateTable(int id, String type, String datePrio, String description, int arch) {
    public void updateTable(int id, int arch) {
        try {
            mydb = getActivity().openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            //if (type.equals("todo")) {
                mydb.execSQL("UPDATE " + TABLE + " SET ARCHIVE = " + arch + " WHERE ID = " +id);
            //}
            //else {
            //    mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
            //            "VALUES('memo', '' , '" + "prio" + "', '" + "text" + "', 0)");
            //}

            mydb.close();

            Toast.makeText(getContext(), "Update erfolgreich", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getContext(), "Fehler beim Schreiben in die Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende instertIntoTable

}
