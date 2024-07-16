import com.typesafe.config.{Config, ConfigFactory}
import io.getquill.*

package object db {
  lazy val ctx = PostgresJdbcContext(SnakeCase, "db")
}
