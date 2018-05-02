package tcs.examples.bitcoin.sql

import scalikejdbc._
import tcs.blockchain.BlockchainLib
import tcs.blockchain.bitcoin.{BitcoinSettings, MainNet}
import tcs.utils.DateConverter.convertDate
import tcs.custom.bitcoin.Exchange
import tcs.db.{DatabaseSettings, MySQL}
import tcs.db.sql.Table
import tcs.utils.DateConverter
import java.util.Calendar
import java.io._
import scala.collection.mutable.ListBuffer


/**
  * Created by Antonio Sanna on 27/04/2018.
  */
object TxFromMemPool
{
  


  def main(args: Array[String]): Unit =
  {

    val blockchain = BlockchainLib.getBitcoinBlockchain(new BitcoinSettings("bitcoin", "L4mbWnzC11BNrmTK","https", "443","co2.unica.it" ,"bitcoin-mainnet", MainNet))
    val mySQL = new DatabaseSettings("mempool", MySQL, "root", "")

    val startTime = System.currentTimeMillis()/1000
    var hashList= new ListBuffer[String]()


    val txTable = new Table(
      sql"""
      create table if not exists transaction(
        id serial not null primary key,
        hash varchar(256) not null,
        txdate TIMESTAMP not null,
        inputsSum bigint,
        outputsSum bigint,
        fee bigint,
        error Boolean not null
    )""",
      sql"""insert into transaction (hash, txdate,inputsSum,outputsSum, fee, error) values(?,?,?,?,?,?)""",
      mySQL)

    for(hash <- blockchain.getMemPool())
    {
        hashList += hash
        try
        {
          val tx = blockchain.getTransaction(hash)
          txTable.insert(Seq(
            tx.hash.toString,
            convertDate(Calendar.getInstance().getTime()),
            tx.getInputsSum(),
            tx.getOutputsSum(),
            null,
            false))
        } 
        catch
        {
          case e: RuntimeException => 
                    txTable.insert(Seq(
                        hash,
                        convertDate(Calendar.getInstance().getTime()),
                        null,
                        null,
                        null,
                        true))
                case _: Throwable => println("Got some other kind of exception")
        }
    }

    do{
        for(hash <- blockchain.getMemPool())
        {
            if(!(hashList contains hash))
            {
                hashList += hash
                try
                {
                val tx = blockchain.getTransaction(hash)
                txTable.insert(Seq(
                    hash,
                    convertDate(Calendar.getInstance().getTime()),
                    tx.getInputsSum(),
                    tx.getOutputsSum(),
                    null,
                    false))
                } 
                catch
                {
                case e: RuntimeException => 
                    txTable.insert(Seq(
                        hash,
                        convertDate(Calendar.getInstance().getTime()),
                        null,
                        null,
                        null,
                        true))
                case _: Throwable => println("Got some other kind of exception")
                }
            }
        }
        println(hashList.length)
    }while((System.currentTimeMillis() / 1000)/60<=10)
    //pw.close
    txTable.close
  }
}