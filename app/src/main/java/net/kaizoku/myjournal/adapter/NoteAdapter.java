package net.kaizoku.myjournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import net.kaizoku.myjournal.R;
import net.kaizoku.myjournal.database.SqliteDatabase;
import net.kaizoku.myjournal.database.model.Note;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;
    private List<Note> listNotes;
    private SqliteDatabase mDatabase;

    public NoteAdapter(Context context, List<Note> listNotes) {
        this.context = context;
        this.listNotes = listNotes;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Note singleNote = listNotes.get(position);
        holder.title.setText(singleNote.getTitle());
        holder.editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(singleNote);
            }
        });
        holder.deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.deleteNote(singleNote.getId());
                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    private void editTaskDialog(final Note note){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_note_layout, null);
        final EditText titleField = (EditText)subView.findViewById(R.id.enter_title);
        final EditText contentField = (EditText)subView.findViewById(R.id.enter_content);
        if(note != null){
            titleField.setText(note.getTitle());
            contentField.setText(String.valueOf(note.getContent()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit note");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT NOTE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleField.getText().toString();
                final String content = contentField.getText().toString();
                if(TextUtils.isEmpty(title) || content.isEmpty()){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateNote(new Note(note.getId(), title, content));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

}
