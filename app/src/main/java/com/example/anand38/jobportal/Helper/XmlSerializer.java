package com.example.anand38.jobportal.Helper;

import com.example.anand38.jobportal.bean.Candidate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by anand38 on 7/6/17.
 */

public class XmlSerializer {
    public static String createCandidateXML(Candidate candidate) {
        StringWriter writer=null;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("candidate");
            doc.appendChild(rootElement);

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element city = doc.createElement("email");
            city.appendChild(doc.createTextNode(candidate.getEmail()));
            rootElement.appendChild(city);

            // lastname elements
            Element temperature = doc.createElement("password");
            temperature.appendChild(doc.createTextNode(candidate.getPassword()));
            rootElement.appendChild(temperature);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            writer=new StringWriter();
            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);

            transformer.transform(source, new StreamResult(writer));

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        System.out.println("XmlString:"+writer.toString());
        return writer.toString();
    }

    public static String create_xml_from_email_only(String email) {
        StringWriter writer=null;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("candidate");
            doc.appendChild(rootElement);

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element city = doc.createElement("email");
            city.appendChild(doc.createTextNode(email));
            rootElement.appendChild(city);


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            writer=new StringWriter();
            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);

            transformer.transform(source, new StreamResult(writer));

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        System.out.println("XmlString:"+writer.toString());
        return writer.toString();

    }
}
