package androidx.media3.ui.danmaku;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;

import okhttp3.OkHttpClient;

@UnstableApi
public final class DanmakuPlayerViewController {

    public void bind(PlayerView playerView) {
    }

    public void setOkHttpClient(@Nullable OkHttpClient client) {
    }

    public void setEnabled(boolean enabled) {
    }

    public void setConfig(@Nullable DanmakuConfig config) {
    }

    public void setDataSource(@Nullable Uri uri) {
    }

    public void sendNow(@Nullable String text) {
    }

    public void close() {
    }
}
