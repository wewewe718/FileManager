package com.example.filemanager.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filemanager.R;

public class QuickAccessItemsAdapter extends BaseAdapter {

    private static class QuickAccessItemViewModel {
        public int iconId;
        public int textId;
        public String directoryPath;

        public QuickAccessItemViewModel(int iconId, int textId, @NonNull String directoryPath) {
            this.iconId = iconId;
            this.textId = textId;
            this.directoryPath = directoryPath;
        }
    }

    public interface Listener {
        void onItemClicked(@NonNull String directoryPath);
    }


    private LayoutInflater layoutInflater;
    private Listener listener;
    private QuickAccessItemViewModel[] data = {
            // Images item
            new QuickAccessItemViewModel(R.drawable.ic_image, R.string.quick_access_item_images, "/test"),

            // Audio item
            new QuickAccessItemViewModel(R.drawable.ic_audio, R.string.quick_access_item_audio, "/test"),

            // Video item
            new QuickAccessItemViewModel(R.drawable.ic_video, R.string.quick_access_item_video, "/test"),

            // Documents item
            new QuickAccessItemViewModel(R.drawable.ic_document, R.string.quick_access_item_documents, "/test"),

            // Apps item
            new QuickAccessItemViewModel(R.drawable.ic_app, R.string.quick_access_item_apps, "/test"),

            // Downloads item
            new QuickAccessItemViewModel(R.drawable.ic_downloads, R.string.quick_access_item_downloads, "/test")
    };


    public QuickAccessItemsAdapter(@NonNull Context context, @NonNull Listener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_quick_access, parent, false);
        }

        QuickAccessItemViewModel model = data[position];

        // Show quick access item icon
        ImageView iconView = convertView.findViewById(R.id.quick_access_item_icon_image_view);
        iconView.setBackgroundResource(model.iconId);

        // Show quick access item name
        TextView textView = convertView.findViewById(R.id.quick_access_item_icon_text_view);
        textView.setText(model.textId);

        convertView.setOnClickListener(v -> listener.onItemClicked(model.directoryPath));

        return convertView;
    }
}
