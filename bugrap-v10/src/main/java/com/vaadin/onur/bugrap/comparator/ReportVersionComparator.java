package com.vaadin.onur.bugrap.comparator;

import java.util.Comparator;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportVersionComparator implements Comparator<Report> {

	@Override
	public int compare(Report rep1, Report rep2) {
		String ver1 = rep1.getVersion() != null ? rep1.getVersion().getVersion() : "";
		String ver2 = rep2.getVersion() != null ? rep2.getVersion().getVersion() : "";
		return ver1.compareTo(ver2);
	}

}
