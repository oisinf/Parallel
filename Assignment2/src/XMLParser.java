import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLParser {
	
	//final static String urlStr = "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=103381&format=xml";
	final static String urlStr ="https://data.smartdublin.ie/cgi-bin/rtpi/realtimebusinformation?stopid=3980&format=xml";
	static HttpURLConnection connection; 
	static InputStream iStream; 
	
    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
	public static void main (String [] args) {
		run();
	}
	
	public static void run(){
		try {
			URL url = new URL(urlStr);
			
			connection = (HttpURLConnection) url.openConnection(); 
			connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            
            iStream = connection.getInputStream();
            buildDOM(iStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			 if (iStream != null) {
                 try {
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                 connection.disconnect();
             }
             else
             {
                 connection.disconnect();
             }
		}
	}
	
	public static void buildDOM(InputStream i) 
	{
		try {
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(i);
		getResults(doc);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getResults(Document d) {
		
		Element element = d.getDocumentElement();
		NodeList nodes = element.getChildNodes();
		NodeList childNode; 
		boolean results = true; 
		for (int j=0; j<nodes.getLength(); j++) {
			
			if (nodes.item(j).getNodeName() == "numberofresults" && nodes.item(j).getTextContent().equals("0")){
				results = false; 
			}
			if (nodes.item(j).getNodeName()=="results") {
				nodes = nodes.item(j).getChildNodes(); 
			}
		}
		
		if (results) {
			for (int i = 0; i<nodes.getLength(); i++) {
				childNode = nodes.item(i).getChildNodes(); 			
				parseDisplay(childNode);
			}
		}
		else {
			System.out.println("There are no buses currently running");
		}
		
	}

	private static void parseDisplay(NodeList nodes) {
		String time = "n/a", due= "n/a", routeNo= "n/a", dest= "n/a", origin= "n/a", operator= "n/a", s ; 
		for (int i = 0; i<nodes.getLength(); i++) {
			s = nodes.item(i).getNodeName(); 			
			switch (s) {
				case "arrivaldatetime" : time = nodes.item(i).getTextContent().substring(11, 19);
				break; 
				case "duetime": due = nodes.item(i).getTextContent(); 
				break; 
				case "destination" : dest = nodes.item(i).getTextContent(); 
				break; 
				case "origin": origin = nodes.item(i).getTextContent(); 
				break; 
				case "route": routeNo	= nodes.item(i).getTextContent();
				break;
				case "operator": operator = nodes.item(i).getTextContent(); 
				default: break ; 
			}
		}
		System.out.println("The next bus on route "+routeNo+" heading for "+dest+" is due in "+due+" minutes at "+time+" and will"
				+ " be departing from "+origin+". \nThis service is operated by "+operator);
	}	
}

