package camtrack.cmeet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import camtrack.cmeet.placeholder.PlaceholderContent.PlaceholderItem;
import camtrack.cmeet.databinding.FragmentRoleBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRoleNameRecyclerViewAdapter extends RecyclerView.Adapter<MyRoleNameRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyRoleNameRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    private OnItemClickListener onItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRoleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    public List<PlaceholderItem> getmValues() {
        return mValues;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView mIdView;
        public  TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentRoleBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mContentView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }


        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}