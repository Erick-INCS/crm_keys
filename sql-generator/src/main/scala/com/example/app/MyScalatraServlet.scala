package com.example.app

import org.scalatra._
import scala.io.Source
import upickle.default.{ReadWriter => RW, macroRW}
import upickle.default._
import java.sql.{Connection,DriverManager}

case class Estacion(
  Estaciones:Int, 
  Empresa:String, 
  Professional:Int, 
  Standard:Int, 
  PyME:Int, 
  valid:Int, 
  opt1:Int, 
  opt2:Int, 
  opt3:Int, 
  opt6:Int, 
  opt7:Int, 
  opt8:Int, 
  opt9:Int, 
  opt10:Int, 
  opt11:Int, 
  opt12:Int, 
  opt13:Int
)
object Estacion{
  implicit val rw: RW[Estacion] = macroRW
}


class MyScalatraServlet extends ScalatraServlet {

  val dataUrl = "http://127.0.0.1:5000/accounts"
  val ids = Source.fromFile("/HDD/Code/text/keys.txt").getLines.toSeq
  val dbUrl = "jdbc:mariadb://172.17.0.2:3306/sugarcrm"
  val dbUser = "root"
  val dbPass = "pass"
  var connection:Connection = _
  val modulos = """Modulo MiEI
Modulo I-COM
Prorrateo y Descarga de Inventario
Consulta de Data de Stage
Comparacion de Data Stage
Prorrateo y Descarga de desperdicios
Simulador Anexo 31
Digitalizacion de Documentos""".split("\n")

  def execSQL(sql:String):Boolean = {
    // connect to the database named "mysql" on port 8889 of localhost
    val driver = "org.mariadb.jdbc.Driver"
    var rs = false

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(dbUrl, dbUser, dbPass)
      rs = connection.createStatement.execute(sql)
    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close
    rs
  }

  get("/") {
    val data = read[Seq[Estacion]](Source.fromURL(dataUrl).mkString)
    val sysTypes = data.map(rw=>Seq(("Professional", Seq(rw.Professional, rw.opt3).max), ("Standard", Seq(rw.Standard, rw.opt2).max), ("PyME", Seq(rw.PyME, rw.opt1).max)).maxBy(_._2)._1)

    if(data.length == 0) "No data."

    // Build the sql
    // Selled sistems
    var str = data.zipWithIndex.map{case (rw, i) => {
      s"('${ids(i)}', <id>, 'GRSA', 'SApp', '${sysTypes(i)}', NOW()),"
    }}.mkString

    val sql1 = "INSERT INTO GR_SistemasVendidos(SV_NSerie, SV_IdAccount, SV_Sistema, SV_Version, SV_TipoSistema, SV_FechaInstalacion, ) VALUES" + str.take(str.length-2) + ";"

    // Modules
    str = data.zipWithIndex.map{case (rw, i) => {
      
      val strSQL = Seq(rw.opt6, rw.opt7, rw.opt8, rw.opt9, rw.opt10, rw.opt11, rw.opt12, rw.opt13).
        zipWithIndex.map{case (n, ii) => if (n > 0) 
          s"('${ids(i)}', <id>, 'GRSA', 'SApp', '${sysTypes(i)}', '${modulos(ii)}', -1, 'Este registro fue generado automaticamente', ${rw.Estaciones})," else ""}.
        mkString
      if (strSQL.length > 0) "INSTERT INTO GR_ModulosComprados(MC_NSerieSistema ,MC_idAccount, MC_Sistema, MC_Version, MC_TipoSistema, MC_Modulo, MC_CostoPagado, MC_Nota, MC_CantidadUsuarios) values " +strSQL.take(strSQL.length-1) + ";"
      else ""
    }}.mkString


    if (execSQL(sql1 + str)) {
      "ok"
    } else {
      println("\n\n\n\n")
      println(sql1 + str)
    }
  }

}
