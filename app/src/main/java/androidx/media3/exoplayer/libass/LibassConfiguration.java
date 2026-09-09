package androidx.media3.exoplayer.libass;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

@UnstableApi
public final class LibassConfiguration {

    @Nullable
    public final String fontConfig;

    @Nullable
    public final String fontsDirectory;

    @Nullable
    public final String defaultFontFamily;

    public final long maximumRenderPixels;

    public final int maximumGlyphCount;

    public final int maximumBitmapCacheSizeMb;

    private LibassConfiguration(@Nullable String fontConfig, @Nullable String fontsDirectory, @Nullable String defaultFontFamily, long maximumRenderPixels, int maximumGlyphCount, int maximumBitmapCacheSizeMb) {
        this.fontConfig = fontConfig;
        this.fontsDirectory = fontsDirectory;
        this.defaultFontFamily = defaultFontFamily;
        this.maximumRenderPixels = maximumRenderPixels;
        this.maximumGlyphCount = maximumGlyphCount;
        this.maximumBitmapCacheSizeMb = maximumBitmapCacheSizeMb;
    }

    public static final class Builder {

        @Nullable
        private String fontConfig;

        @Nullable
        private String fontsDirectory;

        @Nullable
        private String defaultFontFamily;

        private long maximumRenderPixels;

        private int maximumGlyphCount;

        private int maximumBitmapCacheSizeMb;

        public Builder setFontConfig(@Nullable String fontConfig) {
            this.fontConfig = fontConfig;
            return this;
        }

        public Builder setFontsDirectory(@Nullable String fontsDirectory) {
            this.fontsDirectory = fontsDirectory;
            return this;
        }

        public Builder setDefaultFontFamily(@Nullable String defaultFontFamily) {
            this.defaultFontFamily = defaultFontFamily;
            return this;
        }

        public Builder setMaximumRenderPixels(long maximumRenderPixels) {
            this.maximumRenderPixels = maximumRenderPixels;
            return this;
        }

        public Builder setMaximumGlyphCount(int maximumGlyphCount) {
            this.maximumGlyphCount = maximumGlyphCount;
            return this;
        }

        public Builder setMaximumBitmapCacheSizeMb(int maximumBitmapCacheSizeMb) {
            this.maximumBitmapCacheSizeMb = maximumBitmapCacheSizeMb;
            return this;
        }

        public LibassConfiguration build() {
            return new LibassConfiguration(fontConfig, fontsDirectory, defaultFontFamily, maximumRenderPixels, maximumGlyphCount, maximumBitmapCacheSizeMb);
        }
    }
}
