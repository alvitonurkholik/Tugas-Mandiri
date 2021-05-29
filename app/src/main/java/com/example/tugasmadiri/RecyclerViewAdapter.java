package com.example.tugasmadiri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<ModelNote> noteModelList;
    private AlertDialog dialog;

    //constructor
    public RecyclerViewAdapter(DaftarActivity context, List<ModelNote> noteModelList)
    {
        this.context = context;
        this.noteModelList = noteModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_layout, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelNote noteModel = noteModelList.get(position); //current object item
        holder.title.setText(noteModel.getTitle());
        holder.description.setText(noteModel.getDescription());
        holder.dateAdded.setText(noteModel.getDataNoteAdded());
    }


    @Override
    public int getItemCount() {
        return noteModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView title, description, dateAdded;
        Button deleteBtn, updateBtn, shareBtn;

        public ViewHolder(@NonNull View itemView, Context ctx)
        {
            super(itemView);

            context = ctx;
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            dateAdded = itemView.findViewById(R.id.dateAdded);

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            updateBtn = itemView.findViewById(R.id.updateBtn);

            deleteBtn.setOnClickListener(this);
            updateBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            int position;
            position = getAdapterPosition();
            ModelNote noteModel = noteModelList.get(position);

            switch (v.getId())
            {
                case R.id.deleteBtn:

                    setDeleteBtn(noteModel.getId());

                    break;

                case  R.id.updateBtn:

                    setUpdateBtn(noteModel);
                    break;
            }
        }

        void setDeleteBtn(String id)
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);

            databaseHelper.deleteItem(Integer.parseInt(id));
            noteModelList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

            if(noteModelList.isEmpty())
            {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }

        }

        void setUpdateBtn(final ModelNote noteModel)
        {
            //noteModel = noteModelList.get(getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText titleText, descriptionText, tit;

            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
            //tit = view.findViewById(R.id.tit);

            saveButton = view.findViewById(R.id.saveButton);

            //tit.setText("Update Notes!");
            titleText.setText(noteModel.getTitle());
            descriptionText.setText(noteModel.getDescription());
            saveButton.setText("update");

            builder.setView(view);
            dialog = builder.create(); //creating the dialong
            dialog.show(); //showing the dialog

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHelper databaseHelper = new DatabaseHelper(context);

                    noteModel.setTitle(titleText.getText().toString());
                    noteModel.setDescription(descriptionText.getText().toString());

                    if(titleText.getText().toString().isEmpty() && descriptionText.getText().toString().isEmpty())
                    {
                        Snackbar.make(v, "please enter data!", Snackbar.LENGTH_LONG).show();
                    }
                    else
                    {
                        databaseHelper.updateItem(noteModel);
                        notifyItemChanged(getAdapterPosition(), noteModel);// this helps to refresh the activity
                    }

                    dialog.dismiss();
                }
            });
        }

        void setShareBtn()
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, description.getText().toString());
            intent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
            context.startActivity(intent);
        }
    }
}
