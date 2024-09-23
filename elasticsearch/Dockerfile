# Dockerfile for Elasticsearch
FROM docker.elastic.co/elasticsearch/elasticsearch:7.15.1

# Install plugins
RUN elasticsearch-plugin install analysis-nori && \
    elasticsearch-plugin install https://github.com/skyer9/elasticsearch-jaso-analyzer/releases/download/v7.15.1/jaso-analyzer-plugin-7.15.1-plugin.zip

# Set memory optimization options
ENV ES_JAVA_OPTS="-Xms2g -Xmx2g"
