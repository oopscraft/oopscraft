#!/bin/bash
APP_FILE=$(basename ${0})
APP_NAME=${APP_FILE%.*}
PID_FILE=${APP_NAME}.pid
touch ${PID_FILE}
PID=$(cat ${PID_FILE})

# start
function start() {
	java -cp hsqldb.jar org.hsqldb.server.Server -database.0 file:application -dbname.0 application &
	echo $! > ${PID_FILE}
}

# status
function status() {
	ps -f ${PID} | grep java | grep "hsqldb.jar org.hsqldb.server.Server"
}

# stop
function stop() {
	java -jar sqltool.jar --inlineRc=url=jdbc:hsqldb:hsql://127.0.0.1/application,user=SA,password= --sql "shutdown;"
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
	*)
		echo "Usage: ${0} [start|status|stop]"
		;;
esac

exit 0
