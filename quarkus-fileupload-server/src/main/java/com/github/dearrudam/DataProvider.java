package com.github.dearrudam;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class DataProvider {

    public Data getData(long chunkSize, long size) {
        return new Data(chunkSize, size);
    }

    public static class Data {

        private long chunkSize;
        private long size;

        Data(long chunkSize, long size) {
            this.chunkSize = chunkSize;
            this.size = size;
        }

        public void write(OutputStream out) throws IOException {

            long remaingSize = this.size;

            int letter = (int)'A';

            while (remaingSize > 0) {
                long chunck = (remaingSize < this.chunkSize) ? remaingSize : this.chunkSize;
                for(int x=0;x<chunck;x++) {
                    out.write(letter);
                }
                letter= letter >= ((int)'z') ? 'A': letter + 1;
                remaingSize = remaingSize - chunck;
            }

        }

    }

}
