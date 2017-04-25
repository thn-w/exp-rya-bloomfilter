#!/bin/bash

USER_NAME=SPEAR
USER_PASSWORD=spear

SCRIPT_NBF=nbf
SCRIPT_WBF=wbf

case "$1" in
  'nbf.create')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_10m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1b_create.acc
    ;;

  'nbf.drop')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_10m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_100m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_NBF/nbf_1b_drop.acc
    ;;

  'wbf.create')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100k_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_10m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100m_create.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1b_create.acc
    ;;

  'wbf.drop')
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100k_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_10m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_100m_drop.acc
    accumulo shell -u $USER_NAME -p $USER_PASSWORD -f $SCRIPT_WBF/wbf_1b_drop.acc
    ;;

  *)
    echo "Usage: $0 { nbf.create | nbf.drop | wbf.create | wbf.drop }"

esac
exit 0
