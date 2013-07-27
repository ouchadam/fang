package com.ouchadam.sprsrspodcast;

import java.io.*;
import java.net.URL;

public class XMLHelper {

    public enum XmlResource {
        CNET_SMALL("feed_cnet_small.xml");

        final String fileName;

        XmlResource(String fileName) {
            this.fileName = fileName;
        }
    }

    public static XML get(XmlResource xml) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(xml.fileName);
        File file = new File(url.getPath());
        return new XML(file);
    }

    public static class XML {

        private final File file;

        public XML(File file) {
            this.file = file;
        }

        public InputStream toInputStream() {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File not found");
            }
        }

    }

}

