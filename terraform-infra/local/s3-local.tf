resource "docker_image" "minio" {
  name         = "minio/minio:latest"
  keep_locally = true
}

# Create a Docker volume for MinIO data persistence
resource "docker_volume" "minio_data" {
  name = "minio-data"
}

# Create a MinIO container
resource "docker_container" "minio_container" {
  name  = "minio-local"
  image = docker_image.minio.image_id

  ports {
    internal = 9000
    external = 9000
  }
  ports {
    internal = 9001
    external = 9001
  }

  env = [
    "MINIO_ROOT_USER=minioadmin",
    "MINIO_ROOT_PASSWORD=miniopassword"
  ]

  volumes {
    volume_name    = docker_volume.minio_data.name
    container_path = "/data"
  }

  command = ["server", "/data", "--console-address", ":9001"]

  restart = "unless-stopped"
}