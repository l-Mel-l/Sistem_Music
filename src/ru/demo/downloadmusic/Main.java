package ru.demo.downloadmusic;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Main {

    private static final String IN_FILE_TXT = "src\\ru\\demo\\downloadmusic\\inFile.txt";
    private static final String OUT_FILE_TXT = "src\\ru\\demo\\downloadmusic\\outFile.txt";
    private static final String PATH_TO_MUSIC = "src\\ru\\demo\\downloadmusic\\music";

    public static void main(String[] args) {
        String Url;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
                while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);
                System.out.println(url + "ссылка");

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                Pattern email_pattern = Pattern.compile("(https:)\\/.*\\.mp3");
                Matcher matcher = email_pattern.matcher(result);
                int i = 0;
                while (matcher.find() && i < 1) {
                    outFile.write(matcher.group() + "\r\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
            String music;
            int count = 0;
            try {
                while ((music = musicFile.readLine()) != null) {
                    downloadUsingNIO(music, PATH_TO_MUSIC + String.valueOf(count) + ".mp3");
                    count++;

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = "src\\ru\\demo\\downloadmusic\\music0.mp3";
        File file = new File(filePath);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}
