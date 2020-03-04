package bugreportanalyze.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import configs.Configs;

public class JiraParse extends  BRParse {

	@Override
	public void searchCRB(String xmlPath) {
		// Set output
		int expectResultNo=0;
		FileOutputStream outSTr;
		BufferedOutputStream buffer = null;
		try {
			String pattern="_jr.txt";
			outSTr = new FileOutputStream(new File(Configs.OUTPATH)+pattern);
			 buffer = new BufferedOutputStream(outSTr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//读取xml对象
		File bug_report = new File(xmlPath);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 document = documentBuilder.parse(bug_report);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		NodeList items = document.getElementsByTagName("item"); // 拿到文件中的所有bug，list
		int num_item = items.getLength();

		for (int i = 0; i < num_item; i++) {
			System.out.println("processing................"+((float)i/(float)num_item)*100+"%");
			Element item = (Element) items.item(i); // 具体到一个bug标签
			NodeList summaryNodeList = item.getElementsByTagName("summary"); // 获得bug summary
			String BugID = item.getElementsByTagName("key").item(0).getTextContent(); // bug ID
			//总结
			String summary = summaryNodeList.item(0).getTextContent();
			//jira的bug描述
			NodeList descList =item.getElementsByTagName("description");
			String desc=getContext(descList);
			//title
			NodeList tilteList=item.getElementsByTagName("title");
			String title= getContext(tilteList);
			//all text combine
			StringBuffer longDescText = new StringBuffer(summary+desc+title);
			//customfieldvalue
			NodeList customFieldValueList=item.getElementsByTagName("customfieldvalue");
			for(int k=0;k<customFieldValueList.getLength();k++) {
				Element customfieldvalue = (Element) customFieldValueList.item(k); // 得到一个comment
				if (customfieldvalue==null) {
					continue;
				}
				String customfield =customfieldvalue.getTextContent();
				longDescText.append(customfield+"");
			}
			

			// 组合一个bug的所有的长评论
			NodeList longDescNodeList = item.getElementsByTagName("comment");
			
			for (int j = 0; j < longDescNodeList.getLength(); j++) {
				Element longDescNode = (Element) longDescNodeList.item(j); // 得到一个comment
				if (longDescNode==null) {
					continue;
				}
				String comment =longDescNode.getTextContent();

				longDescText.append(comment+" ");
			}

			// 如果涉及的所有文本中存在regression关键字，和并发相关关键字
			String text=longDescText.toString();
			if (checkRegressionCondition(text) && checkConcurrencyCondition(text) && !checkIsRegressiontestOrLogistic(text)) {
				++expectResultNo;
				try {
					buffer.write((BugID + ". Summary: " + summary ).getBytes());
					buffer.write(("\n--------------------------------------------------------------------------------\n")
							.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			buffer.flush();
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("No. of br parsed: " + num_item+" , output: "+expectResultNo);
		System.out.println("Done.");
		
	}

}
