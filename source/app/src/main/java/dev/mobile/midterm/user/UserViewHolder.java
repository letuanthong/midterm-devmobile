package dev.mobile.midterm.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dev.mobile.midterm.R;
import lombok.NonNull;

public class UserViewHolder extends RecyclerView.ViewHolder{
    TextView userNameTxtView, userEmailTxtView, userPhoneTxtView;
    ImageView updateBtn, deleteBtn;
    CardView userCard;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userNameTxtView = itemView.findViewById(R.id.userNameTxtView);
        userEmailTxtView = itemView.findViewById(R.id.userEmailTxtView);
        userPhoneTxtView = itemView.findViewById(R.id.userPhoneTxtView);
        updateBtn = itemView.findViewById(R.id.updateUserBtn);
        deleteBtn = itemView.findViewById(R.id.deleteUserBtn);
        userCard = itemView.findViewById(R.id.recyclerCard);
    }
}
