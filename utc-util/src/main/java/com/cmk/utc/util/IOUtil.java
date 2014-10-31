package com.cmk.utc.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


/**
 * @author zhangliang
 * @Description: 流文件工具类
 * @version $Rev$
 * @LastModify: $Id$
 */
public class IOUtil {
    /**
     * 流传输
     * 
     * @param in 输入流
     * @param out 输出流
     * @throws IOException IOException
     */
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        ReadableByteChannel bcInChannel = null;
        WritableByteChannel bcOutChannel = null;
        try {
            bcInChannel = Channels.newChannel(in);
            bcOutChannel = Channels.newChannel(out);
            while (bcInChannel.read(buffer) != -1) {
                buffer.flip();
                bcOutChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

}
