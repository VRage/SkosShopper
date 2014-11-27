package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DataSaver {
	File file ;
	FileWriter fout;
	FileInputStream fin;
	XStream xstream;
	public DataSaver()  {
		// TODO Auto-generated constructor stub
		xstream = new XStream(new DomDriver());
		xstream.alias("Row", AltEntriesManager.class);
		file = new File("./AltEntries.xml");
	}
	public void SaveEntries(List<AltEntriesManager> list) throws JAXBException, IOException {
		// TODO Auto-generated method stub
		fout = new FileWriter(file);
		 JAXBContext context = JAXBContext
	                .newInstance(AltEntrieswrapper.class);
		 Marshaller m = context.createMarshaller();
		 m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        AltEntrieswrapper wrapper = new AltEntrieswrapper();
	        wrapper.setListEntryManager(list);
	        
	        m.marshal(wrapper, file);
	}
	public List<AltEntriesManager>  loadEntries() throws JAXBException {
		// TODO Auto-generated method stub
		JAXBContext context = JAXBContext
                .newInstance(AltEntrieswrapper.class);
        Unmarshaller um = context.createUnmarshaller();

        // Reading XML from the file and unmarshalling.
        AltEntrieswrapper wrapper = (AltEntrieswrapper) um.unmarshal(file);
        return wrapper.getListEntryManager();

        // Save the file path to the registry.
     //   setPersonFilePath(file);
	}
@XmlRootElement
static class AltEntrieswrapper{
	static final int id =0;
	private List <AltEntriesManager> list;
	@XmlElement
	public List<AltEntriesManager>getListEntryManager(){
		return list;	}
	public void setListEntryManager(List<AltEntriesManager> list){
		this.list = list;
	}
}
}
