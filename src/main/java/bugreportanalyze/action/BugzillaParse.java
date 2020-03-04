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

/**
 * author Wu Qihao modified Song Xuezhi 2020 0207 analyze bug report xml by
 * keyword to find regression bug
 **/
public class BugzillaParse extends BRParse {
	
	@Override
	public void searchCRB(String xmlPath) {
		// Set output
		int expectResultNo=0;
		FileOutputStream outSTr;
		BufferedOutputStream buffer = null;
		try {
			String pattern="_bz.txt";
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

	
		NodeList items = document.getElementsByTagName("bug"); // 拿到文件中的所有bug，list
		int num_item = items.getLength();

		for (int i = 0; i < num_item; i++) {
			System.out.println("processing................"+((float)i/(float)num_item)*100+"%");
			Element item = (Element) items.item(i); // 具体到一个bug标签
			NodeList shortDescNodeList = item.getElementsByTagName("short_desc"); // 获得bug summary
			String BugID = item.getElementsByTagName("bug_id").item(0).getTextContent(); // bug ID
			String shortDesc = shortDescNodeList.item(0).getTextContent();

			// 组合一个bug的所有的长评论
			NodeList longDescNodeList = item.getElementsByTagName("long_desc");
			StringBuffer longDescText = new StringBuffer();

			for (int j = 0; j < longDescNodeList.getLength(); j++) {
				Element longDescNode = (Element) longDescNodeList.item(j); // 得到一个comment
				if (longDescNode==null) {
					continue;
				}
				String commentId = getContext(longDescNode.getElementsByTagName("commentid"));
				String who = getContext(longDescNode.getElementsByTagName("who"));
				String thetext = getContext(longDescNode.getElementsByTagName("thetext"));
				StringBuffer comment = new StringBuffer("comment" + j + " : ").append("commentId" + commentId + " ")
						.append("author:" + who + " ").append("Text:" + thetext + "\n");
				longDescText.append(comment);
			}

			// 如果涉及的所有文本中存在regression关键字，和并发相关关键字
			String text=shortDesc+longDescText.toString();
			if (checkRegressionCondition(text) && checkConcurrencyCondition(text)) {
				++expectResultNo;
				try {
					buffer.write((BugID + ". Summary: " + shortDesc ).getBytes());
					buffer.write(("--------------------------------------------------------------------------------\n")
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