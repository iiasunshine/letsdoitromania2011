<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="ro.ldir.profiler.ProfilerLocal"%>
<%@ page import="ro.ldir.profiler.ProfilerSample"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.lang.reflect.Method"%>
<%@ page import="java.util.SortedSet"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="java.util.TreeSet"%>
<%@ page import="java.util.HashMap"%>


<%!private ProfilerLocal profiler = null;

	public void jspInit() {
		try {
			InitialContext ctx = new InitialContext();
			profiler = (ProfilerLocal) ctx
					.lookup("java:global/LDIRBackend/LDIRBackendEJB/Profiler!ro.ldir.profiler.Profiler");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
				new Comparator<Map.Entry<K, V>>() {
					@Override
					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
						return e2.getValue().compareTo(e1.getValue());
					}
				});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}%>

<%
	Map<Method, SortedSet<ProfilerSample>> movingWindow = profiler
			.getSamplesMovingWindow();
	Map<Method, Long> totalCount = profiler.getTotalCount();
	Map<Method, Long> totalDuration = profiler.getTotalDuration();
	Map<Method, Float> averages = new HashMap<Method, Float>();

	for (Map.Entry<Method, Long> entry : totalDuration.entrySet()) {
		float average = new Float(entry.getValue())
				/ totalCount.get(entry.getKey());
		averages.put(entry.getKey(), average);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;
charset=UTF-8">
<title>Let's Do It Romania! Profiling Data</title>
<style type='text/css'>
table {
	border-collapse: collapse;
	cell-padding: 2;
}

tr.inner {
	font-size: 9pt;
}

tr.inner:hover {
	background: #F0F0F0;
}

td {
	text-align: center;
}

td.methodName {
	text-align: left;
	font-family: monospace, courier;
}

body {
	font-family: helvetica, impact, sans-serif;
	font-size: 11pt;
}
</style>
</head>
<body>
	<h1>Let's Do It Romania! Profiling Data</h1>
	<p>
		This page contains internal technical information regarding the
		performance of the Let's Do It Romania! backend. The data has been
		generated on
		<%=new Date()%>.
	</p>
	<table border="1">
		<tr>
			<th rowspan="2">Method Name</th>
			<th rowspan="2">Total Call Duration</th>
			<th rowspan="2">Calls Count</th>
			<th rowspan="2">Average Call Duration</th>
			<th colspan="3">Samples In Moving Window</th>
		</tr>
		<tr>
			<th>First</th>
			<th>Count</th>
			<th>Average Call Duration</th>
		</tr>
		<%
			SortedSet<Map.Entry<Method, Long>> data = entriesSortedByValues(totalDuration);
			for (Map.Entry<Method, Long> entry : data) {
				SortedSet<ProfilerSample> lastSamples = movingWindow.get(entry
						.getKey());
				long totalLastDuration = 0;
				for (ProfilerSample sample : lastSamples)
					totalLastDuration += sample.getDuration();
		%>
		<tr class="inner">
			<td class="methodName"><%=entry.getKey()%></td>
			<td><%=totalDuration.get(entry.getKey())%> ms</td>
			<td><%=totalCount.get(entry.getKey())%></td>
			<td><%=averages.get(entry.getKey())%> ms</td>
			<td><%=lastSamples.first().getTimestamp()%></td>
			<td><%=lastSamples.size()%></td>
			<td><%=(float) totalLastDuration / lastSamples.size()%> ms</td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>
