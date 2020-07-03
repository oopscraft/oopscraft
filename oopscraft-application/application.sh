#!/bin/bash
APP_FILE=$(basename ${0})
APP_NAME=${APP_FILE%.*}
PID_FILE=${APP_NAME}.pid
touch ${PID_FILE}
PID=$(cat ${PID_FILE})
MAIN_CLASS="net.oopscraft.application.Application"

# load profile
source ${APP_NAME}.env

# start
function start() {
	echo "+ Start Application"
	if [ -f /proc/${PID}/status ]; then
		echo "Application is already running." 
		status
		exit -1
	fi
	CLASSPATH="./*:./lib/*"
	JAVA_OPTS=" -server -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=file:conf/log4j2.xml "${JAVA_OPTS}
	java ${JAVA_OPTS} -classpath ${CLASSPATH} ${MAIN_CLASS} > /dev/null & 
	echo $! > ${PID_FILE}
}

# status
function status() {
    echo "Application Status";
	ps -f ${PID} | grep java | grep -F ${MAIN_CLASS}
}

# stop
function stop() {
    echo "Stop Application";
	if [ ! -f /proc/${PID}/status ]; then
		echo "Application is not running."
		exit -1
	fi
	printf "shutting down."
	kill -15 ${PID}
	while kill -0 ${PID} 2>/dev/null; do printf "."; sleep 1; done
	echo -e "\nshutdown is complete."
}

# main
case ${1} in
    start) 
        start
        ;;
	status)
		status
		;;
	stop)
		stop
		;;
	restart)
		stop
		start
		;;
    *)
        echo "Usage: ${0} [start|status|stop|restart]"
        ;;
esac

# exit
exit 0


