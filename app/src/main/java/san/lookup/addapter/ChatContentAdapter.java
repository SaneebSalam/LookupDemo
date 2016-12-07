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

import java.util.List;

import san.lookup.R;

/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */

public class ChatContentAdapter extends RecyclerView.Adapter<ChatContentAdapter.ViewHolder> {
    private List<POJO_item> feedItems;
    private Context mContext;

    public ChatContentAdapter(Context context, List<POJO_item> feedItems) {
        this.feedItems = feedItems;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ChatContentAdapter.ViewHolder holder, int position) {
        holder.name.setText(feedItems.get(position).getName());
        holder.address.setText(feedItems.get(position).getAddress());
        holder.distance.setText(feedItems.get(position).getLatlng());

        Glide.with(mContext)
                .load(feedItems.get(position).getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.profilepic);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, distance;
        ImageView profilepic;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_chat, parent, false));
            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            distance = (TextView) itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Toast.makeText(mContext, feedItems.get(getAdapterPosition() - 3).getName(), Toast.LENGTH_SHORT)
                        .show();

                }
            });
        }
    }
}



