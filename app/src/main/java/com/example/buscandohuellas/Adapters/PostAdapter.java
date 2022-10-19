package com.example.buscandohuellas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohuellas.Dog;
import com.example.buscandohuellas.Place;
import com.example.buscandohuellas.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context mContext;
    List<Dog> mData;
    //List<Place> mData2;

    public PostAdapter(Context mContext, List<Dog> mData){
        this.mContext = mContext;
        this.mData = mData;
        //this.mData2 = mData2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String sNombre = "Nombre: " + mData.get(position).getNombre();
        String sContacto = "Contacto: " + mData.get(position).getContacto();
        //String sUbicacion = mData2.get(position).getLugar();
        String sRaza = "Raza: " + mData.get(position).getRaza();
        String sSexo = "Sexo: " + mData.get(position).getSexo();
        String sDetalleSalud = "Detalles S.: " + mData.get(position).getDetalles_salud();
        String sTamano = "Peso: " + mData.get(position).getTamano();
        String sColor = "Color: " + mData.get(position).getColor();
        String sDetalleAp = "Detalles A.: " + mData.get(position).getDetalles_ap();
        String sEdad = "Edad: " + mData.get(position).getEdad();
        String sComportamiento = mData.get(position).getComportamiento();
        holder.nombrePerro.setText(sNombre);
        //holder.ubicacionPerro.setText(sUbicacion);
        holder.contactoPerro.setText(sContacto);
        holder.razaPerro.setText(sRaza);
        holder.sexoPerro.setText(sSexo);
        holder.detalleSaludPerro.setText(sDetalleSalud);
        holder.TamanoPerro.setText(sTamano);
        holder.colorPerro.setText(sColor);
        holder.detalleApPerro.setText(sDetalleAp);
        holder.edadPerro.setText(sEdad);
        holder.comportamientoPerro.setText(sComportamiento);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombrePerro;
        //TextView ubicacionPerro;
        TextView contactoPerro;
        TextView razaPerro;
        TextView sexoPerro;
        TextView detalleSaludPerro;
        TextView TamanoPerro;
        TextView colorPerro;
        TextView detalleApPerro;
        TextView edadPerro;
        TextView comportamientoPerro;

        public MyViewHolder(View itemView){
            super(itemView);

            nombrePerro = itemView.findViewById(R.id.nombrePerro);
            contactoPerro = itemView.findViewById(R.id.contactoPerro);
            edadPerro = itemView.findViewById(R.id.edadPerro);
            //ubicacionPerro = itemView.findViewById(R.id.ubicacionPerro);
            razaPerro = itemView.findViewById(R.id.razaPerro);
            sexoPerro = itemView.findViewById(R.id.sexoPerro);
            detalleSaludPerro = itemView.findViewById(R.id.detalleSaludPerro);
            TamanoPerro = itemView.findViewById(R.id.pesoPerro);
            colorPerro = itemView.findViewById(R.id.colorPerro);
            detalleApPerro = itemView.findViewById(R.id.detalleApPerro);
            comportamientoPerro = itemView.findViewById(R.id.comportamientoPerro);
        }

    }
}
