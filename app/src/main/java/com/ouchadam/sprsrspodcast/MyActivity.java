package com.ouchadam.sprsrspodcast;

import android.app.Activity;
import android.os.Bundle;

import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.ContentProviderOperationExecutable;
import com.ouchadam.sprsrspodcast.persistance.OperationWrapperImpl;
import com.ouchadam.sprsrspodcast.persistance.Persister;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.ItemMarshaller;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new Persister<Item>(getExecutor(), getItemMarshaller());
    }

    private BaseMarshaller<Item> getItemMarshaller() {
        return new ItemMarshaller(new OperationWrapperImpl(), channel);
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(getContentResolver());
    }
}
