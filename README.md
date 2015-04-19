This project bundles a set of benchmarks that aims to measure the performance
of a multitude of threading implementations in the Java Virtual Machine.

Benchmarks
----------

As of now, there is just a single benchmark (ring) adopted from the
[Performance Measurements of Threads in Java and Processes in
Erlang](http://www.sics.se/~joe/ericsson/du98024.html) article. I plan to
introduce new benchmarks as time allows. That being said, contributions are
welcome.

Implementations
---------------

Project employs 3 threading libraries to implement fibers in the benchmarks.

1. [Standard Java Threads](http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html)
2. [Akka Actors](http://akka.io/)
3. [Quasar Fibers and Actors](http://docs.paralleluniverse.co/quasar/)

Usage
-----

You first need to build the JMH Uber JAR using Maven.

    $ mvn clean install

Next, you can either use the provided `benchmark.sh` script

    $ ./benchmark.sh --help

    # You can run with default parameters.
    $ ./benchmark.sh

    # Alternatively, you can configure parameters through environment variables.
    $ ringSize=500 workerCount=30 ./benchmark.sh

License
-------

The [fiber-test](https://github.com/vy/fiber-test/) by [Volkan Yazıcı](http://vlkan.com/) is licensed under the [Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

[![Creative Commons Attribution 4.0 International License](http://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)
