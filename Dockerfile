FROM jetty

RUN java -jar "$JETTY_HOME/start.jar" --add-to-startd=jmx,stats
