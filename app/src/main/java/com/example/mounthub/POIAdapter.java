package com.example.mounthub;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder> {

    private final List<POI> poiList;
    private final Context context;

    public POIAdapter(List<POI> poiList, Context context) {
        this.poiList = poiList;
        this.context = context;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_poi, parent, false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        POI poi = poiList.get(position);
        holder.name.setText(poi.getName());
        holder.distance.setText(String.format("Distance: %.0f m", poi.getDistance()));

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, poi.getName() + " clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    static class POIViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance;

        public POIViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.poi_name);
            distance = itemView.findViewById(R.id.poi_distance);
        }
    }
}
