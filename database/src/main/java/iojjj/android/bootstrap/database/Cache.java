package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;
import android.util.LruCache;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Created by Alexander Vlasov on 08.10.2015.
 */
class Cache {

    private final LruCache<String, ? extends EntityHelper> cache;

    public Cache() {
        cache = new LruCache<>(2048);
    }

    public Cache(@NonNull LruCache<String, ? extends EntityHelper> cache) {
        AssertionUtils.assertNotNull(cache, "Cache");
        this.cache = cache;
    }
}
