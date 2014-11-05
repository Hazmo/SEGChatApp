package src;

import java.io.Serializable;

public class ReportClass implements Serializable {

	String user;
	String reportTitle;
	String reportMessage;
	
	ReportClass(String user, String reportTitle, String reportMessage) {
		this.user = user;
		this.reportTitle = reportTitle;
		this.reportMessage = reportMessage;
	}
	
	String getUser() {
		return user;
	}
	
	String getReportTitle() {
		return reportTitle;
	}
	
	String getReportMessage() {
		return reportMessage;
	}
	
	String getListDesc() {
		return new String(user + " - " + reportTitle);
	}
	
	void setResolved() {
		reportTitle += " [RESOLVED]";
	}
}
