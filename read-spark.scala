val data = spark.read.json("/HDD/Code/Python/Scrapy/crm_keys/sv.json")
val cols = List(("Empresa", "SV_Empresa"))
val table = "GR_SistemasVendidos"

val icols = cols.map(c=>(data.columns.indexOf(c._1), c._2))

val sqls =
  data.map(rw => {
    var sql = ""
    for (c <- icols) {
      sql += c._2 + " = '" + rw(c._1) + "', "
    }
    sql.take(sql.length-1)
  })

for (s <- sqls) println(s)
