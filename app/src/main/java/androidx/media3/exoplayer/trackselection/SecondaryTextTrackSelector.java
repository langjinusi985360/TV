package androidx.media3.exoplayer.trackselection;

import android.content.Context;

import androidx.media3.common.util.UnstableApi;

@UnstableApi
public final class SecondaryTextTrackSelector {

    private SecondaryTextTrackSelector() {
    }

    public static final class Factory implements TrackSelector.Factory {

        private final TrackSelector.Factory delegate;

        public Factory(TrackSelector.Factory delegate) {
            this.delegate = delegate;
        }

        @Override
        public TrackSelector createTrackSelector(Context context) {
            return delegate.createTrackSelector(context);
        }
    }
}
