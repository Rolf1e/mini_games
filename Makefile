# === COMPILATION ===
compile: 
	./mill __.compile

compile-server:
	./mill server.compile

# === TEST ===
test: test-server

test-server:
	./mill server.test

# === TOOLING ===
intellij: 
	./mill mill.scalalib.GenIdea/idea

# === DOCKER ===
docker-database:
    docker compose up -d

docker: docker-database