terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.81.0"
    }
  }
}

provider "aws" {
  region = "ap-south-1"
}
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Retrieve the latest ECS-optimized AMI
data "aws_ami" "ecs_optimized" {
  most_recent = true
  owners = ["amazon"]

  filter {
    name = "name"
    values = ["amzn2-ami-ecs-hvm-*-x86_64-ebs"]
  }
}

resource "aws_ecs_cluster" "ecs_cluster" {
  name = "app-cluster"
}

# Create a security group for the EC2 instance and ECS service
resource "aws_security_group" "ecs_sg" {
  name        = "ecs-ec2-sg"
  description = "Security group for ECS EC2 cluster"
  vpc_id      = data.aws_vpc.default.id

  # Allow HTTP inbound traffic
  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow SSH for debugging (optional, can be removed)
  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow all outbound traffic
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


# Create an IAM role for EC2 instances to interact with ECS
resource "aws_iam_role" "ecs_instance_role" {
  name = "ecsInstanceRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# Attach the ECS instance role policy
resource "aws_iam_role_policy_attachment" "ecs_instance_policy" {
  role       = aws_iam_role.ecs_instance_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

# Create an instance profile for the EC2 instance
resource "aws_iam_instance_profile" "ecs_instance_profile" {
  name = "ecsInstanceProfile"
  role = aws_iam_role.ecs_instance_role.name
}
# Create a launch template for the Auto Scaling group
resource "aws_launch_template" "ecs_launch_template" {
  name_prefix   = "ecs-launch-template-"
  image_id      = data.aws_ami.ecs_optimized.id
  instance_type = "t2.micro" # Free Tier-eligible

  iam_instance_profile {
    name = aws_iam_instance_profile.ecs_instance_profile.name
  }
  network_interfaces {
    associate_public_ip_address = true
    subnet_id                   = data.aws_subnets.default.ids[0]
    security_groups = [aws_security_group.ecs_sg.id] # Moved security groups here
  }
  user_data = base64encode(<<-EOF
              #!/bin/bash
              echo ECS_CLUSTER=${aws_ecs_cluster.ecs_cluster.name} >> /etc/ecs/ecs.config
              EOF
  )

  block_device_mappings {
    device_name = "/dev/xvda"
    ebs {
      volume_size = 30 # Matches AMI snapshot requirement
      volume_type = "gp2"
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}

# Create an Auto Scaling group
resource "aws_autoscaling_group" "ecs_asg" {
  name                = "ecs-asg"
  min_size            = 1
  max_size            = 1
  desired_capacity    = 1
  vpc_zone_identifier = data.aws_subnets.default.ids


  launch_template {
    id      = aws_launch_template.ecs_launch_template.id
    version = "$Latest"

  }

  tag {
    key                 = "Name"
    value               = "ecs-ec2-instance"
    propagate_at_launch = true
  }

  lifecycle {
    create_before_destroy = true
  }
}


# Create an ECS capacity provider
resource "aws_ecs_capacity_provider" "ecs_capacity_provider" {
  name = "capacity-provider-ecs"


  auto_scaling_group_provider {
    auto_scaling_group_arn = aws_autoscaling_group.ecs_asg.arn

    managed_scaling {
      maximum_scaling_step_size = 1
      minimum_scaling_step_size = 1
      status                    = "ENABLED"
      target_capacity           = 1
    }
  }
}

# Associate the capacity provider with the ECS cluster
resource "aws_ecs_cluster_capacity_providers" "main" {
  cluster_name = aws_ecs_cluster.ecs_cluster.name
  capacity_providers = [aws_ecs_capacity_provider.ecs_capacity_provider.name]

  default_capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_capacity_provider.name
    weight            = 1
  }
}

resource "aws_lb" "ecs_alb" {

  name = "ecs-alb"

  internal = false

  load_balancer_type = "application"

  security_groups = [aws_security_group.ecs_sg.id]

  subnets = data.aws_subnets.default.ids

  tags = {
    Name = "ecs-alb"
  }

}

resource "aws_lb_target_group" "ecs_tg" {

  name = "ecs-target-group"

  port = 80

  protocol = "HTTP"

  target_type = "ip"

  vpc_id = data.aws_vpc.default.id

  health_check {
    path = "/"
  }

}


resource "aws_lb_listener" "ecs_alb_listener" {

  load_balancer_arn = aws_lb.ecs_alb.arn

  port = 80

  protocol = "HTTP"



  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.ecs_tg.arn
  }

}

resource "aws_ecs_task_definition" "nginx_task" {
  family       = "nginx-task"
  network_mode = "awsvpc"
  requires_compatibilities = ["EC2"]
  container_definitions = jsonencode([
    {
      name      = "nginx-ecs-demo"
      image     = "nginx:latest"
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
          protocol      = "tcp"
        }
      ]
      memory = 128
      cpu    = 100
    }
  ])
}

resource "aws_ecs_service" "ecs_service" {

  name            = "my-ecs-service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.nginx_task.arn
  desired_count   = 1

  network_configuration {
    subnets = data.aws_subnets.default.ids
    security_groups = [aws_security_group.ecs_sg.id]
  }

  force_new_deployment = true
  placement_constraints {
    type = "distinctInstance"
  }

  triggers = {
    redeployment = timestamp()
  }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_capacity_provider.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.ecs_tg.arn
    container_name   = "nginx-ecs-demo"
    container_port   = 80
  }

  depends_on = [aws_autoscaling_group.ecs_asg]

}