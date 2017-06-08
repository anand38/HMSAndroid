package com.example.anand38.jobportal.Helper;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anand38 on 8/6/17.
 */

public class XMLMsgParser {
    private static final String ns = null;
    public static HashMap<String,String>  parse_id_name(InputStream is) {
    HashMap<String,String> map=new HashMap<>();
    XmlPullParser parser = Xml.newPullParser();
    try {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, ns, "candidate");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("id")){
                map.put("id",readTags(parser, "id"));

                //Toast.makeText(this, readTags(parser, "city"), Toast.LENGTH_SHORT).show();
            }else if (name.equals("name")){
                map.put("name",readTags(parser, "name"));

                //Toast.makeText(this, readTags(parser, "temperature"), Toast.LENGTH_SHORT).show();
            } else {
                skip(parser);
            }
        }

    }catch (Exception e){
        e.printStackTrace();
    }


    return map;
}

    private static String readTags(XmlPullParser parser, String tagName)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String res = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return res;
    }
    private static String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.next();

        }
        return result;
    }
    private static void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
