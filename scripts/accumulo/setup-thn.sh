#!/bin/bash

USER_NAME=SPEAR
USER_PASSWORD=spear

SCRIPT_NBF=thn-nbf
SCRIPT_WBF=thn-wbf

case "$1" in
  'nbf.create')
    $0 nbf.create.k
    $0 nbf.create.m
    $0 nbf.create.b
    ;;

  'nbf.create.k')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100k_create.acc
    ;;

  'nbf.create.m')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_10m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100m_create.acc
    ;;

  'nbf.create.b')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1b_create.acc
    ;;

  'nbf.drop')
    $0 nbf.drop.k
    $0 nbf.drop.m
    $0 nbf.drop.b
    ;;

  'nbf.drop.k')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100k_drop.acc
    ;;

  'nbf.drop.m')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_10m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100m_drop.acc
    ;;

  'nbf.drop.b')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1b_drop.acc
    ;;

  'nbf.compact')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_compact.acc
    ;;

  'wbf.create.k')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100k_create.acc
    ;;

  'wbf.create.m')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_10m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100m_create.acc
    ;;

  'wbf.create.b')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1b_create.acc
    ;;

  'wbf.create')
    $0 wbf.create.k
    $0 wbf.create.m
    $0 wbf.create.b
    ;;

  'wbf.drop.k')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100k_drop.acc
    ;;

  'wbf.drop.m')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_10m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100m_drop.acc
    ;;

  'wbf.drop.b')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1b_drop.acc
    ;;

  'wbf.drop')
    $0 wbf.drop.k
    $0 wbf.drop.m
    $0 wbf.drop.b
    ;;

  'wbf.compact')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_compact.acc
    ;;

  *)
    echo "Usage: $0 { nbf.create | nbf.drop | wbf.create | wbf.drop }"

esac
exit 0
