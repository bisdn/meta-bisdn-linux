#!/bin/sh

IF=$1
ACTION=$2

PVID=$(bridge -d vlan show dev $IF | grep PVID | tr -s ' ' | cut -d ' ' -f 2)
[ -n"$PVID" ] || exit 1

case "$ACTION" in
  CTRL-EVENT-EAP-PROPOSED-METHOD |\
  CTRL-EVENT-EAP-STARTED |\
  CTRL-EVENT-EAP-SUCCESS |\
  CTRL-EVENT-EAP-SUCCESS2 |\
  CTRL-EVENT-EAP-FAILURE |\
  CTRL-EVENT-EAP-FAILURE2)
    # nothing to do here
    ;;
  AP-STA-CONNECTED)
    MAC=$3
    bridge fdb add $MAC dev $IF vlan $PVID master
    ;;
  AP-STA-DISCONNECTED)
    MAC=$3
    bridge fdb del $MAC dev $IF vlan $PVID master 2>/dev/null
    ;;
  *)
    echo "Unhandled action $ACTION on IF $IF (EVENT: $*)" >&2
    ;;
esac
