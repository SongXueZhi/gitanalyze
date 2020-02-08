package bugreportanalyze.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import configs.Configs;

/**
 * author Wu Qihao modified Song Xuezhi 2020 0207 analyze bug report xml by
 * keyword to find regression bug
 **/
public class BRParsing {
	public void printDesc(String xmlPath) throws Exception {
		// Set output
		int expectResultNo=0;
		FileOutputStream outSTr = new FileOutputStream(new File(Configs.OUTPATH));
		BufferedOutputStream buffer = new BufferedOutputStream(outSTr);

		//读取xml对象
		File bug_report = new File(xmlPath);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.parse(bug_report);
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

			// 如果标题中存在regression ，内容描述中存在thread相关内容
			
			if (checkRegressionCondition(shortDesc) && checkConcurrencyCondition(longDescText.toString())) {
				++expectResultNo;
				buffer.write((BugID + ". Summary: " + shortDesc + "\n Description: \n"+longDescText).getBytes());
				buffer.write(("--------------------------------------------------------------------------------\n")
						.getBytes());

			}
		}
		
		buffer.flush();
		buffer.close();
		System.out.println("No. of br parsed: " + num_item+"output: "+expectResultNo);
		System.out.println("Done.");
	}

	private boolean checkRegressionCondition(String text) {
		boolean flag = false;
		if (text.contains("regression")) {
			flag = true;

		}
		return flag;
	}

	private boolean checkConcurrencyCondition(String text) {
		boolean flag = false;

		if (text.contains("thread") || text.contains("deadlock") || text.contains("synchronized")
				 || text.contains("wait") || text.contains("notify")
				|| text.contains("notifyAll") || text.contains("concurrency")) {
			flag = true;
		}
		return flag;
	}

	// 获取所有单一标签的内容
	private String getContext(NodeList nodeList) {
		if (nodeList != null) {
			return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : null;
		}
		return null;
	}
}