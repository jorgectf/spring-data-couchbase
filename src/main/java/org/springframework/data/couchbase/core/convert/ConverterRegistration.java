/*
 * Copyright 2012-2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.couchbase.core.convert;

import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.data.couchbase.core.mapping.CouchbaseSimpleTypes;
import org.springframework.util.Assert;

/**
 * Conversion registration information.
 *
 * @author Oliver Gierke
 * @author Michael Nitschinger
 * @author Mark Paluch
 */
class ConverterRegistration {

	private final ConvertiblePair convertiblePair;
	private final boolean reading;
	private final boolean writing;

	/**
	 * Creates a new {@link ConverterRegistration}.
	 *
	 * @param convertiblePair must not be {@literal null}.
	 * @param isReading whether to force to consider the converter for reading.
	 * @param isWriting whether to force to consider the converter for reading.
	 */
	public ConverterRegistration(ConvertiblePair convertiblePair, boolean isReading, boolean isWriting) {
		Assert.notNull(convertiblePair, "ConvertiblePair must not be null!");

		this.convertiblePair = convertiblePair;
		reading = isReading;
		writing = isWriting;
	}

	/**
	 * Creates a new {@link ConverterRegistration} from the given source and target type and read/write flags.
	 *
	 * @param source the source type to be converted from, must not be {@literal null}.
	 * @param target the target type to be converted to, must not be {@literal null}.
	 * @param isReading whether to force to consider the converter for reading.
	 * @param isWriting whether to force to consider the converter for writing.
	 */
	public ConverterRegistration(Class<?> source, Class<?> target, boolean isReading, boolean isWriting) {
		this(new ConvertiblePair(source, target), isReading, isWriting);
	}

	/**
	 * Returns whether the given type is a type that Couchbase can handle basically.
	 *
	 * @param type
	 * @return
	 */
	private static boolean isCouchbaseBasicType(Class<?> type) {
		return CouchbaseSimpleTypes.JSON_TYPES.isSimpleType(type);
	}

	/**
	 * Returns whether the converter shall be used for writing.
	 *
	 * @return
	 */
	public boolean isWriting() {
		return writing == true || (!reading && isSimpleTargetType());
	}

	/**
	 * Returns whether the converter shall be used for reading.
	 *
	 * @return
	 */
	public boolean isReading() {
		return reading == true || (!writing && isSimpleSourceType());
	}

	/**
	 * Returns the actual conversion pair.
	 *
	 * @return
	 */
	public ConvertiblePair getConvertiblePair() {
		return convertiblePair;
	}

	/**
	 * Returns whether the source type is a Couchbase simple one.
	 *
	 * @return
	 */
	public boolean isSimpleSourceType() {
		return isCouchbaseBasicType(convertiblePair.getSourceType());
	}

	/**
	 * Returns whether the target type is a Couchbase simple one.
	 *
	 * @return
	 */
	public boolean isSimpleTargetType() {
		return isCouchbaseBasicType(convertiblePair.getTargetType());
	}
}
