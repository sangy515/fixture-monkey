/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.fixturemonkey.api.property;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.matcher.Matcher;
import com.navercorp.fixturemonkey.api.type.TypeCache;
import com.navercorp.fixturemonkey.api.type.Types;

/**
 * Generates JavaBeans properties which have getter method.
 */
@API(since = "0.5.3", status = Status.MAINTAINED)
public final class JavaBeansPropertyGenerator implements PropertyGenerator {
	private final Predicate<PropertyDescriptor> propertyDescriptorPredicate;
	private final Matcher matcher;

	public JavaBeansPropertyGenerator(
		Predicate<PropertyDescriptor> propertyDescriptorPredicate,
		Matcher matcher
	) {
		this.propertyDescriptorPredicate = propertyDescriptorPredicate;
		this.matcher = matcher;
	}

	@Override
	public List<Property> generateChildProperties(AnnotatedType annotatedType) {
		return TypeCache.getPropertyDescriptorsByPropertyName(Types.getActualType(annotatedType.getType())).values()
			.stream()
			.filter(it -> it.getReadMethod() != null)
			.filter(propertyDescriptorPredicate)
			.map(PropertyDescriptorProperty::new)
			.filter(matcher::match)
			.collect(Collectors.toList());
	}
}
