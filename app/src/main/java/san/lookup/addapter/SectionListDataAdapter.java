package san.lookup.addapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import san.lookup.R;

/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */

class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<POJO_item> itemsList;
    private Context mContext;

    SectionListDataAdapter(Context context, ArrayList<POJO_item> itemsList) {
        this.itemsList = itemsList;
        mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_categorirs, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        POJO_item singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        String uri = "@drawable/"+singleItem.getUrl();  // where myresource (without the extension) is the file

        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());


        Glide.with(mContext)
                .load(imageResource)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView itemImage;


        SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext, itemsList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT)
                            .show();

                }
            });


        }

    }

}