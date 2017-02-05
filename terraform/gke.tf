variable "google_project" {
  description = "The project where this should run"
  default = "caius"
}

variable "google_region" {
  description = "The region where this cluster should run"
  default = "us-east1"
}

variable "google_zone" {
  description = "The zone where this cluster should run"
  default = "us-east1-c"
}

provider "google" {
  credentials = "${file("google_account.json")}"
  project = "${var.google_project}"
  region = "${var.google_region}"
}

resource "google_container_cluster" "primary" {
  name = "caius-cluster"
  zone = "${var.google_zone}"
  initial_node_count = 3

  master_auth {
    username = "admin"
    password = "password"
  }

  node_config {
    oauth_scopes = [
      "https://www.googleapis.com/auth/compute",
      "https://www.googleapis.com/auth/devstorage.read_only",
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring"
    ]
  }

  provisioner "local-exec" {
    command = "gcloud container clusters get-credentials ${google_container_cluster.primary.name} --zone ${google_container_cluster.primary.zone} --project ${var.google_project}"
  }
}
