terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.6.0"
    }
  }
}

provider "docker" {
  # Configuration options
}

resource "docker_network" "share-shere-local" {
  name = "share-shere-local"
  driver = "bridge"
}

resource "docker_image" "postgres" {
  name = "postgres:latest"
  keep_locally = false
}

resource "docker_volume" "postgres_data" {
  name = "postgres-data"
}

resource "docker_container" "postgres_container" {
  name  = "postgres-local"
  image = docker_image.postgres.image_id
  networks_advanced {
    name = docker_network.share-shere-local.name
  }
  ports {
    internal = 5432
    external = 5432
  }

  env = [
    "POSTGRES_USER=admin",
    "POSTGRES_PASSWORD=admin",
    "POSTGRES_DB=sharesphere"
  ]

  volumes {
    volume_name = docker_volume.postgres_data.name
    container_path = "/var/lib/postgresql/data"
  }

  restart = "unless-stopped"
}


