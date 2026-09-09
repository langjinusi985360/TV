package androidx.media3.exoplayer.libass;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleParser;

@UnstableApi
public final class LibassPlaybackSession {

    private final LibassConfiguration configuration;

    private final boolean enabled;

    public LibassPlaybackSession(LibassConfiguration configuration, boolean enabled) {
        this.configuration = configuration;
        this.enabled = enabled;
    }

    public LibassConfiguration getConfiguration() {
        return configuration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAvailable() {
        return false;
    }

    public MediaComponents createMediaComponents(MediaItem mediaItem, ExtractorsFactory extractorsFactory) {
        return new MediaComponents(extractorsFactory, new DefaultSubtitleParserFactory());
    }

    public Renderer createClockRenderer() {
        throw new UnsupportedOperationException("libass is unavailable in this build");
    }

    public void setPreloadMediaItem(@Nullable MediaItem mediaItem) {
    }

    public void setBottomPositionFraction(float bottomPositionFraction) {
    }

    public void setSecondaryBottomPositionFraction(float secondaryBottomPositionFraction) {
    }

    public void setFontScale(float fontScale, boolean immediate) {
    }

    public void close() {
    }

    public static final class MediaComponents {

        public final ExtractorsFactory extractorsFactory;

        public final SubtitleParser.Factory subtitleParserFactory;

        public MediaComponents(ExtractorsFactory extractorsFactory, SubtitleParser.Factory subtitleParserFactory) {
            this.extractorsFactory = extractorsFactory;
            this.subtitleParserFactory = subtitleParserFactory;
        }
    }
}
