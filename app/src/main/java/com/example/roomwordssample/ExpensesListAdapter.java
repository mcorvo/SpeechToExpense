package com.example.roomwordssample;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ExpensesListAdapter extends ListAdapter<Expenses, WordViewHolder> {

    public ExpensesListAdapter(@NonNull DiffUtil.ItemCallback<Expenses> diffCallback) {
        super(diffCallback);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Expenses current = getItem(position);
        holder.bind(current.getRecord());
    }

    static class WordDiff extends DiffUtil.ItemCallback<Expenses> {

        @Override
        public boolean areItemsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem.getRecord().equals(newItem.getRecord());
        }
    }
}