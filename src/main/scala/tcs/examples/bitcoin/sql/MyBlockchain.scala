package tcs.examples.bitcoin.sql

import scalikejdbc._
import tcs.blockchain.BlockchainLib
import tcs.blockchain.bitcoin.{BitcoinSettings, MainNet}
import tcs.db.sql.Table
import tcs.db.{DatabaseSettings, MySQL}
import tcs.utils.converter.DateConverter.convertDate


/**
  * Created by Livio on 14/06/2017.
  */
object MyBlockchain{
  def main(args: Array[String]): Unit ={

    val blockchain = BlockchainLib.getBitcoinBlockchain(new BitcoinSettings("alice", "8ak1gI25KFTvjovL3gAM967mies3E=", "8332", MainNet))
    val mySQL = new DatabaseSettings("myblockchain", MySQL, "alice", "Djanni74!")

    val startTime = System.currentTimeMillis()/1000

    val txTable = new Table(sql"""
      create table if not exists transaction(
        txid int(10) unsigned auto_increment not null primary key,
        transactionHash varchar(256) not null,
        blockHash varchar(256) not null,
        timestamp TIMESTAMP not null
      ) """,
      sql"""insert into transaction(transactionHash, blockHash, timestamp) values (?, ?, ?)""",
      mySQL)

    val inTable = new Table(sql"""
      create table if not exists input(
        id int(10) unsigned auto_increment not null primary key,
        transactionHash varchar(256) not null,
        inputScript text not null
      ) """,
      sql"""insert into input(transactionHash, inputScript) values (?, ?)""",
      mySQL)

    val outTable = new Table(sql"""
      create table if not exists output(
        id int(10) unsigned auto_increment not null primary key,
        transactionHash varchar(256) not null,
        outputScript text not null
      ) """,
      sql"""insert into output(transactionHash, outputScript) values (?, ?)""",
      mySQL)


    blockchain.end(473100).foreach(block => {
      block.txs.foreach(tx => {

        txTable.insert(Seq(tx.hash.toString, block.hash.toString, convertDate(block.date)))

        tx.inputs.foreach(in => { inTable.insert(Seq(tx.hash.toString, in.inScript.toString)) })

        tx.outputs.foreach(out => { outTable.insert(Seq(tx.hash.toString, out.outScript.toString)) })
      })

      if (block.height % 10000 == 0)
        println(block.height)
    })

    txTable.close
    inTable.close
    outTable.close

    val totalTime = System.currentTimeMillis() / 1000 - startTime

    println("Total time: " + totalTime)
    println("Computational time: " + (totalTime - Table.getWriteTime))
    println("Database time: " + Table.getWriteTime)


  }
}