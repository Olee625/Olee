package com.olee.project.test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NormalTest {
    @Test
    public void NewTest() throws IOException {
        //获取文件内容到sb
        File file = new File("my.log");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line + '\n');
            }
            reader.close();
            //正则表达
            String contentsString = contents.toString().replace("\n", "");
            Pattern pattern = Pattern.compile(".*Argument(# Time.*;)");
            Matcher matcher = pattern.matcher(contentsString);
            if (matcher.find())
                System.out.println(matcher.group(1));
            String[] logLists = matcher.group(1).split("# Time:");
            System.out.println(logLists.length);
            for (String log : logLists) {
                System.out.println(log);
            }
            //System.out.println(contents.toString().replace("# administrator command: Ping;\n", ""));
        } catch (IOException e) {
        }


    }

    public void registTest() {
    }
}
