package superbro.evm.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

class Memory {

    static class Byte extends Memory {
        public byte[] data;
        public Byte() {
            data = new byte[65536];
        }
        public Byte(InputStream stream) throws IOException {
            byte[] buf = new byte[65536];
            stream.read(buf);
            data = Arrays.copyOf(buf, 65536);
        }
    }

    static class Word extends Memory {
        public short[] data;
        public Word() {
            data = new short[65536];
        }
        public Word(InputStream stream) throws IOException {
            byte[] buf = new byte[65536*2];
            stream.read(buf);
            ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
            ShortBuffer buffer = byteBuffer.asShortBuffer();
            short[] array = buffer.array();
            data = Arrays.copyOf(array, 65536);
        }
    }
}
