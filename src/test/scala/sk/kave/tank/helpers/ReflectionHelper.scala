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
