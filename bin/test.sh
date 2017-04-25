#!/bin/bash

#JAR_FILE=rya-bloomfilter-0.0.1-SNAPSHOT-shaded.jar
JAR_FILE=rya-bloomfilter-exp-0.0.1-SNAPSHOT-shaded.jar

ACCUMULO_INFO="spear_instance:SPEAR:spear"
ZK_INFO="zoo1.cluster.sparta.com:2181,zoo2.cluster.sparta.com:2181,zoo3.cluster.sparta.com:2181,zoo4.cluster.sparta.com:2181,zoo5.cluster.sparta.com:2181"

RANDOM_INFO="8:0:1000000000:test"
RANDOM_INFO2="10:0:1000000000:test2"

case "$1" in
  'load.random')
    java -jar $JAR_FILE load.random $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO $3
    ;;

  'load.random.nbf.k')
    $0 load.random nbf_1k_ 1000
    $0 load.random nbf_100k_ 100000
    ;;

  'load.random.nbf.m')
    $0 load.random nbf_1m_ 1000000
    $0 load.random nbf_10m_ 10000000
    $0 load.random nbf_100m_ 100000000
    ;;

  'load.random.nbf.b')
    $0 load.random nbf_1b_ 1000000000
    ;;

  'load.random.wbf.k')
    $0 load.random wbf_1k_ 1000
    $0 load.random wbf_100k_ 100000
    ;;

  'load.random.wbf.m')
    $0 load.random wbf_1m_ 1000000
    $0 load.random wbf_10m_ 10000000
    $0 load.random wbf_100m_ 100000000
    ;;

  'load.random.wbf.b')
    $0 load.random wbf_1b_ 1000000000
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
    java -jar $JAR_FILE query.random $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO $3
    ;;

  'query.random.2')
    java -jar $JAR_FILE query.random $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO2 $3
    ;;

  'query.random.values')
    java -jar $JAR_FILE query.random.values $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO $3 $4
    ;;

  'query.random.values.2')
    java -jar $JAR_FILE query.random.values $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO2 $3 $4
    ;;

  'query.random.values.ratio')
    java -jar $JAR_FILE query.random.values.ratio $ACCUMULO_INFO $ZK_INFO $2 $RANDOM_INFO $RANDOM_INFO2 $3 $4
    ;;

  'query.random.nbf')
    $0 query.random nbf_1k_   10
    $0 query.random nbf_100k_ 10
    $0 query.random nbf_1m_   10
    $0 query.random nbf_10m_  10
    $0 query.random nbf_100m_ 10
    ;;

  'query.random.wbf')
    $0 query.random wbf_1k_   10
    $0 query.random wbf_100k_ 10
    $0 query.random wbf_1m_   10
    $0 query.random wbf_10m_  10
    $0 query.random wbf_100m_ 10
    ;;

  'query.random.2.nbf')
    $0 query.random.2 nbf_1k_   10
    $0 query.random.2 nbf_100k_ 10
    $0 query.random.2 nbf_1m_   10
    $0 query.random.2 nbf_10m_  10
    $0 query.random.2 nbf_100m_ 10
    ;;

  'query.random.2.wbf')
    $0 query.random.2 wbf_1k_   10
    $0 query.random.2 wbf_100k_ 10
    $0 query.random.2 wbf_1m_   10
    $0 query.random.2 wbf_10m_  10
    $0 query.random.2 wbf_100m_ 10
    ;;

  'query.random.values.general')
    $0 query.random.values $2 1 3
    $0 query.random.values $2 10 3
    $0 query.random.values $2 100 3
    $0 query.random.values $2 1000 3
    ;;

  'query.random.values.nbf')
    $0 query.random.values.general nbf_1k_
    $0 query.random.values.general nbf_100k_
    $0 query.random.values.general nbf_1m_
    $0 query.random.values.general nbf_10m_
    $0 query.random.values.general nbf_100m_
    ;;

  'query.random.values.wbf')
    $0 query.random.values.general wbf_1k_
    $0 query.random.values.general wbf_100k_
    $0 query.random.values.general wbf_1m_
    $0 query.random.values.general wbf_10m_
    $0 query.random.values.general wbf_100m_
    ;;

  'query.random.values.2.general')
    $0 query.random.values.2 $2 1 3
    $0 query.random.values.2 $2 10 3
    $0 query.random.values.2 $2 100 3
    $0 query.random.values.2 $2 1000 3
    ;;

  'query.random.values.2.nbf')
    $0 query.random.values.2.general nbf_1k_
    $0 query.random.values.2.general nbf_100k_
    $0 query.random.values.2.general nbf_1m_
    $0 query.random.values.2.general nbf_10m_
    $0 query.random.values.2.general nbf_100m_
    ;;

  'query.random.values.2.wbf')
    $0 query.random.values.2.general wbf_1k_
    $0 query.random.values.2.general wbf_100k_
    $0 query.random.values.2.general wbf_1m_
    $0 query.random.values.2.general wbf_10m_
    $0 query.random.values.2.general wbf_100m_
    ;;

  'query.random.values.ratio.general')
    $0 query.random.values.ratio $2 99:1  3
    $0 query.random.values.ratio $2 25:75 3
    $0 query.random.values.ratio $2 50:50 3
    $0 query.random.values.ratio $2 75:25 3
    $0 query.random.values.ratio $2 1:99  3

    $0 query.random.values.ratio $2 999:1   3
    $0 query.random.values.ratio $2 250:750 3
    $0 query.random.values.ratio $2 500:500 3
    $0 query.random.values.ratio $2 750:250 3
    $0 query.random.values.ratio $2 1:999   3
    ;;

  'query.random.values.ratio.nbf')
    $0 query.random.values.ratio.general nbf_1k_
    $0 query.random.values.ratio.general nbf_100k_
    $0 query.random.values.ratio.general nbf_1m_
    $0 query.random.values.ratio.general nbf_10m_
    $0 query.random.values.ratio.general nbf_100m_
    ;;

  'query.random.values.ratio.wbf')
    $0 query.random.values.ratio.general wbf_1k_
    $0 query.random.values.ratio.general wbf_100k_
    $0 query.random.values.ratio.general wbf_1m_
    $0 query.random.values.ratio.general wbf_10m_
    $0 query.random.values.ratio.general wbf_100m_
    ;;

  'run.nbf')
    $0 query.random.nbf
    $0 query.random.2.nbf
    $0 query.random.values.nbf
    $0 query.random.values.2.nbf
    $0 query.random.values.ratio.nbf
    ;;

  'run.wbf')
    $0 query.random.wbf
    $0 query.random.2.wbf
    $0 query.random.values.wbf
    $0 query.random.values.2.wbf
    $0 query.random.values.ratio.wbf
    ;;

  *)
    java -jar $JAR_FILE
    ;;

esac
exit 0 
