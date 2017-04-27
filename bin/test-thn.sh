#!/bin/bash

# application jar file
JAR_FILE=exp-rya-bloomfilter-0.0.1-SNAPSHOT-shaded.jar

# accumulo info <instance name>:<user name>:<password>
ACCUMULO_INFO="spear_instance:SPEAR:spear"

# zookeepers <host>:<port>
ZK_INFO="zoo1.cluster.sparta.com:2181,zoo2.cluster.sparta.com:2181,zoo3.cluster.sparta.com:2181,zoo4.cluster.sparta.com:2181,zoo5.cluster.sparta.com:2181"

# random <seed>:<start>:<end>:<prefix>
RANDOM_INFO="8:0:1000000000:test"
RANDOM_INFO2="10:0:1000000000:test2"

# table prefiex
NBF_PREFIX=thn_nbf
WBF_PREFIX=thn_wbf

# tables with no bloome filter enabled
NBF_1K="$NBF_PREFIX"_1k_
NBF_100K="$NBF_PREFIX"_100k_

NBF_1M="$NBF_PREFIX"_1m_
NBF_10M="$NBF_PREFIX"_10m_
NBF_100M="$NBF_PREFIX"_100m_

NBF_1B="$NBF_PREFIX"_1b_

# tables with bloom filter enabled
WBF_1K="$WBF_PREFIX"_1k_
WBF_100K="$WBF_PREFIX"_100k_

WBF_1M="$WBF_PREFIX"_1m_
WBF_10M="$WBF_PREFIX"_10m_
WBF_100M="$WBF_PREFIX"_100m_

WBF_1B="$WBF_PREFIX"_1b_

case "$1" in
  'rya.load.random')
    java -jar $JAR_FILE rya load.random $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO $3
    ;;

  'load.random.nbf.k')
    $0 rya.load.random $NBF_1K 1000
    $0 rya.load.random $NBF_100K 100000
    ;;

  'load.random.nbf.m')
    $0 rya.load.random $NBF_1M 1000000
    $0 rya.load.random $NBF_10M 10000000
    $0 rya.load.random $NBF_100M 100000000
    ;;

  'load.random.nbf.b')
    $0 rya.load.random $NBF_1B 1000000000
    ;;

  'load.random.wbf.k')
    $0 rya.load.random $WBF_1K 1000
    $0 rya.load.random $WBF_100K 100000
    ;;

  'load.random.wbf.m')
    $0 rya.load.random $WBF_1M 1000000
    $0 rya.load.random $WBF_10M 10000000
    $0 rya.load.random $WBF_100M 100000000
    ;;

  'load.random.wbf.b')
    $0 rya.load.random $WBF_1B 1000000000
    ;;

  'load.random.m')
    $0 load.random.nbf.m
    $0 load.random.wbf.m
    ;;

  'load.random.b')
    $0 load.random.nbf.b
    $0 load.random.wbf.b
    ;;

  'query.random')
    java -jar $JAR_FILE $2 query.random $ACCUMULO_INFO $ZK_INFO $3 $RANDOM_INFO $4
    ;;

  'query.random.nh')
    java -jar $JAR_FILE $2 query.random $ACCUMULO_INFO $ZK_INFO $3 $RANDOM_INFO2 $4
    ;;

  'query.random.values')
    # <query type> <table prefix> <number of values> <number of iterations>
    java -jar $JAR_FILE $2 query.random.values $ACCUMULO_INFO $ZK_INFO $3 $RANDOM_INFO $4 $5
    ;;

  'rya.query.random.values')
    # <table prefix> <number of values> <number of iterations>
    $0 query.random.values rya $2 $3 $4
    ;;

  'accumulo.query.random.values')
    # <table prefix> <number of values> <number of iterations>
    $0 query.random.values accumulo $2 $3 $4
    ;;

  # no hit case
  'query.random.values.nh')
    java -jar $JAR_FILE $2 query.random.values $ACCUMULO_INFO $ZK_INFO $3 $RANDOM_INFO2 $4 $5
    ;;

  'rya.query.random.values.nh')
    # <table prefix> <number of values> <number of iterations>
    $0 query.random.values.nh rya $2 $3 $4
    ;;

  'accumulo.query.random.values.nh')
    # <table prefix> <number of values> <number of iterations>
    $0 query.random.values.nh accumulo $2 $3 $4
    ;;

  'query.random.values.ratio')
    java -jar $JAR_FILE $2 query.random.values.ratio $ACCUMULO_INFO $ZK_INFO $3 $RANDOM_INFO $RANDOM_INFO2 $4 $5
    ;;

  'rya.query.random.values.ratio')
    # <table prefix> <ratio> <number of iterations>
    $0 query.random.values.ratio rya $2 $3 $4
    ;;

  'accumulo.query.random.values.ratio')
    # <table prefix> <ratio> <number of iterations>
    $0 query.random.values.ratio accumulo $2 $3 $4
    ;;

  'query.random.nbf')
    $0 query.random $NBF_1K   10
    $0 query.random $NBF_100K 10
    $0 query.random $NBF_1M   10
    $0 query.random $NBF_10M  10
    $0 query.random $NBF_100M 10
    ;;

  'query.random.wbf')
    $0 query.random $WBF_1K   10
    $0 query.random $WBF_100K 10
    $0 query.random $WBF_1M   10
    $0 query.random $WBF_10M  10
    $0 query.random $WBF_100M 10
    ;;

  'query.random.2.nbf')
    $0 query.random.2 $NBF_1K   10
    $0 query.random.2 $NBF_100K 10
    $0 query.random.2 $NBF_1M   10
    $0 query.random.2 $NBF_10M  10
    $0 query.random.2 $NBF_100M 10
    ;;

  'query.random.2.wbf')
    $0 query.random.2 $WBF_1K   10
    $0 query.random.2 $WBF_100K 10
    $0 query.random.2 $WBF_1M   10
    $0 query.random.2 $WBF_10M  10
    $0 query.random.2 $WBF_100M 10
    ;;

  # via Rya
  'rya.query.random.values.general')
    $0 rya.query.random.values $2 1 5
    $0 rya.query.random.values $2 10 5
    $0 rya.query.random.values $2 100 5
    $0 rya.query.random.values $2 1000 5
    ;;

  'rya.query.random.values.nbf')
    $0 rya.query.random.values.general $NBF_1K
    $0 rya.query.random.values.general $NBF_100K
    $0 rya.query.random.values.general $NBF_1M
    $0 rya.query.random.values.general $NBF_10M
    $0 rya.query.random.values.general $NBF_100M
    $0 rya.query.random.values.general $NBF_1B
    ;;

  'rya.query.random.values.wbf')
    $0 rya.query.random.values.general $WBF_1K
    $0 rya.query.random.values.general $WBF_100K
    $0 rya.query.random.values.general $WBF_1M
    $0 rya.query.random.values.general $WBF_10M
    $0 rya.query.random.values.general $WBF_100M
    $0 rya.query.random.values.general $WBF_1B
    ;;

  # via accumulo
  'accumulo.query.random.values.general')
    $0 accumulo.query.random.values $2 1 5
    $0 accumulo.query.random.values $2 10 5
    $0 accumulo.query.random.values $2 100 5
    $0 accumulo.query.random.values $2 1000 5
    ;;

  'accumulo.query.random.values.nbf')
    $0 accumulo.query.random.values.general $NBF_1K
    $0 accumulo.query.random.values.general $NBF_100K
    $0 accumulo.query.random.values.general $NBF_1M
    $0 accumulo.query.random.values.general $NBF_10M
    $0 accumulo.query.random.values.general $NBF_100M
    $0 accumulo.query.random.values.general $NBF_1B
    ;;

  'accumulo.query.random.values.wbf')
    $0 accumulo.query.random.values.general $WBF_1K
    $0 accumulo.query.random.values.general $WBF_100K
    $0 accumulo.query.random.values.general $WBF_1M
    $0 accumulo.query.random.values.general $WBF_10M
    $0 accumulo.query.random.values.general $WBF_100M
    $0 accumulo.query.random.values.general $WBF_1B
    ;;

  # via rya
  'rya.query.random.values.nh.general')
    $0 rya.query.random.values.nh $2 1 5
    $0 rya.query.random.values.nh $2 10 5
    $0 rya.query.random.values.nh $2 100 5
    $0 rya.query.random.values.nh $2 1000 5
    ;;

  'rya.query.random.values.nh.nbf')
    $0 rya.query.random.values.nh.general $NBF_1K
    $0 rya.query.random.values.nh.general $NBF_100K
    $0 rya.query.random.values.nh.general $NBF_1M
    $0 rya.query.random.values.nh.general $NBF_10M
    $0 rya.query.random.values.nh.general $NBF_100M
    $0 rya.query.random.values.nh.general $NBF_1B
    ;;

  'rya.query.random.values.nh.wbf')
    $0 rya.query.random.values.nh.general $WBF_1K
    $0 rya.query.random.values.nh.general $WBF_100K
    $0 rya.query.random.values.nh.general $WBF_1M
    $0 rya.query.random.values.nh.general $WBF_10M
    $0 rya.query.random.values.nh.general $WBF_100M
    $0 rya.query.random.values.nh.general $WBF_1B
    ;;
    
  # via accumulo
  'accumulo.query.random.values.nh.general')
    $0 accumulo.query.random.values.nh $2 1 5
    $0 accumulo.query.random.values.nh $2 10 5
    $0 accumulo.query.random.values.nh $2 100 5
    $0 accumulo.query.random.values.nh $2 1000 5
    ;;

  'accumulo.query.random.values.nh.nbf')
    $0 accumulo.query.random.values.nh.general $NBF_1K
    $0 accumulo.query.random.values.nh.general $NBF_100K
    $0 accumulo.query.random.values.nh.general $NBF_1M
    $0 accumulo.query.random.values.nh.general $NBF_10M
    $0 accumulo.query.random.values.nh.general $NBF_100M
    $0 accumulo.query.random.values.nh.general $NBF_1B
    ;;

  'accumulo.query.random.values.nh.wbf')
    $0 accumulo.query.random.values.nh.general $WBF_1K
    $0 accumulo.query.random.values.nh.general $WBF_100K
    $0 accumulo.query.random.values.nh.general $WBF_1M
    $0 accumulo.query.random.values.nh.general $WBF_10M
    $0 accumulo.query.random.values.nh.general $WBF_100M
    $0 accumulo.query.random.values.nh.general $WBF_1B
    ;;

  # via rya
  'rya.query.random.values.ratio.general')
    $0 rya.query.random.values.ratio $2 100:0  5
    $0 rya.query.random.values.ratio $2 99:1   5
    $0 rya.query.random.values.ratio $2 25:75  5
    $0 rya.query.random.values.ratio $2 50:50  5
    $0 rya.query.random.values.ratio $2 75:25  5
    $0 rya.query.random.values.ratio $2 1:99   5
    $0 rya.query.random.values.ratio $2 0:100  5

    $0 rya.query.random.values.ratio $2 1000:0  5
    $0 rya.query.random.values.ratio $2 999:1   5
    $0 rya.query.random.values.ratio $2 250:750 5
    $0 rya.query.random.values.ratio $2 500:500 5
    $0 rya.query.random.values.ratio $2 750:250 5
    $0 rya.query.random.values.ratio $2 1:999   5
    $0 rya.query.random.values.ratio $2 0:1000  5
    ;;

  'rya.query.random.values.ratio.nbf')
    $0 rya.query.random.values.ratio.general $NBF_1K
    $0 rya.query.random.values.ratio.general $NBF_100K
    $0 rya.query.random.values.ratio.general $NBF_1M
    $0 rya.query.random.values.ratio.general $NBF_10M
    $0 rya.query.random.values.ratio.general $NBF_100M
    $0 rya.query.random.values.ratio.general $NBF_1B
    ;;

  'rya.query.random.values.ratio.wbf')
    $0 rya.query.random.values.ratio.general $WBF_1K
    $0 rya.query.random.values.ratio.general $WBF_100K
    $0 rya.query.random.values.ratio.general $WBF_1M
    $0 rya.query.random.values.ratio.general $WBF_10M
    $0 rya.query.random.values.ratio.general $WBF_100M
    $0 rya.query.random.values.ratio.general $WBF_1B
    ;;

  # via accumulo
  'accumulo.query.random.values.ratio.general')
    $0 accumulo.query.random.values.ratio $2 100:0  5
    $0 accumulo.query.random.values.ratio $2 99:1   5
    $0 accumulo.query.random.values.ratio $2 25:75  5
    $0 accumulo.query.random.values.ratio $2 50:50  5
    $0 accumulo.query.random.values.ratio $2 75:25  5
    $0 accumulo.query.random.values.ratio $2 1:99   5
    $0 accumulo.query.random.values.ratio $2 0:100  5

    $0 accumulo.query.random.values.ratio $2 1000:0  5
    $0 accumulo.query.random.values.ratio $2 999:1   5
    $0 accumulo.query.random.values.ratio $2 250:750 5
    $0 accumulo.query.random.values.ratio $2 500:500 5
    $0 accumulo.query.random.values.ratio $2 750:250 5
    $0 accumulo.query.random.values.ratio $2 1:999   5
    $0 accumulo.query.random.values.ratio $2 0:1000  5
    ;;

  'accumulo.query.random.values.ratio.nbf')
    $0 accumulo.query.random.values.ratio.general $NBF_1K
    $0 accumulo.query.random.values.ratio.general $NBF_100K
    $0 accumulo.query.random.values.ratio.general $NBF_1M
    $0 accumulo.query.random.values.ratio.general $NBF_10M
    $0 accumulo.query.random.values.ratio.general $NBF_100M
    $0 accumulo.query.random.values.ratio.general $NBF_1B
    ;;

  'accumulo.query.random.values.ratio.wbf')
    $0 accumulo.query.random.values.ratio.general $WBF_1K
    $0 accumulo.query.random.values.ratio.general $WBF_100K
    $0 accumulo.query.random.values.ratio.general $WBF_1M
    $0 accumulo.query.random.values.ratio.general $WBF_10M
    $0 accumulo.query.random.values.ratio.general $WBF_100M
    $0 accumulo.query.random.values.ratio.general $WBF_1B
    ;;

  # nbf (no bloom filter) 
  'run.nbf.rya')
    $0 rya.query.random.values.nbf
    $0 rya.query.random.values.nh.nbf
    $0 rya.query.random.values.ratio.nbf
    ;;
    
 'run.nbf.accumulo')
    $0 accumulo.query.random.values.nbf
    $0 accumulo.query.random.values.nh.nbf
    $0 accumulo.query.random.values.ratio.nbf
    ;;

  # wbf (with bloom filter)
  'run.wbf.rya')
    $0 rya.query.random.values.wbf
    $0 rya.query.random.values.nh.wbf
    $0 rya.query.random.values.ratio.wbf
    ;;

  'run.wbf.accumulo')
    $0 accumulo.query.random.values.wbf
    $0 accumulo.query.random.values.nh.wbf
    $0 accumulo.query.random.values.ratio.wbf
    ;;

  *)
    java -jar $JAR_FILE
    ;;

esac
exit 0 
