package com.baidu.scala.demo.bigdata

import scala.io.Source
import java.io.File

/**
 * test for git repository
 * @author wangyuxue
 */
object BigDecimalTest extends App{
    
    val content = Source.fromFile(new File("/Users/odile/Desktop/test"), "utf-8").mkString
    val array = content.replace("),", ");").split(";")
    //transform data
    val tmpArray = array.par.map((x) =>{
        x.replace("(", "").replace(")", "").split(",")
    })
    
    //sum
    println("sum")
    for (i <- 1 to 5) {
        val begin = System.currentTimeMillis();
        println(tmpArray.map(x => BigDecimal.exact(x(6))).par.sum)
        println(s"sum cal cost ${System.currentTimeMillis() - begin}ms")
    }
    
    //filter count
    val filterValue = tmpArray(0)(0)
    val anotherValue = tmpArray(0)(1)
    println("filter")
    for(i <- 1 to 5){
        val begin = System.currentTimeMillis();
        println(tmpArray
                    .filter(x => x(0).equals(filterValue) && x(1).equals(anotherValue))
                    .map(x => BigDecimal.exact(x(6))).par.sum)
        println(s"filter count cost ${System.currentTimeMillis() - begin}ms")
    }
    
    //group by
    println("group by")
    for(i <- 1 to 5) {
        val begin = System.currentTimeMillis();
        val rs = for{e <- tmpArray.par.groupBy(x => x(1))
        }yield (e._1, (e._2.par.map(y => BigDecimal.exact(y(6))).sum,
            e._2.par.map(y => BigDecimal.exact(y(5))).sum))
        println(s"groupby count cost ${System.currentTimeMillis() - begin}ms")
//        rs.foreach(x => println(s"rs : ${x._1} = ${x._2}"))
//        println(rs.map(x => x._2._1 ).sum)
//        println("===============")
    }
}