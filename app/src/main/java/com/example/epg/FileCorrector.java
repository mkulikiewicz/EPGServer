package com.example.epg;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FileCorrector {
    private static Map<String, String> excpetionsInTransletion = new HashMap();

    private static void addException() {
        excpetionsInTransletion.put("TLC.pl", "TLC.pl");
        excpetionsInTransletion.put("Superstacja", "superstacja.pl");
        excpetionsInTransletion.put("Nowa TV PL", "nowatv.pl");
        excpetionsInTransletion.put("TVP Polonia PL", "Polonia1.pl");
        excpetionsInTransletion.put("FilmBox Extra HD PL", "filmboxextra.pl");
    }

    public static void correctId(String filePath) {
        addException();
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            doc.getDocumentElement().getNodeName();
            NodeList nodeList = doc.getElementsByTagName("tv").item(0).getChildNodes();
            for (int i = 1; i < nodeList.getLength() - 1; i++) {
                if (nodeList.item(i).getNodeName().equals("channel")) {

                    String validString = getCorrectedString(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue());
                    nodeList.item(i).getAttributes().getNamedItem("id").setNodeValue(validString);
                } else if (nodeList.item(i).getNodeName().equals("programme")) {
                    String validString = getCorrectedString(nodeList.item(i).getAttributes().getNamedItem("channel").getNodeValue());
                    nodeList.item(i).getAttributes().getNamedItem("channel").setNodeValue(validString);
                }
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(file);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        } catch (Exception e) {
            Log.d("AppError", e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getCorrectedString(String invalidString) {

        Iterator it = excpetionsInTransletion.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey().equals(invalidString)) {
                return String.valueOf(pair.getValue());
            }
        }
        return invalidString.toLowerCase().replaceAll(" ", "");
    }
}
