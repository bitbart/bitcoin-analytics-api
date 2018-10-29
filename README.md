# BlockAPI: Blockchain analytics API
A Scala framework for the development of general-purpose analytics on blockchains, maintained by [Livio Pompianu](http://tcs.unica.it/members/livio-pompianu) and [Stefano Lande](http://tcs.unica.it/members/stefano-lande) of the [Blockchain@Unica Lab](http://blockchain.unica.it) at the University of Cagliari.

The framework allows to combine data *within* the blockchain 
with data from the *outside* (e.g. exchange rates and tags). 
The workflow consists in two steps: 
1. construct a view of the blockchain and save it in a database;
2. analyse the view by using the query language of the database.

The blockchains currently supported are Bitcoin, Litecoin, and  Ethereum.
The DBMS currently supported are MongoDB, MySQL, PostgreSQL, and Fuseki.

The library is dicussed in [A general framework for blockchain analytics](https://www.researchgate.net/publication/321415812_A_general_framework_for_blockchain_analytics),
in proceeding of the [SERIAL workshop 2017](https://serial17.ibr.cs.tu-bs.de/).

More material is available in the [project page](http://blockchain.unica.it/projects/blockchain-analytics/analytics.html).

The Scaladoc is uploaded on this repository in the [doc folder](https://github.com/bitbart/blockchain-analytics-api/tree/master/docs).

### Install prerequisites
#### General prerequisites
Prerequisites depends on the target analyses.
Generally speaking the framework needs at least one blockchain client and one DBMS.

1. Blockchain clients:
    * [Bitcoin Core](https://bitcoin.org/en/bitcoin-core/) (extracts data from Bitcoin)
    * [Parity](https://parity.io/) (extracts data from Ethereum)
    * [Litecoin Core](https://litecoin.org/#download) (extracts data from Litecoin)
2. DBMS:
    * [MongoDB](https://www.mongodb.com/what-is-mongodb) (constructs a NoSQL view of the data)
    * [MySQL](https://www.mysql.com/) (constructs a SQL view of the data)
    * [PostgreSQL](https://www.postgresql.org/) (constructs a SQL view of the data)
    * [Apache Jena Fuseki](https://jena.apache.org/documentation/fuseki2/index.html) (constructs a RDF view of the data)
3. Either
    * use an IDE for executing a Scala SBT project (we used [IntelliJ IDEA](https://www.jetbrains.com/idea/)) or
    * use the command line in place of an IDE (Install [SBT](http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Linux.html))

#### Required libraries
This step is required to sucessfully compile the project.
Before building the project, execute the following commands:

##### Bitcoinj 
```bash
git clone https://github.com/bitbart/bitcoinj.git
cd bitcoinj
git checkout segwit
mvn install -DskipTests
cd core
mvn install -DskipTests
```
##### Litecoinj
```bash
git clone https://github.com/litecoinj-unica/litecoinj.git
cd litecoinj
mvn install -DskipTests
cd core
mvn install -DskipTests
```

#### Analysis dependent prerequisites 

##### ICO Analytics
This instructions are needed only for performing analyses on ICO on Ethereum.

In order to use the `ICO` class (created to retrieve ICOs data) you must require the respective API keys:
1. [EtherScan](https://etherscan.io) 
    * It is sufficient to sign up and then go [here](https://etherscan.io/myapikey)
2. [ICOBench](https://icobench.com/)
    * [Sign Up](https://icobench.com/register), then require an API key [here](https://icobench.com/developers) 
3. [Ethplorer](https://github.com/EverexIO/Ethplorer/wiki/Ethplorer-API)
    * There is a default, free key: `freekey`. 
      If you need more data or highload of service, you can to get personal API key. 

After doing these steps, do the following:
1. Copy `EtherScan` key into `EtherScanAPI.apiKey` attribute
2. Copy `ICOBench` private key and public key into `ICOBenchAPI.privateKey` and `ICOBenchAPI.publicKey` attributes respectively
3. Copy `Ethplorer` key into `EthplorerAPI.apiKey` attribute

### Install blockchain analytics API
1. Execute the blockchain client in order to obtain a local copy of the target blockchain (this process may take several hours)
2. Enable the client RPC calls.
    * In the Bitcoin Core case
        ```bash
        bitcoind -datadir=path/to/blockchain -server -rpcuser=user -rpcpassword=password -rpcserialversion=0
        ```
3. Run a localhost instance of the DBMS on the default port.
4. Clone this repository.
5. From your IntelliJ welcome screen: select "Import Project" and open the [build.sbt](https://github.com/bitbart/bitcoin-analytics-api/blob/master/build.sbt) file from your repository directory.

### Running 
1. Execute of one of the available [examples](https://github.com/bitbart/bitcoin-analytics-api/tree/master/src/main/scala/tcs/examples). Either 
    * open the file from your IDE and select run or 
    * use the command line: from the root directory of the project, execute
        ```bash
        sbt "runMain tcs.examples.ClassName"
        ```

Our framework will build the selected database. Then you can query it for performing your analysis.
For each available Scala script, we provide some default [queries along with the resulting csv files](https://github.com/bitbart/blockchain-analytics-api/tree/master/queries).

### Acknowledgments
The authors thank the following developers of the Department of Mathematics and Computer Science of the University of Cagliari for their valuable contributions. 

Members of Blockchain@Unica lab:
   * [Nicola Atzei](http://tcs.unica.it/members/nicola-atzei), [Sergio Serusi](https://sites.google.com/site/tcsunica/members/sergio-serusi) - Improvements on software architecture and testing
   
Interns at Blockchain@Unica lab:
   * [Giulia Argiolas](https://github.com/giuliamorgen) - Introduction of Litecoin blockchain
   * [Daniele Stefano Ferru](https://github.com/ferruvich) - Introduction of Ethereum blockchain, analysis of ICOs
   * [Davide Curcio](https://github.com/davidecurcio) - Fuseki Database
   * [Antonio Sanna](https://github.com/TonioMeepo) - Bitcoin mempool
   
Students of the Cybersecurity course:
   * [Andrea Corriga](https://github.com/AsoStrife), [Omar Desogus](https://github.com/cedoor), [Enrico Podda](https://github.com/EnricoPodda) - Empty blocks on Ethereum
   * [Giacomo Corrias](https://www.linkedin.com/in/giacomo-corrias-a730b7160/), [Francesco Pisu](https://www.linkedin.com/in/francesco-pisu-b07a3b13a/) - Empty blocks on Bitcoin
   * [Giancarlo Lelli](https://www.linkedin.com/in/giancarlolelli/) - Bitcoin pools
   * [Fabrizio Chelo](https://www.linkedin.com/in/fabrizio-chelo-37005735), [Hicham Lafhouli](https://github.com/H1cham), [Antonello Meloni](https://github.com/infovillasimius) - Signature hash types
   * [Giovanni Laerte Frongia](https://www.linkedin.com/in/giovanni-laerte-frongia-3899b2107/), [Luca Pitzalis](https://github.com/pizza1994) - Verified contracts from Etherscan.io
   * [Filippo Andrea Fanni](https://www.linkedin.com/in/filippo-andrea-fanni/), [Martina Senis](), [Alessandro Tola](https://www.linkedin.com/in/alessandro-tola-54048238/) - Ethereum pools 
   * [Riccardo Mulas](https://github.com/riccardomulas) - Non-standard transactions
   * [Fabio Carta](https://www.linkedin.com/in/fabio-carta-45781196/), [Francesca Malloci](https://www.linkedin.com/in/francescamalloci/), [Flavia Murru](https://www.linkedin.com/in/flavia-murru-269459159) - Duplicate Ethereum contracts
   * [Carlo Cabras](https://www.linkedin.com/in/carlocabras21/), [Federico Maria Cau](https://www.linkedin.com/in/federico-maria-cau-9178b114a/), [Mattia Samuel Mancosu](https://www.linkedin.com/in/mattia-samuel-mancosu/) - Balances of Ethereum addresses
   * [Andrea Demontis](https://github.com/AndreaDemontis), [Stefano Dessì](https://github.com/StefanoDessi) - Balances of Bitcoin addresses
   * [Federica Gerina](https://www.linkedin.com/in/federica-gerina-961765132/), [Silvia Maria Massa](https://www.linkedin.com/in/silvia-maria-massa-2072a6163/), [Francesca Moi](https://www.linkedin.com/in/francesca-moi-3582b9164/) - State of the DAPPS
   * [Giuseppina Lai](https://www.linkedin.com/in/giusy-lai-ba8175b1/), [Federica Muceli](), [Federico Spiga]() - UTXO set
   * [Alessandro Bonini](https://www.linkedin.com/in/alessandro-bonini/), [Alberto Pes](https://www.linkedin.com/in/alberto-pes-32478070/), [Maurizio Porcu](https://www.linkedin.com/in/maurizioporcu/) - Blockchain.info tags
   * [Stefano Raimondo Chessa](https://github.com/StefanoChessa), [Marco Guria](https://github.com/marcoguria), [Alessio Manai](https://www.linkedin.com/in/alessiomanai/), [Alessio Speroni](https://www.linkedin.com/in/alessiosperoni) - Ethereum tokens
   * [Paolo Orrù](https://www.linkedin.com/in/paoloorru), [Elisa Pau](https://www.linkedin.com/in/elisa-pau-52699b107) - Cross validation Ethereum
   * [Stefano Danese](https://www.linkedin.com/in/stefano-danese-3422585b), [Giuseppe Bellisano](https://www.linkedin.com/in/bellisano), [Emanuela Ripoli](https://www.linkedin.com/in/emanuela-ripoli-291891166) - Bitcoin transactions ip-geolocalization
   * [Riccardo Casu](https://www.linkedin.com/in/riccardo-casu-79870a166/), [Carlo Cuccu](https://www.linkedin.com/in/carlo-cuccu-213939165), [Vittoria Frau](https://www.linkedin.com/in/vittoria-frau) - Bitcoin exchange rates
   * [Federico Medda](https://bitbucket.org/federicomedda/), [Alberto Musa](https://github.com/AlbertoMusa) - Cross validation Bitcoin
   * [Simone Balloccu](), [Andrea Bellanti](), [Federica Stocchino]() - Transaction chains
   * [Daniele Sanna](https://github.com/danielesanna), [Giovanni Usai](https://github.com/giovanniusai) - Cross validation Litecoin
   * [Soukaina Aassouss](), [Reda Belaiche](), [Khadija Zine](https://github.com/khadijazine) - Counterparty Tokens 
