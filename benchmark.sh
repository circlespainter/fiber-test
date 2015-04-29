#!/bin/bash

jfrOpts="-XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:FlightRecorderOptions=defaultrecording=true,dumponexit=true"

set -e

[ -z "$workerCount" ] && workerCount=503
[ -z "$ringSize" ] && ringSize=1000000
[ -z "$rings" ] && rings=2
[ -z "$fiberParallelism" ] && fiberParallelism=$rings
[ -z "$businessLogic" ] && businessLogic=null
[ -z "$blArraySize" ] && blArraySize=48
[ -z "$quasarAgentLocation" ] && quasarAgentLocation=$HOME/.m2/repository/co/paralleluniverse/quasar-core/0.6.3-SNAPSHOT/quasar-core-0.6.3-SNAPSHOT.jar
[ -z "$bytemanAgentLocation" ] && bytemanAgentLocation="$HOME/.m2/repository/org/jboss/byteman/byteman/2.2.1/byteman-2.2.1.jar"
[ -z "$warmupIters" ] && warmupIters=5
[ -z "$iters" ] && iters=10
[ -z "$stat" ] && stat=avg
[ -z "$unit" ] && unit=ms
[ -z "$forks" ] && forks=5
[ -z "$benchRegexp" ] && benchRegexp=".*Benchmark.*"
[ "$enableJfr" != "true" ] && enableJfr=false && jfrOpts=""

if [ "$1" = "-h" -o "$1" = "--help" ]; then
    echo "Available environment parameters (with defaults):"
    echo "    workerCount             ($workerCount)"
    echo "    ringSize                ($ringSize)"
    echo "    rings                   ($rings)"
    echo "    fiberParallelism        ($fiberParallelism)"
    echo "    businessLogic           ($businessLogic)"
    echo "    blArraySize             ($blArraySize)"
    echo "    quasarAgentLocation     ($quasarAgentLocation)"
    echo "    bytemanAgentLocation    ($bytemanAgentLocation)"
    echo "    warmupIters             ($warmupIters)"
    echo "    iters                   ($iters)"
    echo "    stat                    ($stat)"
    echo "    unit                    ($unit)"
    echo "    forks                   ($forks)"
    echo "    benchRegexp             ($benchRegexp)"
    echo "    enableJfr               ($enableJfr)"
    exit 0
fi

error() {
    echo "$@" >&2
}

if [ ! -e "$quasarAgentLocation" ]; then 
    error "Could not locate agent."
    error "Expected path: $quasarAgentLocation"
    exit 1
fi

# -Dorg.jboss.byteman.verbose
# -Dco.paralleluniverse.fiber.verifyInstrumentation=true
cmd="$JAVA_HOME/bin/java -server -XX:+TieredCompilation -XX:+AggressiveOpts -jar target/ring-bench.jar\
 -jvmArgsAppend \"-server -XX:+TieredCompilation -XX:+AggressiveOpts -Xbootclasspath/p:$bytemanAgentLocation $jfrOpts -Dorg.jboss.byteman.transform.all -DworkerCount=$workerCount -DringSize=$ringSize -Drings=$rings -DfiberParallelism=$fiberParallelism -DbusinessLogic=$businessLogic -Dringbench.BusinessLogic.arraySize=$blArraySize -javaagent:$quasarAgentLocation -javaagent:$bytemanAgentLocation=script:script.btm\"\
 -wi $warmupIters -i $iters -bm $stat -tu $unit -f $forks \"$benchRegexp\""

echo "$cmd"
eval "$cmd"
