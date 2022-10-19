package com.example.buscandohuellas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohuellas.Dog;
import com.example.buscandohuellas.R;

import java.util.List;

public class RegisterPetAdapter extends RecyclerView.Adapter<RegisterPetAdapter.MyViewHolder> {
    Context mContext;
    List<Dog> mData;

    public RegisterPetAdapter(Context mContext, List<Dog> mData){
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_dog_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String sInfo1 = mData.get(position).getRaza() + ", " + mData.get(position).getSexo() + ", " + mData.get(position).getColor();
        String sInfo2 = mData.get(position).getEdad() + ", " + mData.get(position).getTamano();
        holder.nombrePerro.setText(mData.get(position).getNombre());
        holder.info1.setText(sInfo1);
        holder.info2.setText(sInfo2);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePerro;
        TextView info1;
        TextView info2;

        public MyViewHolder(View itemView){
            super(itemView);

            nombrePerro = itemView.findViewById(R.id.ubicacionPerro);
            info1 = itemView.findViewById(R.id.contactoPerro);
            info2 = itemView.findViewById(R.id.edadPerro);
        }

    }
}
