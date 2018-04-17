package com.example.om.basestructure.db;

import android.util.Log;

import com.couchbase.lite.QueryRow;
import com.couchbase.lite.support.LazyJsonObject;
import com.example.om.basestructure.utils.JsonUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by android on 19.11.17.
 */

public class QueryIterator<T> implements Iterator<T> {

    private static final String LOG_TAG = QueryIterator.class.getSimpleName();
    private List<QueryRow> mRowList;
    private Class mClazz;
    private int mPosition = 0;

    public QueryIterator(List<QueryRow> rows, Class<T> clazz) {
        mRowList = rows;
        mClazz = clazz;
    }

    @Override
    public boolean hasNext() {
        return mRowList.size() > mPosition;
    }

    @Override
    public T next() {
        Log.d(LOG_TAG, "hasNext() is called");
        if (mRowList.size() < mPosition) {
            return null;
        }
        LazyJsonObject value = (LazyJsonObject) mRowList.get(mPosition).getValue();
        mPosition++;
        byte[] json = JsonUtils.objectToJson(value);
        return (T) JsonUtils.jsonToObject(json, mClazz);
    }
}
