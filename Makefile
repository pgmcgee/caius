build:
	lein ring uberjar
	docker build -t caius-web .

start:
	docker-compose build
	docker-compose up

stop:
	docker-compose down

provision:
	cd terraform && terraform apply -var-file=gke.tfvars

proxy:
	@echo "Open http://127.0.0.1:8001/ui"
	kubectl proxy


deploy-image: build
	docker tag caius-web gcr.io/caius-157820/caius-web
	gcloud docker -- push gcr.io/caius-157820/caius-web

deploy: deploy-image
	kubectl create deployment caius-web --image=gcr.io/caius-157820/caius-web
	kubectl expose deployment caius-web --port=3000 --target-port=3000 --name=caius-web --type=LoadBalancer
	kubectl create deployment selenium-chrome --image=registry.hub.docker.com/selenium/standalone-chrome
	kubectl expose deployment selenium-chrome --port=4444 --target-port=4444 --name=selenium-chrome

destroy:
	cd terraform && terraform destroy -var-file=gke.tfvars

open:
	$(eval EXTERNAL_HOST=$(shell kubectl describe services caius-web | grep 'LoadBalancer Ingress' | awk '{print $$3}'))
	open "http://$(EXTERNAL_HOST):3000/"
