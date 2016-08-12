package com.erosnow.search.common.temp;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class TempUtility {

	public static void main(String[] args) {
		SolrSchemaFields fields = loadConfig(
				TempUtility.class.getClassLoader().getResource("SolrSchema.xml").getFile());
		// System.out.println("Size is "+fields.getFields().size());
		Collections.sort(fields.getFields());
		Map<String, Integer> map = new TreeMap<String, Integer>();
		for (SolrSchemaField field : fields.getFields()) {
			if(map.containsKey(field.getName())){
				map.put(field.getName(),map.get(field.getName())+1);
			}else{
				map.put(field.getName(),1);
			}
			/*System.out.println(
					field.getName() + "\t" + field.getType() + "\t" + field.getIndexed() + "\t" + field.getStored()
							+ "\t" + field.getRequired() + "\t" + field.getDefault1() + "\t" + field.getMultiValued());*/
		}
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
		
		// System.out.println(fields);

	}

	private static SolrSchemaFields loadConfig(String filePath) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SolrSchemaFields.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (SolrSchemaFields) jaxbUnmarshaller.unmarshal(new File(filePath));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while loading file " + filePath);
		}
	}

}
