module "servers" {
  source                = "../common/server"
  app_region            = "eu-west-2"
  server_ami            = "ami-0cbf43fd299e3a464"
  server_instance_type  = "t3.medium"
  server_count          = 1

  docker_image          = var.docker_payment_image
  aws_security_group_id = var.aws_security_group_id
  aws_key_pair_name     = var.aws_key_pair_name
  KAFKA_SERVER_IP       = var.KAFKA_SERVER_IP
  DATABASE_URL          = var.DATABASE_URL
  DATABASE_USERNAME     = var.DATABASE_USERNAME
  DATABASE_PASSWORD     = var.DATABASE_PASSWORD
}

module "nginx" {
  source                = "../common/load_balancer"
  app_region            = "eu-west-2"
  server_ami            = "ami-0cbf43fd299e3a464"
  server_instance_type  = "t3.medium"

  aws_security_group_id = var.aws_security_group_id
  aws_key_pair_name     = var.aws_key_pair_name
  servers               = module.servers.server_ips
}

variable "aws_security_group_id" {
}

variable "aws_key_pair_name" {
}

variable "KAFKA_SERVER_IP" {
}

variable "DATABASE_URL" {
}

variable "DATABASE_USERNAME" {
}

variable "DATABASE_PASSWORD" {
}

variable "docker_payment_image" {
  description = "Docker payment image name"
  type        = string
}

output payments_nginx_public_ip {
  value = module.nginx.load_balancer_public_id
}