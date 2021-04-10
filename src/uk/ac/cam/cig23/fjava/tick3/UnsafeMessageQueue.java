/*
 * Copyright 2020 Andrew Rice <acr31@cam.ac.uk>, Alastair Beresford <arb33@cam.ac.uk>, C.I. Griffiths
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.cam.cig23.fjava.tick3;

public class UnsafeMessageQueue<T> implements MessageQueue<T> {
  private static class Link<L> {
    L val;
    Link<L> next;

    Link(L val) {
      this.val = val;
      this.next = null;
    }
  }

  private Link<T> first = null;
  private Link<T> last = null;

  public void put(T val) {

    Link newLast = new Link(val);
    if(first==null) {
      first = newLast;
      last = newLast;
    } else {
      last.next = newLast;
      last = newLast;
    }
  }

  public T take() {
    while (first == null) { // use a loop to block thread until data is available
      try {
        Thread.sleep(100);
      } catch (InterruptedException ie) {
        // Ignored exception

        //This exception is thrown when the thread is interrupted. As sleep is a blocking method
        // it throws this exception. This will probably be an
        //interrupt from the writer thread (if we look at this as an isolated program then
        // it will be but it could be other processes on the computer interupting). By catching the
        // interupt we are stopping it from actually happening so we should probably do anything this thread
        //needs to finish safely (nothing as it is just sleeping). We should manually interupt the thread with
        //Thread().currentThread().interrupt(). We also might want to break the while loop because once
        //the thread has control again there will probably be something in the queue to read.
      }
    }

    T val = first.val;
    first = first.next;
    return val;
  }
}
