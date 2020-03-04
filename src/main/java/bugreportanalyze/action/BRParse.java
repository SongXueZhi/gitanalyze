package bugreportanalyze.action;

import org.w3c.dom.NodeList;
/**
 * 
 * @author knightsong
 *
 */
public class BRParse {
	
	protected boolean checkRegressionCondition(String text) {
		boolean flag = false;
		text = text.toLowerCase();
		if (text.contains("regression")) {
			flag = true;

		}
		return flag;
	}

	protected boolean checkConcurrencyCondition(String text) {
		boolean flag = false;
		text = text.toLowerCase();
		if (text.contains("thread") || text.contains("deadlock") || text.contains("synchronized")
				|| text.contains("wait") || text.contains("notify") || text.contains("notifyAll")
				|| text.contains("concurrency")) {
			flag = true;
		}
		return flag;
	}

	protected boolean checkFixedCondition(String text) {
		boolean flag = false;
		text = text.toLowerCase();
		if (text.contains("Fixed ")) {
			flag = true;
		}
		return flag;
	}
	  protected  boolean  checkIsRegressiontestOrLogistic(String text) {
			boolean flag = false;
			text = text.toLowerCase();
			if (text.contains("regression test") || text.contains("logistic regression")) {
				flag = true;
			}
			return flag;
		}
	  

	// 获取所有单一标签的内容
	protected String getContext(NodeList nodeList) {
		if (nodeList != null) {
			return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : null;
		}
		return null;
	}
 
	public void searchCRB(String xmlPath) {
		System.out.print("have no task");
		
	}
}
