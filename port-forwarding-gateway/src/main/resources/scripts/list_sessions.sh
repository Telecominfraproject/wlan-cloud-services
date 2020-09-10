#!/bin/bash
# Prints a list of currently active PortForwardingGateway sessions
# Usage: list_sessions.sh --host=pfGatewayHost --port=pfGatewayPort --ca=/opt/tip-wlan/certs/cacert.pem --cert=/opt/tip-wlan/certs/clientcert.pem --key=/opt/tip-wlan/certs/clientkey_dec.pem
#

# parse command line options
die() { echo "$*" >&2; exit 2; }  # complain to STDERR and exit with error
needs_arg() { if [ -z "$OPTARG" ]; then die "No arg for --$OPT option"; fi; }
extract_error_msg() { arg="$*"; jsonModel=$( echo $arg | grep model_type ) ; if [ "X$jsonModel" != "X" ]; then echo $( echo $arg | jq .error ); else echo $arg ; fi; }

while getopts ":h:p:-:" OPT; do
  # https://stackoverflow.com/questions/402377/using-getopts-to-process-long-and-short-command-line-options
  # support long options: https://stackoverflow.com/a/28466267/519360
  if [ "$OPT" = "-" ]; then   # long option: reformulate OPT and OPTARG
    OPT="${OPTARG%%=*}"       # extract long option name
    OPTARG="${OPTARG#$OPT}"   # extract long option argument (may be empty)
    OPTARG="${OPTARG#=}"      # if long option argument, remove assigning `=`
  fi

  case "$OPT" in
    verbose )      verbose_output=true ;;
    h | host )     needs_arg; pfg_host="$OPTARG" ;;
    p | port )     needs_arg; pfg_port="$OPTARG" ;;
    ca )           needs_arg; pfg_ca="$OPTARG" ;;
    cert )         needs_arg; pfg_cert="$OPTARG" ;;
    key )          needs_arg; pfg_key="$OPTARG" ;;
    help )         echo "Prints a list of currently active PortForwardingGateway sessions"
                   echo "Usage: list_sessions.sh --host=pfGatewayHost --port=pfGatewayPort --ca=/opt/tip-wlan/certs/cacert.pem --cert=/opt/tip-wlan/certs/clientcert.pem --key=/opt/tip-wlan/certs/clientkey_dec.pem "
                   exit 0
                   ;;
    ??* )          die "Illegal option --$OPT" ;;  # bad long option
    \? )           die "Illegal option --$OPT" ; exit 2 ;;  # bad short option (error reported via getopts)
  esac
done
shift $((OPTIND-1)) # remove parsed options and args from $@ list


if [ $verbose_output ]; then
printf "command line parameters: \n"
printf "pfg_host: %s\n" "$pfg_host"
printf "pfg_port: %s\n" "$pfg_port"
printf "pfg_ca: %s\n" "$pfg_ca"
printf "pfg_cert: %s\n" "$pfg_cert"
printf "pfg_key: %s\n" "$pfg_key"
fi

#
# *** Config section
#

pfg_host=${pfg_host:-127.0.0.1}
pfg_port=${pfg_port:-9092}
pfg_ca=${pfg_ca:-/opt/tip-wlan/certs/cacert.pem}
pfg_cert=${pfg_cert:-/opt/tip-wlan/certs/clientcert.pem}
pfg_key=${pfg_key:-/opt/tip-wlan/certs/clientkey_dec.pem}

#
# *** End Of Config section
#

if [ $verbose_output ]; then
printf "after configuring parameters: \n"
printf "pfg_host: %s\n" "$pfg_host"
printf "pfg_port: %s\n" "$pfg_port"
printf "pfg_ca: %s\n" "$pfg_ca"
printf "pfg_cert: %s\n" "$pfg_cert"
printf "pfg_key: %s\n" "$pfg_key"
fi

pf_sessions=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key https://$pfg_host:$pfg_port/api/portFwd/listSessions/ )
pf_sessions_rc=$?
pf_error=$( echo $pf_sessions| grep error )

if [ "X$pf_error" != "X" -o $pf_sessions_rc -ne 0 ] ; then
  die "Error : $( extract_error_msg $pf_sessions )"
fi

echo "Existing Sessions: $pf_sessions "
