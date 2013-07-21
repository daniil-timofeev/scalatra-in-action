
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.LifeCycle
import org.scalatra.ScalatraBase._
import org.scalatra.servlet.ScalatraListener

import ScalatraListener.LifeCycleKey

object ScalatraLauncher extends App {

  // load config using typesafe config
  val config = AppConfig(ConfigFactory.load())
  val hostname  : String         = config.hostname
  val port      : Int            = config.port
  val lifecycle : Option[String] = config.lifecycle

  // alternatively using system properties
  // val port = sys.props.get(PortKey).map(_.toInt).getOrElse(8080)
  // val host = sys.props.get(HostNameKey).getOrElse("localhost")
  // val lifecycle = sys.props.get(LifeCycleKey)


  // setup server
  val server = new Server
  server.setGracefulShutdown(5000)
  server.setSendServerVersion(false)
  server.setSendDateHeader(true)
  server.setStopAtShutdown(true)

  val connector = new SelectChannelConnector
  connector.setHost(hostname)
  connector.setPort(port)
  connector.setMaxIdleTime(90000)
  server.addConnector(connector)

  val context = new WebAppContext
  context.setContextPath("/")
  context.setResourceBase("webapp")

  lifecycle.foreach(l => context.setInitParameter(LifeCycleKey, l))
  context.setEventListeners(Array(new ScalatraListener))

  // default servlet: context.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(context)

  server.start
  server.join

}
