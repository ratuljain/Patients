//package com.example.lol.patients.adapter;
//
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.lol.patients.model.Medicine;
//import com.example.lol.patients.R;
//
//import java.util.List;
//
//public class MedicineRecycler extends RecyclerView.Adapter<MedicineRecycler.MedicineViewHolder> {
//
//    private List<Medicine> contactList;
//
//    public MedicineRecycler(List<Medicine> contactList) {
//        this.contactList = contactList;
//    }
//
//    @Override
//    public int getItemCount() {
//        return contactList.size();
//    }
//
//    @Override
//    public void onBindViewHolder(MedicineViewHolder medicineViewHolder, int i) {
//
//
//        Medicine ci = contactList.get(i);
//
//
//        medicineViewHolder.vMedName.setText(ci.name);
//        medicineViewHolder.vMedQuantity.setText(ci.quantity);
//    }
//
//    @Override
//    public MedicineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View itemView = LayoutInflater.
//                from(viewGroup.getContext()).
//                inflate(R.layout.card_layout, viewGroup, false);
//
//        return new MedicineViewHolder(itemView);
//    }
//
//    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
//
//
//        protected TextView vMedName;
//        protected TextView vMedQuantity;
//
//
//        public MedicineViewHolder(View v) {
//            super(v);
//
//
//            vMedName =  (TextView) v.findViewById(R.id.medicineName);
//            vMedQuantity = (TextView)  v.findViewById(R.id.quantity);
//        }
//    }
//}
