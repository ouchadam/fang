package com.ouchadam.fang.parsing.helper;

import java.io.*;
import java.net.URL;

public class XMLHelper {

    public enum XmlResource {
        CNET_SMALL("feed_cnet_small.xml", new cnet_small()),
        HSW_SMALL("feed_hsw_small.xml", new hsw_small());

        private final String fileName;
        private final XmlValues xmlValues;

        XmlResource(String fileName, XmlValues xmlValues) {

            this.fileName = fileName;
            this.xmlValues = xmlValues;
        }
    }

    public static XML get(XmlResource xml) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(xml.fileName);

        System.out.println("Url is : " + url);

        File file = new File(url.getPath());
        return new XML(file, xml.xmlValues);
    }

    public static class XML {

        private final File file;
        private final XmlValues xmlValues;

        public XML(File file, XmlValues xmlValues) {
            this.file = file;
            this.xmlValues = xmlValues;
        }

        public XmlValues values() {
            return xmlValues;
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

