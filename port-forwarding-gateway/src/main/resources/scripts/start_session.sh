#!/bin/bash
# Stops a PortForwarding session on the AP
# Usage: start_session.sh --host=pfGatewayHost --port=pfGatewayPort --ca=/opt/tip-wlan/certs/cacert.pem --cert=/opt/tip-wlan/certs/clientcert.pem \
#           --key=/opt/tip-wlan/certs/clientkey_dec.pem --inventoryId=ap-1 --apUser=root --multiplexingPort=8888 --customizedSsh=/Users/user/openssh-portable/ssh
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
    verbose )          verbose_output=true ;;
    h | host )         needs_arg; pfg_host="$OPTARG" ;;
    p | port )         needs_arg; pfg_port="$OPTARG" ;;
    inventoryId )      needs_arg; inventory_id="$OPTARG" ;;
    multiplexingPort ) needs_arg; multiplexing_port="$OPTARG" ;;
    customizedSsh )    needs_arg; customized_ssh="$OPTARG" ;;
    ca )               needs_arg; pfg_ca="$OPTARG" ;;
    cert )             needs_arg; pfg_cert="$OPTARG" ;;
    key )              needs_arg; pfg_key="$OPTARG" ;;
    apUser)            needs_arg; ap_user="$OPTARG" ;;
    help )             echo "Starts a PortForwarding session on the AP and connects to it with ssh"
                       echo "Usage: start_session.sh --host=pfGatewayHost --port=pfGatewayPort --ca=/opt/tip-wlan/certs/cacert.pem --cert=/opt/tip-wlan/certs/clientcert.pem --key=/opt/tip-wlan/certs/clientkey_dec.pem --inventoryId=ap-1 --apUser=root --multiplexingPort=8888 --customizedSsh=/Users/user/openssh-portable/ssh"
                       exit 0
                       ;;
    ??* )              die "Illegal option --$OPT" ;;  # bad long option
    \? )               die "Illegal option --$OPT" ;;  # bad short option (error reported via getopts)
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
printf "inventory_id: %s\n" "$inventory_id"
printf "multiplexing_port: %s\n" "$multiplexing_port"
printf "customized_ssh: %s\n" "$customized_ssh"
printf "ap_user: %s\n" "$ap_user"
fi

if [ ! $inventory_id ]; then
  die "Missing option --inventoryId=... "
fi

#
# *** Config section
#

pfg_host=${pfg_host:-127.0.0.1}
pfg_port=${pfg_port:-9092}
pfg_ca=${pfg_ca:-/opt/tip-wlan/certs/cacert.pem}
pfg_cert=${pfg_cert:-/opt/tip-wlan/certs/clientcert.pem}
pfg_key=${pfg_key:-/opt/tip-wlan/certs/clientkey_dec.pem}
multiplexing_port=${multiplexing_port:-8888}
customized_ssh=${customized_ssh:-~/openssh-portable/ssh}
ap_user=${ap_user:-root}

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
printf "multiplexing_port: %s\n" "$multiplexing_port"
printf "customized_ssh: %s\n" "$customized_ssh"
printf "ap_user: %s\n" "$ap_user"
fi

if [ ! -x $customized_ssh ]; then
  die "Customized ssh binary '$customized_ssh' not found. Use --customizedSsh command line argument to set it."
fi

wait_num_sec_for_agent_start=15

#
# check if there is an old instance of port forwarding agent running, and stop it
# without this new session will hang on login prompt
#

pf_agent_status=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key https://$pfg_host:$pfg_port/api/portFwd/agentStatus/inventoryId/$inventory_id/ )
pf_result_rc=$?
if [ $verbose_output ]; then
  echo "Port Forwarding Agent on AP: $pf_agent_status"
fi

pf_error=$( echo $pf_agent_status| grep error )

if [ "X$pf_error" != "X" -o $pf_result_rc -ne 0 ] ; then
  die "Error : $( extract_error_msg $pf_agent_status )"
fi

if [ "X$pf_agent_status" == "Xup" ]; then
  echo "Old Agent still running on $inventory_id, will stop it ..."

  stopAgentResponse=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key --header "Content-Type: application/json; charset=utf-8" --data '' https://$pfg_host:$pfg_port/api/portFwd/stopAgent/inventoryId/$inventory_id/ )

  if [ $verbose_output ]; then
    echo "Stop old agent response: $stopAgentResponse"
  fi

fi

#
# done checking for an old instance of port forwarding agent
#

echo "Starting port forwarding agent on $inventory_id ..."

startAgentResponse=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key --header "Content-Type: application/json; charset=utf-8" --data '' https://$pfg_host:$pfg_port/api/portFwd/startAgent/inventoryId/$inventory_id/ )

if [ "X$startAgentResponse" != "Xsuccess" ] ; then
  echo "Got error trying to start forwarding agent : $( extract_error_msg $startAgentResponse )"
  echo "Will try to establish session anyway"
fi

if [ $verbose_output ]; then
  echo "Start agent response: $startAgentResponse"
fi

echo "Waiting for the agent to start on AP ..."
LIMIT=$wait_num_sec_for_agent_start
for ((a=1; a <= LIMIT ; a++))
do
	#wait for debug connection to be established from AP to the portForwarder
	sleep 1

  pf_agent_status=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key https://$pfg_host:$pfg_port/api/portFwd/agentStatus/inventoryId/$inventory_id/ )
  pf_result_rc=$?

	if [ $verbose_output ]; then
		echo "Agent Status: $pf_agent_status"
	fi

	pf_error=$( echo $pf_agent_status| grep error )

	if [ "X$pf_error" != "X" -o $pf_result_rc -ne 0 ] ; then
	  die "Got error : $( extract_error_msg $pf_agent_status )"
	fi

	if [ "X$pf_agent_status" == "Xup" ]; then
    echo "Agent started"
    break
	fi

	#done waiting for debug session
done

if [ "X$pf_agent_status" != "Xup" ]; then
  die "Agent was not started"
fi

#
# Agent has been started on AP at this point, time to start the session
#
echo "Starting port forwarding session for $inventory_id ..."

pf_session=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key --header "Content-Type: application/json; charset=utf-8" --data '' https://$pfg_host:$pfg_port/api/portFwd/createSession/inventoryId/$inventory_id/port/22/ )
pf_result_rc=$?

#echo $pf_session
# 59525-1422023542297210192-dev-ap-0001-22@127.0.0.1

pf_error=$( echo $pf_session| grep error )

if [ "X$pf_error" != "X" -o $pf_result_rc -ne 0 ] ; then
  die "Got error : $( extract_error_msg $pf_session )"
fi

pf_port=$( echo $pf_session|cut -f1 -d'-' )
pf_token=$( echo $pf_session|cut -f2 -d'-' )
pf_gateway=$( echo $pf_session|cut -f2 -d'@' )

if [ $verbose_output ]; then
  echo "Port: $pf_port"
  echo "Token: $pf_token"
  echo "Port Forwarding Gateway: $pf_gateway"
fi

enable_port_multiplexing="-D 127.0.0.1:$multiplexing_port"
extra_ssh_args=" $enable_port_multiplexing -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -o ServerAliveInterval=60 "
# Adding -F option which specifies location of the ssh config file.
# Default config file may have options that are incompatible with portable_ssh, so we are using an empty config file here.
extra_ssh_args=" $extra_ssh_args -F /dev/null "
extra_ssh_args=" $extra_ssh_args -o LogLevel=QUIET "

export SSH_BANNER_COMMENTS="$pf_token"
export full_ssh_command="$customized_ssh -p $pf_port $extra_ssh_args $ap_user@$pf_gateway"


ap_ipaddress=127.0.0.1

## Examples of use:
echo "Things you can do after establishing master SSH connection:"
echo "* Start another ssh session:"
echo "      ssh -o ProxyCommand=\"nc -x 127.0.0.1:$multiplexing_port %h %p\" -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -o LogLevel=QUIET $ap_user@$ap_ipaddress"
echo "* Copy files to/from AP:"
echo "      scp -o ProxyCommand=\"nc -x 127.0.0.1:$multiplexing_port %h %p\" -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -o LogLevel=QUIET root@$ap_ipaddress:/path/to/test.txt ."
echo "* Use http to connect to AP web UI:"
echo "      Configure web browser to use SOCKS proxy on 127.0.0.1:$multiplexing_port"
echo "  or use curl:"
echo "      curl --proxy socks5://127.0.0.1:$multiplexing_port http://$ap_ipaddress"
##

echo "Starting master SSH session: $full_ssh_command "
$full_ssh_command
echo "Master SSH session stopped"

#
# Session should be stopped by the PF Gateway when ssh disconnects.
# But sometimes sockects linger longer than expected, in those cases it is possible to stop the session manually:
#
#stopSessionResponse=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key --header "Content-Type: application/json; charset=utf-8" --data '' https://$pfg_host:$pfg_port/api/portFwd/stopSession/$pf_session/ )
#if [ $verbose_output ]; then
#  echo "Stop session response: $stopSessionResponse"
#fi
#

echo "Stopping port forwarding agent on $inventory_id ..."

stopAgentResponse=$( curl --silent --show-error --insecure --capath $pfg_ca --cert $pfg_cert --key $pfg_key --header "Content-Type: application/json; charset=utf-8" --data '' https://$pfg_host:$pfg_port/api/portFwd/stopAgent/inventoryId/$inventory_id/ )

if [ $verbose_output ]; then
  echo "Stop agent response: $stopAgentResponse"
fi
