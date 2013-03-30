package sk.kave.tank.helpers

/**
 * @author Igo
 */
trait ReflectionHelper {
  implicit def reflector(ref: AnyRef) = new {
    def getPrivateValue(name: String): Any = {
      val method = ref.getClass.getMethods.find(_.getName == name).get
      method.setAccessible(true)
      method.invoke(ref)
    }

    def setPrivateValue(name: String, args: Any) = {
      val method = ref.getClass.getMethods.find(_.getName == name + "_$eq").get
      method.setAccessible(true)
      method.invoke(ref, args.asInstanceOf[AnyRef])
    }

    def callPrivateMethod(name: String, args: Any){
      val method = ref.getClass.getMethods.find(_.getName == name).get
      method.setAccessible(true)
      method.invoke(ref, args.asInstanceOf[AnyRef])
    }
  }
}
