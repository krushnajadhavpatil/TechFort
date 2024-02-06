package com.example.techfort.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class BookmarkId {

    @Exclude
    public String BookmarkId;

    public <T extends com.example.techfort.Model.BookmarkId> T withId(@NonNull final String id) {
        this.BookmarkId = id;
        return (T) this;
    }

}

