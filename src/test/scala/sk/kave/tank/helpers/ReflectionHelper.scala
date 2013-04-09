/*
 * Copyright
 *  viliam.kois@gmail.com   Kois Viliam
 *  ikvaso@gmail.com        Kvasnicka Igor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package sk.kave.tank.helpers

import scala.Predef._

/**
 * @author Igo
 */
trait ReflectionHelper {

  implicit def reflector(ref: AnyRef) = new {

    def getPrivateField[T](name: String, returnType: Class[T]): T = {
      val methodOption = ref.getClass.getMethods.find(m => m.getName == name || m.getName.endsWith("$$" + name))//inhereted fields/methods constains $$ in their name
      require(methodOption.isDefined, "Could not find field named: " + name)

      val method = methodOption.get
      method.setAccessible(true)
      val result = method.invoke(ref)
      require(result.isInstanceOf[T])
      result.asInstanceOf[T]
    }

    def setPrivateValue(name: String, args: Any) = {
      val methodOption = ref.getClass.getMethods.find(m => m.getName == name + "_$eq" || m.getName.endsWith("$$" + name + "_$eq"))
      require(methodOption.isDefined, "Could not find field named: " + name)

      val method = methodOption.get
      method.setAccessible(true)
      method.invoke(ref, args.asInstanceOf[AnyRef])
    }

    def callPrivateMethod(name: String, args: Any) {
      val methodOption = ref.getClass.getMethods.find(m => m.getName == name || m.getName.endsWith("$$" + name))
      require(methodOption.isDefined, "Could not find method named: " + name)

      val method = methodOption.get
      method.setAccessible(true)
      method.invoke(ref, args.asInstanceOf[AnyRef])
    }
  }
}
