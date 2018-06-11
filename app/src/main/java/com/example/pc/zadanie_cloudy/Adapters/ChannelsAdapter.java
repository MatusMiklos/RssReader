package com.example.pc.zadanie_cloudy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.zadanie_cloudy.Pojo.Channels;
import com.example.pc.zadanie_cloudy.R;
import com.example.pc.zadanie_cloudy.ChannelActivity;
import com.example.pc.zadanie_cloudy.RestAPI.RestApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 03.12.2017.
 */

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.SubscriptionViewHolder>{

    private List<Channels> channelsList;
    private int userId;//ID prihlaseneho uzivatela
    private Context context;//dana aktivita
    public static final String ENDPOINT_URL = "http://act7.azurewebsites.net/";
    private RestApi restApi;
    private int mode;//urcuje, ci riesime prihlasenie / odhlasenie odberu

    public ChannelsAdapter(List<Channels> channelsList, int userId, RestApi restApi, int mode) {
        this.channelsList = channelsList;
        this.userId = userId;
        this.restApi = restApi;
        this.mode = mode;
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_channels, parent, false);
        return new SubscriptionViewHolder(view);
    }

    //funkcionalita zobrazenia poloziek v recyclerview
    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, final int position) {
        final Channels channels = channelsList.get(position);
        holder.subcription_banner.setText(channels.getTitle());
        holder.subcription_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, channels.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ChannelActivity.class);
                intent.putExtra("subscription", channels.getId());
                intent.putExtra("title", channels.getTitle());
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
        if (mode ==0) {
            holder.subsciption_unsubcribe.setImageResource(R.drawable.unsubscribe);
            holder.subsciption_unsubcribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unsubscribeChannel(position, channels.getId());
                }
            });
        } else {
            holder.subsciption_unsubcribe.setImageResource(R.drawable.subscribe);
            holder.subsciption_unsubcribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subscribeChannel(position, channels.getId());
                }
            });
        }
    }

    //velkost data listu
    @Override
    public int getItemCount() {return channelsList.size();}

    class SubscriptionViewHolder extends RecyclerView.ViewHolder{
        TextView subcription_banner;
        ImageView subsciption_unsubcribe;
        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            subcription_banner = (TextView) itemView.findViewById(R.id.channelsText);
            subsciption_unsubcribe = (ImageView) itemView.findViewById(R.id.channelsUnsubscribe);
        }
    }

    //dopyt pre zrusenie odberu
    public void unsubscribeChannel(int position, int websiteId) {
        channelsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, channelsList.size());

        Call <Channels> call = restApi.unsubscribe(userId, websiteId);
        call.enqueue(new Callback<Channels>() {
            @Override
            public void onResponse(Call<Channels> call, Response<Channels> response) {
                Log.d("subs", "done");
            }

            @Override
            public void onFailure(Call<Channels> call, Throwable t) {

            }
        });
    }

    //dopyt pre prihlasenie odberu
    public void subscribeChannel(int position, int websiteId) {
        channelsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, channelsList.size());

        Call <Channels> call = restApi.subscribe(userId, websiteId);
        call.enqueue(new Callback<Channels>() {
            @Override
            public void onResponse(Call<Channels> call, Response<Channels> response) {
                Log.d("subs", "done");
            }

            @Override
            public void onFailure(Call<Channels> call, Throwable t) {

            }
        });
    }


}
