package com.ouchadam.sprsrspodcast.persistance;

import android.net.Uri;

import java.util.Arrays;

public class Query {

    public Uri uri;
    public String[] projection;
    public String selection;
    public String[] selectionArgs;
    public String sortOrder;

    public static class Builder {

        private Query query = new Query();

        public Builder withUri(Uri uri) {
            query.uri = uri;
            return this;
        }

        public Builder withProjection(String[] projection) {
            query.projection = projection;
            return this;
        }

        public Builder withSelection(String selection) {
            query.selection = selection;
            return this;
        }

        public Builder withSelectionArgs(String[] selectionArgs) {
            query.selectionArgs = selectionArgs;
            return this;
        }

        public Builder withSorter(String sortOrder) {
            query.sortOrder = sortOrder;
            return this;
        }

        public Query build() {
            return query;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Query query = (Query) o;

        if (!Arrays.equals(projection, query.projection)) {
            return false;
        }
        if (selection != null ? !selection.equals(query.selection) : query.selection != null) {
            return false;
        }
        if (!Arrays.equals(selectionArgs, query.selectionArgs)) {
            return false;
        }
        if (sortOrder != null ? !sortOrder.equals(query.sortOrder) : query.sortOrder != null) {
            return false;
        }
        if (uri != null ? !uri.equals(query.uri) : query.uri != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (projection != null ? Arrays.hashCode(projection) : 0);
        result = 31 * result + (selection != null ? selection.hashCode() : 0);
        result = 31 * result + (selectionArgs != null ? Arrays.hashCode(selectionArgs) : 0);
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        return result;
    }
}