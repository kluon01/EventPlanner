package com.example.eventplanner.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "EventAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private List<Event> mEventList;

    public EventAdapter(List<Event> eventList) {
        mEventList = eventList;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_events, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_events_empty, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mEventList != null && mEventList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mEventList != null && mEventList.size() > 0) {
            return mEventList.size();
        } else {
            return 1;
        }
    }

    public void setData(List<Event> eventList) {
        mEventList = eventList;
        notifyDataSetChanged();
    }


    public void addData(Event event){
        mEventList.add(event);
        notifyDataSetChanged();
    }

    // ***OLD*** This method will look for event in event list and update it to new data
    public void updateEventListOld(Event updatedEvent){
        if(updateExistingEvent(updatedEvent)){
            notifyDataSetChanged();
            return;
        }
        else{
            addData(updatedEvent);
            notifyDataSetChanged();
        }
    }

    private boolean updateExistingEvent(Event updatedEvent){
        Iterator<Event> itr = mEventList.iterator();
        while(itr.hasNext()){
            Event event = itr.next();
            if(event.getDocumentId().equals(updatedEvent.getDocumentId())){
                event.setDateAndTime(updatedEvent.getDateAndTime());
                event.setInfo(updatedEvent.getInfo());
                event.setLatitude(updatedEvent.getLatitude());
                event.setLongitude(updatedEvent.getLongitude());
                event.setSubtitle(updatedEvent.getSubtitle());
                event.setTitle(updatedEvent.getTitle());
                return true;
            }
        }
        return false;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.thumbnail)
        ImageView coverImageView;

        @BindView(R.id.title)
        TextView titleTextView;

        @BindView(R.id.subTitle)
        TextView subTitleTextView;

        @BindView(R.id.eventInfo)
        TextView infoTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            subTitleTextView.setText("");
            infoTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Event mEvent = mEventList.get(position);

            coverImageView.setImageResource(R.drawable.ic_cake_black_24dp);

            if (mEvent.getTitle() != null) {
                titleTextView.setText(mEvent.getTitle());
            }

            if (mEvent.getSubtitle() != null) {
                subTitleTextView.setText(mEvent.getSubtitle());
            }

            if (mEvent.getInfo() != null) {
                infoTextView.setText(mEvent.getInfo());
            }
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.buttonRetry)
        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

    }
}
