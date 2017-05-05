package com.rakaadinugroho.sealmood.utils;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Raka Adi Nugroho on 4/18/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class FileUtils {
    public static byte[] toBinary(Uri uri) throws URISyntaxException, IOException {
        InputStream inputStream = new FileInputStream(new File(new URI(uri.toString())));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes    = new byte[1024];

        int bytesRead;
        while ((bytesRead = inputStream.read(bytes)) > 0){
            byteArrayOutputStream.write(bytes, 0, bytesRead);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        return data;
    }
}
