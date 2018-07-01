package net.kaizoku.myjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.kaizoku.myjournal.adapter.NoteAdapter;
import net.kaizoku.myjournal.database.SqliteDatabase;
import net.kaizoku.myjournal.database.model.Note;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SqliteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);
        RecyclerView noteView = (RecyclerView)findViewById(R.id.note_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        noteView.setLayoutManager(linearLayoutManager);
        noteView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        List<Note> allNotes = mDatabase.listNotes();

        if(allNotes.size() > 0){
            noteView.setVisibility(View.VISIBLE);
            NoteAdapter mAdapter = new NoteAdapter(this, allNotes);
            noteView.setAdapter(mAdapter);
        }else {
            noteView.setVisibility(View.GONE);
            Toast.makeText(this, "There are no notes in the database. Start adding now", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new quick task
                addTaskDialog();
            }
        });
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_note_layout, null);
        final EditText titleField = (EditText)subView.findViewById(R.id.enter_title);
        final EditText contentField = (EditText)subView.findViewById(R.id.enter_content);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new note");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD NOTE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleField.getText().toString();
                final String content = contentField.getText().toString();
                if(TextUtils.isEmpty(title) || content.isEmpty()){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Note newNote = new Note(title, content);
                    mDatabase.addNote(newNote);
                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

}
