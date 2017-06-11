package com.example.anand38.jobportal.Helper;

import android.util.Xml;

import com.example.anand38.jobportal.bean.Job;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

    public static List<Job> parse_jobs_data(InputStream is,String xml) throws IOException {
        List<Job> list=new ArrayList<>();
        System.out.println("the xml is:"+xml);


        try {
        Document doc = convertStringToDocument(xml);
        doc.getDocumentElement().normalize();

        //parsing using xpath
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/jobs/job";

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                Job job=new Job();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    job.setJob_id(eElement.getElementsByTagName("job_id").item(0).getTextContent());
                    job.setPosition(eElement.getElementsByTagName("position").item(0).getTextContent());
                    job.setLocation(eElement.getElementsByTagName("location").item(0).getTextContent());
                    job.setSalary(eElement.getElementsByTagName("salary").item(0).getTextContent());
                    job.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                    job.setOpenings(eElement.getElementsByTagName("openings").item(0).getTextContent());
                    job.setEligibility(eElement.getElementsByTagName("eligibility").item(0).getTextContent());
                    job.setPosted_on(eElement.getElementsByTagName("posted_on").item(0).getTextContent());
                }
                System.out.println("Jobid:"+job.getJob_id());
                System.out.println("Position"+job.getPosition());
                System.out.println("location"+job.getLocation());
                System.out.println("salary"+job.getSalary());
                System.out.println("description"+job.getDescription());
                System.out.println("openings"+job.getOpenings());
                System.out.println("eligibility"+job.getEligibility());
                System.out.println("posted_on"+job.getPosted_on());

                list.add(job);
            }

           // System.out.println("Now list size:"+list.size());


        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return list;
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
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
