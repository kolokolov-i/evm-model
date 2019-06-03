package superbro.evm.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Memory {

    public static class Byte extends Memory implements Cloneable {
        public byte[] data;
        public Byte() {
            data = new byte[65536];
        }
        private Byte(byte[] arr) {data = arr.clone();}
        public Byte(InputStream stream) throws IOException {
            byte[] buf = new byte[65536];
            stream.read(buf);
            data = Arrays.copyOf(buf, 65536);
        }
        public void write(OutputStream stream) throws IOException {
            stream.write(data);
        }
        @Override
        public Memory.Byte clone(){
            return new Byte(this.data);
        }
    }

    public static class Word extends Memory implements Cloneable {
        public short[] data;
        private Word(short[] arr){
            data = arr.clone();
        }
        public Word() {
            data = new short[65536];
        }
        public Word(InputStream stream) throws IOException {
            byte[] buf = new byte[65536*2];
            data = new short[65536];
            stream.read(buf);
            ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(data);
        }
        public void write(OutputStream stream) throws IOException {
            byte[] bytes = new byte[65536*2];
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(data);
            stream.write(bytes);
        }
        @Override
        public Memory.Word clone(){
            return new Word(this.data);
        }
    }
}
