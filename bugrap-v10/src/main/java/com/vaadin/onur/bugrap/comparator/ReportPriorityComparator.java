package com.vaadin.onur.bugrap.comparator;

import java.util.Comparator;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportPriorityComparator implements Comparator<Report> {

	@Override
	public int compare(Report rep1, Report rep2) {
		return rep1.getPriority().compareTo(rep2.getPriority());
	}

	
}
