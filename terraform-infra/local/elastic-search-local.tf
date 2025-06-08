# Pull Elasticsearch image
resource "docker_image" "elasticsearch" {
  name         = "elasticsearch:9.0.2"
  keep_locally = false
}

# Run Elasticsearch container
resource "docker_container" "elasticsearch" {
  name  = "elasticsearch-local"
  image = docker_image.elasticsearch.image_id

  ports {
    internal = 9200
    external = 9200
  }

  env = [
    "discovery.type=single-node",              # Run as single node for local setup
    "xpack.security.enabled=false",            # Disable security for simplicity
    "ES_JAVA_OPTS=-Xms512m -Xmx512m" # Set heap size
  ]

  memory = 1024 # Memory limit in MB (1GB)

  # Ensure container restarts on failure
  restart = "unless-stopped"

  # Healthcheck to verify Elasticsearch is running
  healthcheck {
    test     = ["CMD-SHELL", "curl -f http://localhost:9200/_cluster/health || exit 1"]
    interval = "30s"
    timeout  = "10s"
    retries  = 3
  }
}