///////////////////////////////////////////////////////////////////////////////////
// The MIT License (MIT)
//
// Copyright (c) 2016 Benoit CORTIER
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
///////////////////////////////////////////////////////////////////////////////////

package fr.auroden.pool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic object pool.
 *
 * @param <K> keys type.
 * @param <T> objects type.
 */
public class ObjectPool<K, T> {
	private Map<K, Deque<T>> unlocked = new HashMap<>();

	/**
	 * Returns a T object associated with the key.
	 * If there is no object available, a new one is created with the allocator.
	 *
	 * @param key the key associated with the T object.
	 * @param alloc the T object allocator.
	 * @return a usable T object.
	 */
    public synchronized T checkOut(K key, Allocator<T> alloc) {
		if (!this.unlocked.containsKey(key)) {
			this.unlocked.put(key, new ArrayDeque<>());
		}

		Deque<T> deq = this.unlocked.get(key);
		if (deq.isEmpty()) {
			return alloc.allocate();
		}
		return deq.getFirst();
    }

	/**
	 * Should be called for each checkOut.
	 *
	 * @param key the key associated with the T object.
	 * @param obj the object to be reused later.
	 */
	public synchronized void checkIn(K key, T obj) {
		this.unlocked.get(key).addLast(obj);
	}
}

