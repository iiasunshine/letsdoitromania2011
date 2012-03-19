/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2012 by the LDIR development team, further referred to 
 *  as "authors".
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Filename: Profiler.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.profiler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Consumes data produced by the profiling interceptor and provides convenience
 * methods for this data.
 */
@Stateless
@LocalBean
public class Profiler /*implements ProfilerLocal*/ {
	private static final int MAX_SAMPLE_BUCKET = 30;
/*	private static Map<Method, SortedSet<ProfilerSample>> movingWindow = new HashMap<Method, SortedSet<ProfilerSample>>();
	private static Map<Method, Long> totalCount = new HashMap<Method, Long>();
	private static Map<Method, Long> totalDuration = new HashMap<Method, Long>();

	static synchronized void recordCall(Method method, long duration) {
		if (method == null)
			return;

		ProfilerSample sample = new ProfilerSample(duration);

		SortedSet<ProfilerSample> methodSamples = movingWindow.get(method);
		if (methodSamples == null) {
			methodSamples = new TreeSet<ProfilerSample>();
			movingWindow.put(method, methodSamples);
		}

		methodSamples.add(sample);
		if (methodSamples.size() > MAX_SAMPLE_BUCKET)
			methodSamples.remove(methodSamples.first());

		Long count = totalCount.get(method);
		if (count == null)
			count = new Long(0);
		totalCount.put(method, count + 1);

		Long recordedDuration = totalDuration.get(method);
		if (recordedDuration == null)
			recordedDuration = new Long(0);
		totalDuration.put(method, recordedDuration + duration);
	}

	@Override
	public Map<Method, SortedSet<ProfilerSample>> getSamplesMovingWindow() {
		Map<Method, SortedSet<ProfilerSample>> result = new HashMap<Method, SortedSet<ProfilerSample>>();

		synchronized (Profiler.class) {
			for (Map.Entry<Method, SortedSet<ProfilerSample>> entry : movingWindow
					.entrySet())
				result.put(entry.getKey(),
						new TreeSet<ProfilerSample>(entry.getValue()));
		}
		return result;
	}

	@Override
	public Map<Method, Long> getTotalCount() {
		Map<Method, Long> result = new HashMap<Method, Long>();
		synchronized (Profiler.class) {
			for (Map.Entry<Method, Long> entry : totalCount.entrySet())
				result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	@Override
	public Map<Method, Long> getTotalDuration() {
		Map<Method, Long> result = new HashMap<Method, Long>();
		synchronized (Profiler.class) {
			for (Map.Entry<Method, Long> entry : totalDuration.entrySet())
				result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}*/
}
