package net.kaizoku.myjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import net.kaizoku.myjournal.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView deleteNote;
    public  ImageView editNote;

    public NoteViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.note_title);
        deleteNote = (ImageView)itemView.findViewById(R.id.delete_note);
        editNote = (ImageView)itemView.findViewById(R.id.edit_note);
    }

}
