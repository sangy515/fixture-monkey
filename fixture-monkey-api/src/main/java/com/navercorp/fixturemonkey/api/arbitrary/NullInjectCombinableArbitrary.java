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

package com.navercorp.fixturemonkey.api.arbitrary;

import java.lang.reflect.Proxy;

import javax.annotation.Nullable;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.random.Randoms;

/**
 * It would generate an object may be {@code null} with a {@code nullProbability}% chance.
 */
@API(since = "0.5.0", status = Status.MAINTAINED)
final class NullInjectCombinableArbitrary implements CombinableArbitrary {
	private final CombinableArbitrary combinableArbitrary;
	private final double nullProbability;

	NullInjectCombinableArbitrary(CombinableArbitrary combinableArbitrary, double nullProbability) {
		this.combinableArbitrary = combinableArbitrary;
		this.nullProbability = nullProbability;
	}

	@Override
	public Object combined() {
		Object combined = combinableArbitrary.combined();
		if (combined instanceof Proxy) {
			return combined;
		}

		return injectNull(combined);
	}

	@Override
	public Object rawValue() {
		Object rawValue = combinableArbitrary.rawValue();
		if (rawValue instanceof Proxy) {
			return rawValue;
		}

		return injectNull(rawValue);
	}

	@Override
	public void clear() {
		combinableArbitrary.clear();
	}

	@Override
	public boolean fixed() {
		return combinableArbitrary.fixed();
	}

	@Nullable
	private Object injectNull(Object object) {
		int frequencyNull = (int)Math.round(nullProbability * 1000);
		if (frequencyNull <= 0) {
			return object;
		}
		int currentSeed = Randoms.nextInt(1000);
		return currentSeed < frequencyNull ? null : object;
	}
}
