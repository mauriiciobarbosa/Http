package com.example.mauricio.http.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mauricio.http.R;
import com.example.mauricio.http.model.Book;
import com.example.mauricio.http.tasks.BookTask;
import com.example.mauricio.http.utils.BookHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mauricio on 15/11/16.
 */
public class BooksFragment extends Fragment {

    protected BookTask mTask;
    protected List<Book> books;
    protected ListView mListView;
    protected TextView mTextMessage;
    protected ProgressBar mProgressBar;
    protected ArrayAdapter<Book> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_books, null);

        mListView = (ListView) layout.findViewById(R.id.lvBooks);
        mTextMessage = (TextView) layout.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);

        mListView.setEmptyView(mTextMessage);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (books == null)
            books = new ArrayList<>();

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, books);
        mListView.setAdapter(mAdapter);

        if (mTask == null) {
            if (BookHttp.hasConnection(getContext())) {
                startDownload();
            } else {
                mTextMessage.setText(R.string.msg_has_no_connection);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            showProgress(true);
        }
    }

    public void showProgress(boolean show) {
        if (show) mTextMessage.setText(R.string.msg_downloading_books);
        mTextMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void startDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new BookTask(this);
            mTask.execute();
        }
    }

    public void updateBooks(List<Book> books) {
        if (books == null) {
            mTextMessage.setText(R.string.msg_download_books_fail);
            return;
        }

        this.books.clear();
        this.books.addAll(books);
        mAdapter.notifyDataSetChanged();
    }
}
