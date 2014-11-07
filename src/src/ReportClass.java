

import java.io.Serializable;

public class ReportClass implements Serializable {

	String user;
	String reportTitle;
	String reportMessage;
	String source;
	boolean isOpen = true;
	
	ReportClass(String user, String reportTitle, String reportMessage, String source) {
		this.user = user;
		this.reportTitle = reportTitle;
		this.reportMessage = reportMessage;
		this.source = source;
	}
	
	String getUser() {
		return user;
	}
	
	String getSource() {
		return source;
	}
	
	String getReportTitle() {
		return reportTitle;
	}
	
	String getReportMessage() {
		return reportMessage;
	}
	
	String getListDesc() {
		return new String(user + " - " + reportTitle + " - " + source);
	}
	
	void setResolved() {
		source += " [RESOLVED]";
		isOpen = false;
	}
	
	boolean isOpen() {
		return isOpen;
	}
}
