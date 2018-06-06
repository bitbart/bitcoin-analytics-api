package tcs.examples.bitcoin.fuseki

object TestAddresses {
  def main(args: Array[String]): Unit = {

    val addresses = new AddressesGraph("b5f6e3b217fa7f6d58081b5d2a9a6607eebd889ed2c470191b2a45e0dcb98eb0", 500000, 150000l, 490480l)
    addresses.delete()
    addresses.startAddressesGraph(Forward)
  }
}