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

package fr.auroden.pool.example;

import fr.auroden.pool.Allocator;
import fr.auroden.pool.ObjectPool;

/**
 * Simple ObjectPool example.
 */
class Example {
	enum Type {
		A,
		B
	}

	static class MyObject {
		String val;

		MyObject(String val) {
			this.val = val;
		}

		@Override
		public String toString() {
			return val;
		}
	}

	static public void main(String[] argv) {
		ObjectPool<Type, MyObject> pool = new ObjectPool();

		// how to build a new MyObject associated with the type A.
		Allocator<MyObject> typeA_allocator = () -> new MyObject("A");

		// how to build a new MyObject associated with the type B.
		Allocator<MyObject> typeB_allocator = () -> new MyObject("B");

		MyObject a1 = pool.checkOut(Type.A, typeA_allocator);
		MyObject a2 = pool.checkOut(Type.A, typeA_allocator);
		System.out.println("a1 = a2 : " + (a1 == a2)); // should be false.

		MyObject b1 = pool.checkOut(Type.B, typeB_allocator);
		pool.checkIn(Type.B, b1);
		MyObject b2 = pool.checkOut(Type.B, typeB_allocator);
		System.out.println("b1 = b2 : " + (b1 == b2)); // should be true.

		// don't forget to checkIn every objects to reuse later.
		pool.checkIn(Type.A, a1);
		pool.checkIn(Type.A, a2);
		pool.checkIn(Type.B, b2);
	}
}
