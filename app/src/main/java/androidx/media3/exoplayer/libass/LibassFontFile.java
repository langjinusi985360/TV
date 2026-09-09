package androidx.media3.exoplayer.libass;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

@UnstableApi
public final class LibassFontFile {

    private static final int TAG_TTCF = 0x74746366;
    private static final int TAG_TTF = 0x00010000;
    private static final int TAG_OTTO = 0x4F54544F;
    private static final int TAG_TRUE = 0x74727565;
    private static final int TAG_TYP1 = 0x74797031;
    private static final int TAG_NAME = 0x6E616D65;
    private static final int NAME_FAMILY = 1;
    private static final int NAME_TYPOGRAPHIC_FAMILY = 16;
    private static final int PLATFORM_UNICODE = 0;
    private static final int PLATFORM_MACINTOSH = 1;
    private static final int PLATFORM_WINDOWS = 3;

    private LibassFontFile() {
    }

    public static String getFamilyName(File file) throws IOException {
        String familyName = readFamilyName(file);
        if (familyName != null && !familyName.isEmpty()) return familyName;
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index > 0 ? name.substring(0, index) : name;
    }

    @Nullable
    private static String readFamilyName(File file) {
        try (RandomAccessFile source = new RandomAccessFile(file, "r")) {
            return readFamilyName(source, sfntOffset(source));
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static long sfntOffset(RandomAccessFile source) throws IOException {
        source.seek(0);
        if (source.readInt() != TAG_TTCF) return 0;
        source.skipBytes(4);
        return Integer.toUnsignedLong(source.readInt());
    }

    @Nullable
    private static String readFamilyName(RandomAccessFile source, long offset) throws IOException {
        source.seek(offset);
        if (!isSfnt(source.readInt())) return null;
        int tableCount = source.readUnsignedShort();
        source.skipBytes(6);
        long nameOffset = -1;
        for (int i = 0; i < tableCount; i++) {
            int tag = source.readInt();
            source.skipBytes(4);
            long tableOffset = Integer.toUnsignedLong(source.readInt());
            source.skipBytes(4);
            if (tag == TAG_NAME) {
                nameOffset = tableOffset;
                break;
            }
        }
        if (nameOffset < 0) return null;
        source.seek(nameOffset);
        source.skipBytes(2);
        int count = source.readUnsignedShort();
        long storage = nameOffset + source.readUnsignedShort();
        String family = null;
        String typographicFamily = null;
        for (int i = 0; i < count; i++) {
            int platformId = source.readUnsignedShort();
            int encodingId = source.readUnsignedShort();
            source.skipBytes(2);
            int nameId = source.readUnsignedShort();
            int length = source.readUnsignedShort();
            int recordOffset = source.readUnsignedShort();
            if (nameId != NAME_FAMILY && nameId != NAME_TYPOGRAPHIC_FAMILY) continue;
            if (!isReadable(platformId, encodingId)) continue;
            if (nameId == NAME_TYPOGRAPHIC_FAMILY ? typographicFamily != null : family != null) continue;
            long position = source.getFilePointer();
            source.seek(storage + recordOffset);
            byte[] data = new byte[length];
            source.readFully(data);
            source.seek(position);
            String value = decode(platformId, data).trim();
            if (value.isEmpty()) continue;
            if (nameId == NAME_TYPOGRAPHIC_FAMILY) typographicFamily = value;
            else family = value;
            if (typographicFamily != null) break;
        }
        return typographicFamily != null ? typographicFamily : family;
    }

    private static boolean isSfnt(int tag) {
        return tag == TAG_TTF || tag == TAG_OTTO || tag == TAG_TRUE || tag == TAG_TYP1;
    }

    private static boolean isReadable(int platformId, int encodingId) {
        if (platformId == PLATFORM_UNICODE) return true;
        if (platformId == PLATFORM_WINDOWS) return encodingId == 1 || encodingId == 10;
        return platformId == PLATFORM_MACINTOSH && encodingId == 0;
    }

    private static String decode(int platformId, byte[] data) {
        if (platformId == PLATFORM_MACINTOSH) return new String(data, StandardCharsets.US_ASCII);
        return new String(data, StandardCharsets.UTF_16BE);
    }
}
