package com.example.mauricio.http.tasks;

import android.os.AsyncTask;

import com.example.mauricio.http.model.Book;
import com.example.mauricio.http.utils.BookHttp;
import com.example.mauricio.http.view.BooksFragment;

import java.util.List;

/**
 * Created by mauricio on 15/11/16.
 */
public class BookTask extends AsyncTask<Void, Void, List<Book>> {

    private BooksFragment fragment;

    public BookTask(BooksFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.showProgress(true);
    }

    @Override
    protected List<Book> doInBackground(Void... voids) {
        return BookHttp.loadJsonBooks();
    }

    @Override
    protected void onPostExecute(List<Book> books) {
        super.onPostExecute(books);
        fragment.showProgress(false);
        fragment.updateBooks(books);
    }
}
